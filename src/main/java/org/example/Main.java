package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
public class Main {
    public static void main(String[] args) {
        String botToken = "12345:YOUR_TOKEN";
        // Using try-with-resources to allow autoclose to run upon finishing
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new MyAmazingBot(botToken));
            System.out.println("org.example.MyAmazingBot successfully started!");
            // Ensure this prcess wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}