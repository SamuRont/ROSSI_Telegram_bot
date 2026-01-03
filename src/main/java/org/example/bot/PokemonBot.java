package org.example.bot;

import org.example.commands.*;
import org.example.database.DatabaseManager;
import org.example.utils.ConfigLoader;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class PokemonBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;

    public PokemonBot() {
        DatabaseManager.init();
        client = new OkHttpTelegramClient(ConfigLoader.get("BOT_TOKEN"));
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String t = update.getMessage().getText();

        if (t.startsWith("/start")) new StartCommand().run(client, update);
        else if (t.startsWith("/help")) new HelpCommand().run(client, update);
        else if (t.startsWith("/look")) new LookCommand().run(client, update);
        else if (t.startsWith("/rarity")) new RarityCommand().run(client, update);
        else if (t.startsWith("/attack")) new AttackCommand().run(client, update);
        else if (t.startsWith("/setinfo")) new SetInfoCommand().run(client, update);
        else if (t.startsWith("/compare")) new CompareCommand().run(client, update);
        else if (t.startsWith("/deck")) new DeckCommand().run(client, update);
        else if (t.startsWith("/stats")) new StatsCommand().run(client, update);
        else if (t.startsWith("/type")) new TypeCommand().run(client, update);
        else if (t.startsWith("/history")) new HistoryCommand().run(client, update);
        else if (t.startsWith("/recommend")) new RecommendCommand().run(client, update);

    }
}
