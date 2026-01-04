package org.example.commands;

import org.example.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class HelpCommand extends StartCommand {

    public void run(TelegramClient c, Update u) {
        DatabaseManager.incrementCommandUsage("/help");

        String helpText = """
            📖 COMANDI DISPONIBILI:
            
            🔍 RICERCA
            /look <nome> - Visualizza una carta
            /rarity <rarità> - Cerca per rarità
            /attack <attacco> - Cerca per attacco
            /type <tipo> - Analizza un tipo
            /setinfo <set> - Info su un set
            
            ⚔️ COMPARAZIONE
            /compare <carta1>;<carta2> - Confronta due carte
            /stats <nome> - Statistiche dettagliate
            
            💼 DECK BUILDING
            /deck <tipo> - Suggerimenti per deck
            /recommend <nome> - Carte correlate
            /savedeck <nome>;<carta1>,<carta2>,... - Salva deck
            /mydecks - Visualizza i tuoi deck
            
            📊 STATISTICHE
            /history - Le tue ultime ricerche
            /globalstats - Statistiche globali del bot
            
            💡 Esempio: /look Charizard
            """;

        send(c, u, helpText);
    }
}