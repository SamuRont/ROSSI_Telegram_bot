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

        String text = update.getMessage().getText();
        System.out.println("📩 Messaggio ricevuto!");
        System.out.println("📝 Testo: " + text);

        // Routing comandi
        if (text.startsWith("/start")) new StartCommand().run(client, update);
        else if (text.startsWith("/help")) new HelpCommand().run(client, update);
        else if (text.startsWith("/look")) new LookCommand().run(client, update);
        else if (text.startsWith("/attack")) new AttackCommand().run(client, update);
        else if (text.startsWith("/compare")) new CompareCommand().run(client, update);
        else if (text.startsWith("/deck")) new DeckCommand().run(client, update);
        else if (text.startsWith("/stats")) new StatsCommand().run(client, update);
        else if (text.startsWith("/type")) new TypeCommand().run(client, update);
        else if (text.startsWith("/history")) new HistoryCommand().run(client, update);
        else if (text.startsWith("/recommend")) new RecommendCommand().run(client, update);
        else if (text.startsWith("/globalstats")) new GlobalStatsCommand().run(client, update);
        else if (text.startsWith("/savedeck")) new SaveDeckCommand().run(client, update);
        else if (text.startsWith("/mydecks")) new MyDecksCommand().run(client, update);
        else if (text.startsWith("/position")) new PositionCommand().run(client, update);
        else if (text.startsWith("/meta")) new MetaCommand().run(client, update);

    }
}