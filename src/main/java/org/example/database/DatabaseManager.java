package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:bot.db";

    // ✅ Metodo per ottenere la connessione (USATO DA TUTTI I COMANDI)
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException("Errore connessione SQLite", e);
        }
    }

    // ✅ Inizializzazione DB e tabelle
    public static void init() {
        try (Connection c = connect();
             Statement s = c.createStatement()) {

            s.execute("""
                CREATE TABLE IF NOT EXISTS users(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    telegram_id TEXT,
                    username TEXT,
                    first_name TEXT
                );
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS command_usage(
                    command TEXT PRIMARY KEY,
                    count INTEGER
                );
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS searched_cards(
                    card TEXT
                );
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS deck_history(
                    type TEXT
                );
            """);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
