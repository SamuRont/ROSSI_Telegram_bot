package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDecksCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        long telegramId = update.getMessage().getFrom().getId();

        DatabaseManager.incrementCommandUsage("/mydecks");

        StringBuilder out = new StringBuilder("💼 I TUOI DECK SALVATI:\n\n");

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT deck_name, card_name FROM user_decks " +
                             "WHERE telegram_id = ? ORDER BY deck_name, added_at"
             )) {

            ps.setString(1, String.valueOf(telegramId));
            ResultSet rs = ps.executeQuery();

            // Raggruppa carte per deck
            Map<String, List<String>> decks = new HashMap<>();

            while (rs.next()) {
                String deckName = rs.getString("deck_name");
                String cardName = rs.getString("card_name");

                decks.computeIfAbsent(deckName, k -> new ArrayList<>()).add(cardName);
            }

            if (decks.isEmpty()) {
                out.append("Nessun deck salvato ancora.\n\n");
                out.append("💡 Usa /savedeck per creare il tuo primo deck!");
            } else {
                int deckNum = 1;
                for (Map.Entry<String, List<String>> entry : decks.entrySet()) {
                    out.append("🎴 Deck ").append(deckNum++).append(": ")
                            .append(entry.getKey()).append("\n");
                    out.append("   Carte (").append(entry.getValue().size()).append("):\n");

                    for (String card : entry.getValue()) {
                        out.append("   • ").append(card).append("\n");
                    }
                    out.append("\n");
                }

                out.append("📊 Totale deck: ").append(decks.size());
            }

        } catch (Exception e) {
            out.append("❌ Errore nel recupero dei deck");
            System.err.println("Errore MyDecksCommand: " + e.getMessage());
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