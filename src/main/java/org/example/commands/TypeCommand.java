package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TypeCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        String type = update.getMessage().getText().replace("/type", "").trim();

        if (type.isEmpty()) {
            send(client, update, """
                ⚠️ Uso: /type <tipo>
                
                Tipi: Fire, Water, Grass, Electric, Psychic, 
                Fighting, Darkness, Metal, Dragon, Fairy, Colorless
                
                Esempio: /type Electric
                """);
            return;
        }

        DatabaseManager.incrementCommandUsage("/type");
        DatabaseManager.saveTypeSearch(type, update.getMessage().getFrom().getId());

        JsonArray cards = PokemonApiClient.getCards("types:" + type);

        if (cards.size() == 0) {
            send(client, update, "❌ Nessuna carta trovata per tipo: " + type);
            return;
        }

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
        msg.append("⚡ ANALISI TIPO: ").append(type).append("\n\n");
        msg.append("📊 Carte totali: ").append(cards.size()).append("\n\n");
        msg.append("🏆 TOP 5 PER HP:\n");

        for (int i = 0; i < Math.min(5, list.size()); i++) {
            JsonObject c = list.get(i);
            msg.append(i + 1).append(". ")
                    .append(c.get("name").getAsString())
                    .append(" (HP ")
                    .append(c.has("hp") ? c.get("hp").getAsString() : "N/A")
                    .append(")\n");
        }

        msg.append("\n💡 Usa /deck ").append(type).append(" per suggerimenti deck!");

        try {
            client.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(msg.toString())
                    .build());
        } catch (Exception e) {
            System.err.println("Errore TypeCommand: " + e.getMessage());
        }
    }
}