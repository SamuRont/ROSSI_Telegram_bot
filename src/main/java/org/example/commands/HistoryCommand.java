package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HistoryCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        long telegramId = update.getMessage().getFrom().getId();

        DatabaseManager.incrementCommandUsage("/history");

        StringBuilder out = new StringBuilder("📜 TUA CRONOLOGIA RICERCHE:\n\n");

        try (Connection c = DatabaseManager.connect();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT card, searched_at FROM searched_cards " +
                             "WHERE telegram_id = ? " +
                             "ORDER BY searched_at DESC LIMIT 15"
             )) {

            ps.setString(1, String.valueOf(telegramId));
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                out.append(count).append(". ")
                        .append(rs.getString("card"))
                        .append("\n");
            }

            if (count == 0) {
                out.append("Nessuna ricerca effettuata ancora.\n");
                out.append("\n💡 Prova con /look Pikachu");
            } else {
                out.append("\n📊 Totale ricerche: ").append(count);
            }

        } catch (Exception e) {
            out.append("❌ Errore nel recupero della cronologia.");
            System.err.println("Errore HistoryCommand: " + e.getMessage());
        }

        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(out.toString())
                    .build());
        } catch (Exception e) {
            System.err.println("Errore invio messaggio: " + e.getMessage());
        }
    }
}