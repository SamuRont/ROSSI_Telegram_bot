package org.example.commands;

import com.google.gson.JsonObject;
import org.example.api.PokemonApiClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class CompareCommand extends StartCommand {
    public void run(TelegramClient c, Update u) {
        String[] p = u.getMessage().getText().replace("/compare","").split(";");
        JsonObject c1 = PokemonApiClient.getFirstCard("name:" + p[0].trim());
        JsonObject c2 = PokemonApiClient.getFirstCard("name:" + p[1].trim());
        send(c, u, c1.get("name")+" HP:"+c1.get("hp")+"\n"+c2.get("name")+" HP:"+c2.get("hp"));
    }
}
