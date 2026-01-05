package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MetaCommand {

    public void run(TelegramClient client, Update update) {
        String[] parts = update.getMessage().getText().split(" ", 2);

        if (parts.length < 2) {
            send(client, update, "❌ Uso corretto: /meta <tipo>");
            return;
        }

        String type = parts[1].toLowerCase();

        JsonObject data;
        try {
            data = PokemonApiClient.getType(type);
        } catch (Exception e) {
            send(client, update, "❌ Tipo non valido: " + type);
            return;
        }

        JsonArray pokemon = data.getAsJsonArray("pokemon");
        int count = pokemon.size();

        StringBuilder out = new StringBuilder();
        out.append("📊 META ANALYSIS — ").append(type.toUpperCase()).append("\n\n");
        out.append("Numero Pokémon di questo tipo: ").append(count).append("\n");

        out.append("\nEsempi Pokémon:\n");
        for (int i = 0; i < Math.min(5, count); i++) {
            out.append("- ")
                    .append(pokemon.get(i).getAsJsonObject()
                            .getAsJsonObject("pokemon")
                            .get("name").getAsString())
                    .append("\n");
        }

        out.append("\n🧠 Conclusione:\n");
        out.append("Il tipo ").append(type)
                .append(" ha una buona varietà di Pokémon utilizzabili.");

        send(client, update, out.toString());
    }

    private void send(TelegramClient client, Update update, String text) {
        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(text)
                    .build());
        } catch (Exception ignored) {}
    }
}
