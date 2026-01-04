package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GlobalStatsCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        DatabaseManager.incrementCommandUsage("/globalstats");

        StringBuilder sb = new StringBuilder("📊 STATISTICHE GLOBALI BOT\n\n");

        try (Connection conn = DatabaseManager.connect();
             Statement s = conn.createStatement()) {

            // Totale utenti
            ResultSet rsUsers = s.executeQuery("SELECT COUNT(*) as total FROM users");
            if (rsUsers.next()) {
                sb.append("👥 Utenti registrati: ").append(rsUsers.getInt("total")).append("\n\n");
            }

            // Carte più cercate
            ResultSet rs1 = s.executeQuery(
                    "SELECT card, COUNT(*) as cnt FROM searched_cards " +
                            "GROUP BY card ORDER BY cnt DESC LIMIT 5"
            );
            sb.append("🔥 TOP 5 CARTE PIÙ CERCATE:\n");
            int pos = 1;
            while (rs1.next()) {
                sb.append(pos++).append(". ")
                        .append(rs1.getString("card"))
                        .append(" (").append(rs1.getInt("cnt")).append(" ricerche)\n");
            }

            // Tipi più cercati
            ResultSet rs2 = s.executeQuery(
                    "SELECT type, COUNT(*) as cnt FROM type_searches " +
                            "GROUP BY type ORDER BY cnt DESC LIMIT 5"
            );
            sb.append("\n⚡ TIPI PIÙ POPOLARI:\n");
            pos = 1;
            while (rs2.next()) {
                sb.append(pos++).append(". ")
                        .append(rs2.getString("type"))
                        .append(" (").append(rs2.getInt("cnt")).append("x)\n");
            }

            // Comandi più usati
            ResultSet rs3 = s.executeQuery(
                    "SELECT command, count FROM command_usage ORDER BY count DESC LIMIT 5"
            );
            sb.append("\n📈 COMANDI PIÙ USATI:\n");
            pos = 1;
            while (rs3.next()) {
                sb.append(pos++).append(". ")
                        .append(rs3.getString("command"))
                        .append(": ").append(rs3.getInt("count")).append(" volte\n");
            }

            // Totale ricerche
            ResultSet rsTotal = s.executeQuery("SELECT COUNT(*) as total FROM searched_cards");
            if (rsTotal.next()) {
                sb.append("\n🎯 Ricerche totali: ").append(rsTotal.getInt("total"));
            }

        } catch (Exception e) {
            sb.append("\n❌ Errore nel recupero delle statistiche");
            System.err.println("Errore GlobalStatsCommand: " + e.getMessage());
        }

        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(sb.toString())
                    .build());
        } catch (Exception e) {
            System.err.println("Errore invio messaggio: " + e.getMessage());
        }
    }
}