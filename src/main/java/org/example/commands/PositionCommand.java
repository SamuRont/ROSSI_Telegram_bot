package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class PositionCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        String input = u.getMessage().getText().replace("/position", "").trim().toLowerCase();

        if (input.isEmpty()) {
            send(c, u, """
                ⚠️ Uso: /position <luogo>
                
                Luoghi disponibili:
                • calli - Viale Alessandro Manzoni 38, Vicenza
                • boraso - Piazzola Gualdi 3, Vicenza
                • Iris - Via Giuliano Ziggiotti 2, Arzignano
                
                
                Esempio: /position calli
                """);
            return;
        }

        DatabaseManager.incrementCommandUsage("/position");

        double latitude;
        double longitude;
        String locationName;
        String address;

        switch (input) {
            case "calli" -> {
                latitude = 45.55751437777123;
                longitude = 11.537131583486122;
                locationName = "Calli";
                address = "Viale Alessandro Manzoni 38, Vicenza";
            }
            case "boraso" -> {
                latitude = 45.54494624007369;
                longitude = 11.54876128111555;
                locationName = "Boraso";
                address = "Contrà Mure S. Michele 33, Vicenza";
            }
            case "iris" -> {
                latitude = 45.553278204777186;
                longitude = 11.558680554132424;
                locationName = "Iris";
                address = "Via Giuliano Ziggiotti 2, Arzignano";
            }
            default -> {
                send(c, u, """
                    ❌ Luogo non riconosciuto: %s
                    
                    Luoghi disponibili:
                    • calli
                    • boraso
                    • iris
                    
                    Usa /position <luogo>
                    """.formatted(input));
                return;
            }
        }

        try {
            c.execute(SendMessage.builder()
                    .chatId(u.getMessage().getChatId())
                    .text("📍 " + locationName + "\n📫 " + address)
                    .build());

            c.execute(SendLocation.builder()
                    .chatId(u.getMessage().getChatId())
                    .latitude(latitude)
                    .longitude(longitude)
                    .build());

            System.out.println("✅ Position sent: " + locationName);

        } catch (Exception e) {
            send(c, u, "❌ Errore nell'invio della posizione");
            System.err.println("Errore PositionCommand: " + e.getMessage());
        }
    }
}