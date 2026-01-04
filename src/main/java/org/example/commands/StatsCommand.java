package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class StatsCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        String name = update.getMessage().getText().replace("/stats", "").trim();

        if (name.isEmpty()) {
            send(client, update, "⚠️ Uso: /stats <nome carta>\nEsempio: /stats Charizard");
            return;
        }

        DatabaseManager.incrementCommandUsage("/stats");
        DatabaseManager.saveCardSearch(name, update.getMessage().getFrom().getId());

        JsonObject card = PokemonApiClient.getFirstCard("name:" + name);

        if (card == null) {
            send(client, update, "❌ Carta non trovata: " + name);
            return;
        }

        StringBuilder text = new StringBuilder();
        text.append("📊 STATISTICHE DETTAGLIATE\n\n");
        text.append("🃏 ").append(card.get("name").getAsString()).append("\n");
        text.append("❤️ HP: ").append(card.has("hp") ? card.get("hp").getAsString() : "N/A").append("\n");

        if (card.has("types")) {
            text.append("⚡ Tipo: ");
            for (var t : card.getAsJsonArray("types")) {
                text.append(t.getAsString()).append(" ");
            }
            text.append("\n");
        }

        if (card.has("rarity")) {
            text.append("⭐ Rarità: ").append(card.get("rarity").getAsString()).append("\n");
        }

        if (card.has("set")) {
            text.append("📦 Set: ")
                    .append(card.getAsJsonObject("set").get("name").getAsString())
                    .append("\n");
        }

        if (card.has("attacks")) {
            text.append("\n💥 ATTACCHI:\n");
            JsonArray attacks = card.getAsJsonArray("attacks");
            for (var a : attacks) {
                JsonObject atk = a.getAsJsonObject();
                text.append("• ")
                        .append(atk.get("name").getAsString())
                        .append(" (")
                        .append(atk.has("damage") ? atk.get("damage").getAsString() : "0")
                        .append(")\n");
            }
        }

        try {
            InputFile photo = new InputFile(
                    card.getAsJsonObject("images").get("large").getAsString()
            );

            client.execute(
                    SendPhoto.builder()
                            .chatId(update.getMessage().getChatId())
                            .photo(photo)
                            .caption(text.toString())
                            .build()
            );
        } catch (Exception e) {
            send(client, update, text.toString());
        }
    }
}