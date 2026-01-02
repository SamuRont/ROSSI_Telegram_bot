package org.example.commands;

import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class SetInfoCommand extends StartCommand {
    public void run(TelegramClient c, Update u) {
        String s = u.getMessage().getText().replace("/setinfo","").trim();
        send(c, u, "Carte nel set: " + PokemonApiClient.getCards("set.id:" + s).size());
    }
}
