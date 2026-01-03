package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class RecommendCommand {

    public void run(TelegramClient client, Update update) {
        String name = update.getMessage().getText().replace("/recommend", "").trim();
        if (name.isEmpty()) return;

        JsonObject card = PokemonApiClient.getFirstCard("name:" + name);
        if (card == null || !card.has("types")) return;

        String type = card.getAsJsonArray("types").get(0).getAsString();
        JsonArray related = PokemonApiClient.getCards("types:" + type);

        StringBuilder msg = new StringBuilder();
        msg.append("🔍 Basato su ").append(card.get("name").getAsString()).append("\n");
        msg.append("⚡ Tipo: ").append(type).append("\n\n");
        msg.append("📌 Carte consigliate:\n");

        for (int i = 0; i < Math.min(3, related.size()); i++) {
            msg.append("- ")
                    .append(related.get(i).getAsJsonObject().get("name").getAsString())
                    .append("\n");
        }

        msg.append("\n🧠 Strategia: deck basato su velocità e sinergia di tipo.");

        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(msg.toString())
                    .build());
        } catch (Exception ignored) {}
    }
}
