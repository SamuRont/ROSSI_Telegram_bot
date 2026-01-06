package org.example.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MetaCommand extends StartCommand {

    public void run(TelegramClient client, Update update) {
        String input = update.getMessage().getText().replace("/meta", "").trim().toLowerCase();

        if (input.isEmpty()) {
            send(client, update, """
                ⚠️ Uso: /meta <tipo>
                
                Tipi disponibili:
                fire, water, grass, electric, psychic, 
                fighting, dark, steel, dragon, fairy, normal
                
                Esempio: /meta fire
                """);
            return;
        }

        DatabaseManager.incrementCommandUsage("/meta");

        try {
            System.out.println("🔍 Meta analysis for type: " + input);

            JsonArray pokemon = PokemonApiClient.getCards("types:" + input);

            if (pokemon.size() == 0) {
                send(client, update, "❌ Tipo non valido o nessun Pokemon trovato: " + input);
                return;
            }

            int count = pokemon.size();

            StringBuilder out = new StringBuilder();
            out.append("📊 META ANALYSIS — ").append(input.toUpperCase()).append("\n\n");
            out.append("🎴 Numero Pokémon di questo tipo: ").append(count).append("\n\n");

            out.append("🏆 TOP 5 PER HP:\n");

            JsonArray sortedPokemon = new JsonArray();
            for (int i = 0; i < pokemon.size(); i++) {
                sortedPokemon.add(pokemon.get(i));
            }

            for (int i = 0; i < sortedPokemon.size() - 1; i++) {
                for (int j = 0; j < sortedPokemon.size() - i - 1; j++) {
                    JsonObject curr = sortedPokemon.get(j).getAsJsonObject();
                    JsonObject next = sortedPokemon.get(j + 1).getAsJsonObject();

                    int hpCurr = curr.has("hp") ? Integer.parseInt(curr.get("hp").getAsString()) : 0;
                    int hpNext = next.has("hp") ? Integer.parseInt(next.get("hp").getAsString()) : 0;

                    if (hpCurr < hpNext) {
                        JsonObject temp = sortedPokemon.get(j).getAsJsonObject();
                        sortedPokemon.set(j, sortedPokemon.get(j + 1));
                        sortedPokemon.set(j + 1, temp);
                    }
                }
            }

            for (int i = 0; i < Math.min(5, sortedPokemon.size()); i++) {
                JsonObject p = sortedPokemon.get(i).getAsJsonObject();
                String name = p.get("name").getAsString();
                String hp = p.has("hp") ? p.get("hp").getAsString() : "N/A";
                out.append((i + 1)).append(". ").append(name).append(" (HP: ").append(hp).append(")\n");
            }

            out.append("\n🧠 ANALISI:\n");

            if (count >= 15) {
                out.append("✅ Tipo molto popolare con alta varietà\n");
            } else if (count >= 8) {
                out.append("✅ Tipo bilanciato con buone opzioni\n");
            } else {
                out.append("⚠️ Tipo con poche opzioni disponibili\n");
            }

            out.append("💡 Usa /deck ").append(input).append(" per suggerimenti!");

            send(client, update, out.toString());

        } catch (Exception e) {
            send(client, update, "❌ Errore nell'analisi del tipo: " + input);
            System.err.println("Errore MetaCommand: " + e.getMessage());
        }
    }
}