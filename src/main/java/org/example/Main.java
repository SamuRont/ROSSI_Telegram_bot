package org.example;

import org.example.bot.PokemonBot;
import org.example.utils.ConfigLoader;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        String botToken = ConfigLoader.get("BOT_TOKEN");

        try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
            app.registerBot(botToken, new PokemonBot());  // ⚠️ IMPORTANTE: due parametri!
            System.out.println("✅ Bot avviato correttamente!");
            System.out.println("📊 Database inizializzato");
            System.out.println("🤖 In attesa di messaggi...");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("❌ Errore nell'avvio del bot:");
            e.printStackTrace();
        }
    }
}