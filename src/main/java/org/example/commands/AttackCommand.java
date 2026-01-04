package org.example.commands;

import com.google.gson.JsonArray;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class AttackCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        String attack = u.getMessage().getText().replace("/attack", "").trim();

        if (attack.isEmpty()) {
            send(c, u, """
                ⚠️ Uso: /attack <nome attacco>
                
                Esempi comuni:
                • Thunderbolt
                • Flamethrower
                • Hydro Pump
                • Solar Beam
                
                Esempio: /attack Thunderbolt
                """);
            return;
        }

        DatabaseManager.incrementCommandUsage("/attack");

        JsonArray cards = PokemonApiClient.getCards("attacks.name:\"" + attack + "\"");

        String response = String.format("""
            💥 Attacco: %s
            🎴 Carte con questo attacco: %d
            
            💡 Le carte con attacchi iconici sono spesso più ricercate!
            """, attack, cards.size());

        send(c, u, response);
    }
}