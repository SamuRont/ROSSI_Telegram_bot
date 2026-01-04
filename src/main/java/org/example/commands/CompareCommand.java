package org.example.commands;

import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class CompareCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        String input = u.getMessage().getText().replace("/compare", "").trim();

        if (!input.contains(";")) {
            send(c, u, """
                ⚠️ Uso: /compare <carta1>;<carta2>
                
                Esempio: /compare Pikachu;Raichu
                
                💡 Separa i nomi con un punto e virgola (;)
                """);
            return;
        }

        String[] parts = input.split(";");
        if (parts.length != 2) {
            send(c, u, "❌ Inserisci esattamente due carte separate da ;");
            return;
        }

        String name1 = parts[0].trim();
        String name2 = parts[1].trim();

        DatabaseManager.incrementCommandUsage("/compare");
        DatabaseManager.saveComparison(name1, name2, u.getMessage().getFrom().getId());

        JsonObject card1 = PokemonApiClient.getFirstCard("name:" + name1);
        JsonObject card2 = PokemonApiClient.getFirstCard("name:" + name2);

        if (card1 == null || card2 == null) {
            send(c, u, "❌ Una o entrambe le carte non sono state trovate.\n💡 Verifica i nomi.");
            return;
        }

        String hp1 = card1.has("hp") ? card1.get("hp").getAsString() : "N/A";
        String hp2 = card2.has("hp") ? card2.get("hp").getAsString() : "N/A";

        String type1 = card1.has("types") ?
                card1.getAsJsonArray("types").get(0).getAsString() : "N/A";
        String type2 = card2.has("types") ?
                card2.getAsJsonArray("types").get(0).getAsString() : "N/A";

        String winner = determineWinner(hp1, hp2);

        String response = String.format("""
            ⚔️ CONFRONTO CARTE
            
            🎴 %s
            ❤️ HP: %s
            ⚡ Tipo: %s
            
            VS
            
            🎴 %s
            ❤️ HP: %s
            ⚡ Tipo: %s
            
            %s
            """,
                card1.get("name").getAsString(), hp1, type1,
                card2.get("name").getAsString(), hp2, type2,
                winner
        );

        send(c, u, response);
    }

    private String determineWinner(String hp1, String hp2) {
        try {
            int h1 = Integer.parseInt(hp1);
            int h2 = Integer.parseInt(hp2);

            if (h1 > h2) return "🏆 Prima carta ha più HP!";
            if (h2 > h1) return "🏆 Seconda carta ha più HP!";
            return "🤝 Parità di HP!";
        } catch (NumberFormatException e) {
            return "📊 Confronta le statistiche per decidere!";
        }
    }
}