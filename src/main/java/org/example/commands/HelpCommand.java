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
            /look <nome> - Visualizza un Pokemon
            /rarity <rarità> - Cerca per rarità
            /attack <attacco> - Cerca per attacco
            /type <tipo> - Analizza un tipo
            /meta <tipo> - Analisi statistica del meta game
            
            ⚔️ COMPARAZIONE
            /compare <carta1>;<carta2> - Confronta due Pokemon
            /stats <nome> - Statistiche dettagliate
            
            💼 DECK BUILDING
            /deck <tipo> - Suggerimenti per deck
            /recommend <nome> - Pokemon correlati
            /savedeck <nome>;<carta1>,<carta2>,... - Salva deck
            /mydecks - Visualizza i tuoi deck
            
            📊 STATISTICHE
            /history - Le tue ultime ricerche
            /globalstats - Statistiche globali del bot
            
            📍 POSIZIONI
            /position <luogo> - Mostra posizione (calli/boraso)
            
            💡 Esempio: /look Pikachu
            """;

        send(c, u, helpText);
    }
}
