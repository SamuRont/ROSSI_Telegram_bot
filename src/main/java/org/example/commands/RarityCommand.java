package org.example.commands;

import com.google.gson.JsonArray;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class RarityCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        String rarity = u.getMessage().getText().replace("/rarity", "").trim();

        if (rarity.isEmpty()) {
            send(c, u, """
                ⚠️ Uso: /rarity <rarità>
                
                Rarità disponibili:
                • Common
                • Uncommon
                • Rare
                • Rare Holo
                • Ultra Rare
                • Secret Rare
                
                Esempio: /rarity Ultra Rare
                """);
            return;
        }

        DatabaseManager.incrementCommandUsage("/rarity");

        JsonArray cards = PokemonApiClient.getCards("rarity:\"" + rarity + "\"");

        if (cards.size() == 0) {
            send(c, u, "❌ Nessuna carta trovata con rarità: " + rarity);
            return;
        }

        String response = String.format("""
            ⭐ Rarità: %s
            📊 Carte trovate: %d
            
            💡 Usa /look <nome> per vedere una carta specifica
            """, rarity, cards.size());

        send(c, u, response);
    }
}