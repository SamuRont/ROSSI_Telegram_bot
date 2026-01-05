package org.example.database;

import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:bot.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException("❌ Errore connessione SQLite", e);
        }
    }

    public static void init() {
        try (Connection c = connect(); Statement s = c.createStatement()) {

            // Tabella utenti
            s.execute("""
                CREATE TABLE IF NOT EXISTS users(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    telegram_id TEXT UNIQUE,
                    username TEXT,
                    first_name TEXT,
                    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
            """);

            // Tabella statistiche comandi
            s.execute("""
                CREATE TABLE IF NOT EXISTS command_usage(
                    command TEXT PRIMARY KEY,
                    count INTEGER DEFAULT 0
                );
            """);

            // Tabella carte cercate
            s.execute("""
                CREATE TABLE IF NOT EXISTS searched_cards(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    card TEXT NOT NULL,
                    telegram_id TEXT,
                    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
                );
            """);

            // Tabella cronologia tipi cercati
            s.execute("""
                CREATE TABLE IF NOT EXISTS type_searches(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL,
                    telegram_id TEXT,
                    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
                );
            """);

            // Tabella deck salvati dagli utenti
            s.execute("""
                CREATE TABLE IF NOT EXISTS user_decks(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    telegram_id TEXT,
                    deck_name TEXT,
                    card_name TEXT,
                    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
                );
            """);

            // Tabella confronti effettuati
            s.execute("""
                CREATE TABLE IF NOT EXISTS comparisons(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    telegram_id TEXT,
                    card1 TEXT,
                    card2 TEXT,
                    compared_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
                );
            """);

            System.out.println("✅ Database inizializzato con successo");

        } catch (Exception e) {
            System.err.println("❌ Errore nell'inizializzazione del database:");
            e.printStackTrace();
        }
    }

    // Incrementa contatore
    public static void incrementCommandUsage(String command) {
        String sql = """
            INSERT INTO command_usage(command, count) VALUES(?, 1)
            ON CONFLICT(command) DO UPDATE SET count = count + 1
        """;

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, command);
            ps.execute();
        } catch (Exception e) {
            System.err.println("⚠️ Errore incremento comando: " + e.getMessage());
        }
    }

    // Registra utente
    public static void registerUser(long telegramId, String username, String firstName) {
        String sql = "INSERT OR IGNORE INTO users(telegram_id, username, first_name) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(telegramId));
            ps.setString(2, username);
            ps.setString(3, firstName);
            ps.execute();
        } catch (Exception e) {
            System.err.println("⚠️ Errore registrazione utente: " + e.getMessage());
        }
    }

    //  Salva ricerca carta
    public static void saveCardSearch(String cardName, long telegramId) {
        String sql = "INSERT INTO searched_cards(card, telegram_id) VALUES(?, ?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cardName);
            ps.setString(2, String.valueOf(telegramId));
            ps.execute();
        } catch (Exception e) {
            System.err.println("⚠️ Errore salvataggio ricerca: " + e.getMessage());
        }
    }

    // Salva ricerca tipo
    public static void saveTypeSearch(String type, long telegramId) {
        String sql = "INSERT INTO type_searches(type, telegram_id) VALUES(?, ?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setString(2, String.valueOf(telegramId));
            ps.execute();
        } catch (Exception e) {
            System.err.println("⚠️ Errore salvataggio tipo: " + e.getMessage());
        }
    }

    // Salva confronto
    public static void saveComparison(String card1, String card2, long telegramId) {
        String sql = "INSERT INTO comparisons(telegram_id, card1, card2) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(telegramId));
            ps.setString(2, card1);
            ps.setString(3, card2);
            ps.execute();
        } catch (Exception e) {
            System.err.println("⚠️ Errore salvataggio confronto: " + e.getMessage());
        }
    }
}