package org.example.commands;

import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class LookCommand {
    public void run(TelegramClient c, Update u) {
        String name = u.getMessage().getText().replace("/look","").trim();
        JsonObject card = PokemonApiClient.getFirstCard("name:" + name);
        if (card == null) return;

        try {
            c.execute(SendPhoto.builder()
                    .chatId(u.getMessage().getChatId())
                    .photo(card.getAsJsonObject("images").get("large").getAsString())
                    .caption(card.get("name").getAsString() + " HP: " + card.get("hp"))
                    .build());
        } catch (Exception ignored) {}
    }
}
