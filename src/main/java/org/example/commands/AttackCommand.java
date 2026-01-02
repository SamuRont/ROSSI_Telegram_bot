package org.example.commands;

import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class AttackCommand extends StartCommand {
    public void run(TelegramClient c, Update u) {
        String a = u.getMessage().getText().replace("/attack","").trim();
        send(c, u, "Carte con attacco " + a + ": " +
                PokemonApiClient.getCards("attacks.name:" + a).size());
    }
}
