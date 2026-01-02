package org.example.commands;

import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class RarityCommand extends StartCommand {
    public void run(TelegramClient c, Update u) {
        String r = u.getMessage().getText().replace("/rarity","").trim();
        send(c, u, "Carte trovate: " + PokemonApiClient.getCards("rarity:" + r).size());
    }
}
