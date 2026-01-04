package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class RecommendCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        String name = update.getMessage().getText().replace("/recommend", "").trim();

        if (name.isEmpty()) {
            send(client, update, "⚠️ Uso: /recommend <nome carta>\nEsempio: /recommend Pikachu");
            return;
        }

        DatabaseManager.incrementCommandUsage("/recommend");

        JsonObject card = PokemonApiClient.getFirstCard("name:" + name);

        if (card == null || !card.has("types")) {
            send(client, update, "❌ Carta non trovata o senza tipo: " + name);
            return;
        }

        String type = card.getAsJsonArray("types").get(0).getAsString();
        JsonArray related = PokemonApiClient.getCards("types:" + type);

        StringBuilder msg = new StringBuilder();
        msg.append("🔍 Basato su: ").append(card.get("name").getAsString()).append("\n");
        msg.append("⚡ Tipo: ").append(type).append("\n\n");
        msg.append("📌 CARTE CONSIGLIATE:\n");

        int count = 0;
        for (int i = 0; i < related.size() && count < 5; i++) {
            JsonObject relCard = related.get(i).getAsJsonObject();
            String relName = relCard.get("name").getAsString();

            // Evita di consigliare la stessa carta
            if (relName.equalsIgnoreCase(card.get("name").getAsString())) continue;

            count++;
            msg.append("• ").append(relName).append("\n");
        }

        msg.append("\n🧠 Strategia: costruisci un deck con sinergia di tipo ").append(type).append("!");

        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(msg.toString())
                    .build());
        } catch (Exception e) {
            System.err.println("Errore RecommendCommand: " + e.getMessage());
        }
    }
}