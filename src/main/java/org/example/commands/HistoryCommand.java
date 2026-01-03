package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HistoryCommand {

    public void run(TelegramClient client, Update update) {
        StringBuilder out = new StringBuilder("📜 Cronologia ricerche:\n");

        try (Connection c = DatabaseManager.connect();
             Statement s = c.createStatement()) {

            ResultSet rs = s.executeQuery(
                    "SELECT card FROM searched_cards ORDER BY ROWID DESC LIMIT 10"
            );

            while (rs.next()) {
                out.append("- ").append(rs.getString("card")).append("\n");
            }

        } catch (Exception e) {
            out.append("Nessun dato disponibile.");
        }

        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(out.toString())
                    .build());
        } catch (Exception ignored) {}
    }
}
