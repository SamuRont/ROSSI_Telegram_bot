package org.example.commands;

import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class DeckCommand extends StartCommand {
    public void run(TelegramClient c, Update u) {
        String t = u.getMessage().getText().replace("/deck","").trim();
        send(c, u, "Suggerimento deck " + t + ": " +
                PokemonApiClient.getCards("types:" + t).size() + " carte trovate");
    }
}
