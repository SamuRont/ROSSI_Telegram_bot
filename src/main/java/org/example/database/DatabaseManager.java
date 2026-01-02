package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:bot.db";

    public static void init() {
        try (Connection c = DriverManager.getConnection(URL);
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
