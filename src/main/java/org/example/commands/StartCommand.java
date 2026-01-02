package org.example.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class StartCommand {
    public void run(TelegramClient c, Update u) {
        send(c, u, "Benvenuto! Usa /help");
    }
    protected void send(TelegramClient c, Update u, String t) {
        try {
            c.execute(SendMessage.builder().chatId(u.getMessage().getChatId()).text(t).build());
        } catch (Exception ignored) {}
    }
}
