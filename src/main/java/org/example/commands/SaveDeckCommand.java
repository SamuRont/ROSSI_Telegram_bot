package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SaveDeckCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        String input = update.getMessage().getText().replace("/savedeck", "").trim();

        if (!input.contains(";")) {
            send(client, update, """
                ⚠️ Uso: /savedeck <nome_deck>;<carta1>,<carta2>,<carta3>
                
                Esempio:
                /savedeck MioDeckFuoco;Charizard,Arcanine,Moltres
                
                💡 Separa il nome del deck dalle carte con ;
                💡 Separa le carte con virgole
                """);
            return;
        }

        String[] parts = input.split(";");
        if (parts.length != 2) {
            send(client, update, "❌ Formato non valido. Usa: /savedeck nome;carta1,carta2");
            return;
        }

        String deckName = parts[0].trim();
        String[] cards = parts[1].split(",");

        if (cards.length == 0) {
            send(client, update, "❌ Devi specificare almeno una carta!");
            return;
        }

        DatabaseManager.incrementCommandUsage("/savedeck");

        long telegramId = update.getMessage().getFrom().getId();

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO user_decks(telegram_id, deck_name, card_name) VALUES(?, ?, ?)"
             )) {

            for (String card : cards) {
                ps.setString(1, String.valueOf(telegramId));
                ps.setString(2, deckName);
                ps.setString(3, card.trim());
                ps.addBatch();
            }

            ps.executeBatch();

            String response = String.format("""
                ✅ Deck '%s' salvato con successo!
                
                🎴 Carte aggiunte: %d
                
                💡 Usa /mydecks per vedere tutti i tuoi deck
                """, deckName, cards.length);

            send(client, update, response);

        } catch (Exception e) {
            send(client, update, "❌ Errore nel salvataggio del deck");
            System.err.println("Errore SaveDeckCommand: " + e.getMessage());
        }
    }
}