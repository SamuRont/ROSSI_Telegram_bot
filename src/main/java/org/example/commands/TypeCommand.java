package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TypeCommand {

    public void run(TelegramClient client, Update update) {
        String type = update.getMessage().getText().replace("/type", "").trim();
        if (type.isEmpty()) return;

        JsonArray cards = PokemonApiClient.getCards("types:" + type);
        List<JsonObject> list = new ArrayList<>();

        for (var c : cards) list.add(c.getAsJsonObject());

        list.sort(Comparator.comparingInt((JsonObject c) -> {
            if (c.has("hp")) {
                try {
                    return Integer.parseInt(c.get("hp").getAsString());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
            return 0;
        }).reversed());


        StringBuilder msg = new StringBuilder();
        msg.append("⚡ Tipo: ").append(type).append("\n");
        msg.append("Carte totali: ").append(cards.size()).append("\n\n");
        msg.append("🏆 Top Pokémon:\n");

        for (int i = 0; i < Math.min(5, list.size()); i++) {
            JsonObject c = list.get(i);
            msg.append(i + 1).append(". ")
                    .append(c.get("name").getAsString())
                    .append(" (HP ")
                    .append(c.has("hp") ? c.get("hp").getAsString() : "N/A")
                    .append(")\n");
        }

        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(msg.toString())
                    .build());
        } catch (Exception ignored) {}
    }
}
