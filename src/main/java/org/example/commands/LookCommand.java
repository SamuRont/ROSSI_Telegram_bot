package org.example.commands;

import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class LookCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        String name = u.getMessage().getText().replace("/look", "").trim();

        if (name.isEmpty()) {
            send(c, u, "⚠️ Uso: /look <nome carta>\nEsempio: /look Pikachu");
            return;
        }

        DatabaseManager.incrementCommandUsage("/look");
        DatabaseManager.saveCardSearch(name, u.getMessage().getFrom().getId());

        JsonObject card = PokemonApiClient.getFirstCard("name:" + name);

        if (card == null) {
            send(c, u, "❌ Carta '" + name + "' non trovata.\n💡 Prova con un nome diverso.");
            return;
        }

        try {
            String imageUrl = card.getAsJsonObject("images").get("large").getAsString();
            String cardName = card.get("name").getAsString();
            String hp = card.has("hp") ? card.get("hp").getAsString() : "N/A";

            String caption = String.format("🎴 %s\n❤️ HP: %s", cardName, hp);

            c.execute(SendPhoto.builder()
                    .chatId(u.getMessage().getChatId())
                    .photo(new InputFile(imageUrl))
                    .caption(caption)
                    .build());

        } catch (Exception e) {
            send(c, u, "❌ Errore nel caricamento dell'immagine");
            System.err.println("Errore LookCommand: " + e.getMessage());
        }
    }
}