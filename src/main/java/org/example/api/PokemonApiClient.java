package org.example.api;

import com.google.gson.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PokemonApiClient {

    private static final String BASE_URL = "https://pokeapi.co/api/v2";

    // Cache per evitare chiamate ripetute
    private static final Map<String, JsonObject> cache = new HashMap<>();

    private static JsonObject request(String endpoint) throws Exception {
        String url = BASE_URL + endpoint;
        System.out.println("🌐 API Request: " + url);
        InputStreamReader reader = new InputStreamReader(new URL(url).openStream());
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    public static JsonObject getFirstCard(String query) {
        try {
            String name = query.replace("name:", "").trim().toLowerCase();

            if (name.isEmpty()) {
                return null;
            }

            if (cache.containsKey(name)) {
                System.out.println("💾 Using cached data for: " + name);
                return cache.get(name);
            }

            System.out.println("🔍 Searching for: " + name);
            JsonObject pokemon = request("/pokemon/" + name);
            JsonObject card = buildCard(pokemon, name);

            cache.put(name, card);

            System.out.println("✅ Pokemon found: " + name);
            return card;

        } catch (Exception e) {
            System.err.println("⚠️ Pokemon non trovato: " + query);
            return null;
        }
    }

    public static JsonArray getCards(String query) {
        try {
            JsonArray cards = new JsonArray();

            //Usa l'API per prendere TUTTI i Pokemon di un tipo
            if (query.contains("types:")) {
                String type = query.replace("types:", "").trim().toLowerCase();
                System.out.println("🔍 Fetching ALL pokemon of type: " + type);

                JsonObject typeData = request("/type/" + type);
                JsonArray pokemonArray = typeData.getAsJsonArray("pokemon");

                System.out.println("📊 Found " + pokemonArray.size() + " pokemon of type " + type);

                int limit = Math.min(100, pokemonArray.size());

                for (int i = 0; i < limit; i++) {
                    String pokemonName = pokemonArray.get(i).getAsJsonObject()
                            .getAsJsonObject("pokemon")
                            .get("name").getAsString();

                    JsonObject card = getFirstCard("name:" + pokemonName);
                    if (card != null) {
                        cards.add(card);
                    }
                }

                System.out.println("✅ Loaded " + cards.size() + " cards");
                return cards;
            }

            if (query.contains("rarity:")) {
                String rarity = query.replace("rarity:", "").trim().toLowerCase();
                System.err.println("⚠️ Rarity search not fully supported by PokeAPI");
                return cards;
            }

            if (query.contains("attacks.name:")) {
                String moveName = query.replace("attacks.name:", "").replace("\"", "").trim().toLowerCase();
                System.out.println("🔍 Fetching pokemon with move: " + moveName);

                try {
                    JsonObject moveData = request("/move/" + moveName);
                    JsonArray learnedBy = moveData.getAsJsonArray("learned_by_pokemon");

                    System.out.println("📊 Found " + learnedBy.size() + " pokemon with this move");

                    int limit = Math.min(100, learnedBy.size());

                    for (int i = 0; i < limit; i++) {
                        String pokemonName = learnedBy.get(i).getAsJsonObject()
                                .get("name").getAsString();

                        JsonObject card = getFirstCard("name:" + pokemonName);
                        if (card != null) {
                            cards.add(card);
                        }
                    }

                    System.out.println("✅ Loaded " + cards.size() + " cards");
                    return cards;
                } catch (Exception e) {
                    System.err.println("⚠️ Move not found: " + moveName);
                    return cards;
                }
            }

            if (query.contains("set.id:")) {
                System.err.println("⚠️ Set search not supported by PokeAPI");
                return cards;
            }

            System.err.println("⚠️ Query type not recognized: " + query);
            return cards;

        } catch (Exception e) {
            System.err.println("⚠️ Errore API per query: " + query);
            e.printStackTrace();
            return new JsonArray();
        }
    }

    private static JsonObject buildCard(JsonObject pokemon, String name) {
        JsonObject card = new JsonObject();
        card.addProperty("name", capitalizeFirstLetter(name));

        // HP
        if (pokemon.has("stats")) {
            JsonArray stats = pokemon.getAsJsonArray("stats");
            if (stats.size() > 0) {
                int hp = stats.get(0).getAsJsonObject().get("base_stat").getAsInt();
                card.addProperty("hp", String.valueOf(hp));
            }
        }

        // Tipi
        if (pokemon.has("types")) {
            JsonArray typesArray = new JsonArray();
            for (var typeObj : pokemon.getAsJsonArray("types")) {
                String typeName = typeObj.getAsJsonObject()
                        .getAsJsonObject("type")
                        .get("name").getAsString();
                typesArray.add(capitalizeFirstLetter(typeName));
            }
            card.add("types", typesArray);
        }

        // Immagine
        card.add("images", getImages(pokemon));

        // Attacchi
        card.add("attacks", getAttacks(pokemon));

        // Rarità e Set
        card.addProperty("rarity", "Common");
        JsonObject set = new JsonObject();
        set.addProperty("name", "PokeAPI Collection");
        card.add("set", set);

        return card;
    }

    // Estrae le immagini
    private static JsonObject getImages(JsonObject pokemon) {
        JsonObject images = new JsonObject();

        if (pokemon.has("sprites")) {
            JsonObject sprites = pokemon.getAsJsonObject("sprites");
            String imageUrl = null;

            if (sprites.has("other")) {
                JsonObject other = sprites.getAsJsonObject("other");
                if (other.has("official-artwork")) {
                    JsonObject artwork = other.getAsJsonObject("official-artwork");
                    if (artwork.has("front_default") && !artwork.get("front_default").isJsonNull()) {
                        imageUrl = artwork.get("front_default").getAsString();
                    }
                }
            }

            if (imageUrl == null && sprites.has("front_default") && !sprites.get("front_default").isJsonNull()) {
                imageUrl = sprites.get("front_default").getAsString();
            }

            images.addProperty("large", imageUrl);
        }

        return images;
    }
    public static JsonObject getType(String type) throws Exception {
        return request("https://pokeapi.co/api/v2/type/" + type);
    }


    // Estrae gli attacchi
    private static JsonArray getAttacks(JsonObject pokemon) {
        JsonArray attacks = new JsonArray();

        if (pokemon.has("moves")) {
            JsonArray moves = pokemon.getAsJsonArray("moves");

            for (int i = 0; i < Math.min(4, moves.size()); i++) {
                JsonObject attack = new JsonObject();
                String moveName = moves.get(i).getAsJsonObject()
                        .getAsJsonObject("move")
                        .get("name").getAsString()
                        .replace("-", " ");
                attack.addProperty("name", capitalizeWords(moveName));
                attack.addProperty("damage", String.valueOf((i + 1) * 20));
                attacks.add(attack);
            }
        }

        return attacks;
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static String capitalizeWords(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(capitalizeFirstLetter(word)).append(" ");
        }
        return result.toString().trim();
    }


}