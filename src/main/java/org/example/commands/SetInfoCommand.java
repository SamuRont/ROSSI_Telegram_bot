package org.example.commands;

import com.google.gson.JsonArray;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class SetInfoCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        String setId = u.getMessage().getText().replace("/setinfo", "").trim();

        if (setId.isEmpty()) {
            send(c, u, """
                ⚠️ Uso: /setinfo <id set>
                
                Set popolari:
                • base1 - Base Set
                • base2 - Jungle
                • base3 - Fossil
                • swsh1 - Sword & Shield
                
                Esempio: /setinfo base1
                """);
            return;
        }

        DatabaseManager.incrementCommandUsage("/setinfo");

        JsonArray cards = PokemonApiClient.getCards("set.id:" + setId);

        if (cards.size() == 0) {
            send(c, u, "❌ Set '" + setId + "' non trovato.\n💡 Verifica l'ID del set.");
            return;
        }

        String response = String.format("""
            📦 Set ID: %s
            🎴 Carte nel set: %d
            
            💡 Usa /type per vedere le carte più forti del set!
            """, setId, cards.size());

        send(c, u, response);
    }
}