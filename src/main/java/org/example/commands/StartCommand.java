package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class StartCommand {

    public void run(TelegramClient c, Update u) {
        // Registra utente nel database
        User user = u.getMessage().getFrom();
        DatabaseManager.registerUser(
                user.getId(),
                user.getUserName(),
                user.getFirstName()
        );

        DatabaseManager.incrementCommandUsage("/start");

        String welcomeMsg = """
            🎴 Benvenuto nel Pokémon TCG Bot!
            
            Esplora il mondo delle carte Pokémon con comandi avanzati.
            Usa /help per vedere tutti i comandi disponibili.
            
            🔥 Inizia subito con /look Pikachu
            """;

        send(c, u, welcomeMsg);
    }

    protected void send(TelegramClient c, Update u, String t) {
        try {
            c.execute(SendMessage.builder()
                    .chatId(u.getMessage().getChatId())
                    .text(t)
                    .build());
        } catch (Exception e) {
            System.err.println("⚠️ Errore invio messaggio: " + e.getMessage());
        }
    }
}