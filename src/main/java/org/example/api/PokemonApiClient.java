package org.example.api;

import com.google.gson.*;
import java.io.InputStreamReader;
import java.net.URL;

public class PokemonApiClient {

    private static JsonObject request(String q) throws Exception {
        String url = "https://api.pokemontcg.io/v2/cards?q=" + q;
        InputStreamReader reader = new InputStreamReader(new URL(url).openStream());
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    public static JsonObject getFirstCard(String query) {
        try {
            JsonArray data = request(query).getAsJsonArray("data");
            return data.size() > 0 ? data.get(0).getAsJsonObject() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonArray getCards(String query) {
        try {
            return request(query).getAsJsonArray("data");
        } catch (Exception e) {
            return new JsonArray();
        }
    }
}
