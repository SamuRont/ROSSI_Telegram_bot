package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class DeckCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        String type = u.getMessage().getText().replace("/deck", "").trim();

        if (type.isEmpty()) {
            send(c, u, """
                ⚠️ Uso: /deck <tipo>
                
                Tipi disponibili:
                • Fire, Water, Grass
                • Electric, Psychic, Fighting
                • Darkness, Metal, Dragon
                • Fairy, Colorless
                
                Esempio: /deck Fire
                """);
            return;
        }

        DatabaseManager.incrementCommandUsage("/deck");
        DatabaseManager.saveTypeSearch(type, u.getMessage().getFrom().getId());

        JsonArray cards = PokemonApiClient.getCards("types:" + type);

        if (cards.size() == 0) {
            send(c, u, "❌ Nessuna carta trovata per il tipo: " + type);
            return;
        }

        StringBuilder response = new StringBuilder();
        response.append(String.format("💼 DECK BUILDER - Tipo %s\n\n", type));
        response.append(String.format("🎴 Carte disponibili: %d\n\n", cards.size()));
        response.append("🔥 Top 3 consigliate:\n");

        for (int i = 0; i < Math.min(3, cards.size()); i++) {
            JsonObject card = cards.get(i).getAsJsonObject();
            String name = card.get("name").getAsString();
            String hp = card.has("hp") ? card.get("hp").getAsString() : "N/A";
            response.append(String.format("%d. %s (HP: %s)\n", i + 1, name, hp));
        }

        response.append("\n💡 Usa /savedeck per salvare il tuo deck!");

        send(c, u, response.toString());
    }
}