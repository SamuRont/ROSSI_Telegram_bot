package org.example.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class HelpCommand extends StartCommand {
    public void run(TelegramClient c, Update u) {
        send(c, u, """
        Comandi:
        /look <nome>
        /rarity <rarità>
        /attack <attacco>
        /setinfo <set>
        /compare <c1>;<c2>
        /deck <tipo>
        """);
    }
}
