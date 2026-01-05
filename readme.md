# ROSSI Telegram Bot 🎮⚡

**Nome Bot**: `PokemonInfoRossi_bot`

Bot Telegram sviluppato in **Java 21** che utilizza **PokéAPI** per fornire informazioni dettagliate sui Pokémon, con funzionalità avanzate di ricerca, analisi e gestione deck.

Il progetto rispetta **tutti i requisiti richiesti dalla consegna**, inclusi:

* Utilizzo API esterna (PokéAPI)
* Database SQLite significativo e ben popolato
* Comandi multipli (16 comandi)
* Struttura modulare
* Configurazione tramite file esterno
* Utilizzo TelegramBots 9.2.0
* Statistiche avanzate
* Gestione deck personalizzati

---

## 🧰 Tecnologie utilizzate

* **Java 21**
* **Maven**
* **TelegramBots 9.2.0** (longpolling + client)
* **SQLite** con 6 tabelle relazionali
* **PokéAPI v2** → [https://pokeapi.co/](https://pokeapi.co/)
* **Gson 2.13.1** per parsing JSON

---

## 📁 Struttura del progetto
```
ROSSI_Telegram_bot/
│
├── src/main/java/org/example/
│   ├── Main.java
│   ├── bot/
│   │   └── PokemonBot.java
│   ├── api/
│   │   └── PokemonApiClient.java
│   ├── commands/
│   │   ├── StartCommand.java
│   │   ├── HelpCommand.java
│   │   ├── LookCommand.java
│   │   ├── RarityCommand.java
│   │   ├── AttackCommand.java
│   │   ├── CompareCommand.java
│   │   ├── DeckCommand.java
│   │   ├── StatsCommand.java
│   │   ├── TypeCommand.java
│   │   ├── MetaCommand.java
│   │   ├── HistoryCommand.java
│   │   ├── RecommendCommand.java
│   │   ├── GlobalStatsCommand.java
│   │   ├── SaveDeckCommand.java
│   │   ├── MyDecksCommand.java
│   │   └── PositionCommand.java
│   ├── database/
│   │   └── DatabaseManager.java
│   └── utils/
│       └── ConfigLoader.java
│
├── config.properties.example
├── .gitignore
├── pom.xml
└── README.md
```




## 🤖 Comandi disponibili

### 🔍 Ricerca Base
| Comando | Descrizione | Esempio |
|---------|-------------|---------|
| `/start` | Messaggio di benvenuto e registrazione utente | `/start` |
| `/help` | Lista completa comandi disponibili | `/help` |
| `/look <nome>` | Info complete + FOTO Pokemon | `/look pikachu` |
| `/rarity <rarità>` | Cerca Pokemon per rarità | `/rarity common` |
| `/attack <nome>` | Trova Pokemon con quell'attacco | `/attack thunderbolt` |

### ⚔️ Analisi Avanzata
| Comando | Descrizione | Esempio |
|---------|-------------|---------|
| `/stats <nome>` | Statistiche dettagliate Pokemon | `/stats charizard` |
| `/type <tipo>` | Analizza tipo e top 5 Pokemon | `/type fire` |
| `/meta <tipo>` | Meta-analysis approfondita di un tipo | `/meta electric` |
| `/compare <nome1>;<nome2>` | Confronto tra due Pokemon | `/compare pikachu;raichu` |

### 💼 Deck Building
| Comando | Descrizione | Esempio |
|---------|-------------|---------|
| `/deck <tipo>` | Suggerimenti per deck | `/deck water` |
| `/recommend <nome>` | Pokemon correlati e sinergie | `/recommend charizard` |
| `/savedeck <nome>;<pokemon>` | Salva deck personalizzato | `/savedeck Fire;charizard,arcanine` |
| `/mydecks` | Visualizza i tuoi deck salvati | `/mydecks` |

### 📊 Statistiche
| Comando | Descrizione | Esempio |
|---------|-------------|---------|
| `/history` | Cronologia delle tue ricerche | `/history` |
| `/globalstats` | Statistiche globali del bot | `/globalstats` |

### 📍 Posizioni
| Comando | Descrizione | Esempio |
|---------|-------------|---------|
| `/position <luogo>` | Mostra posizione su mappa | `/position calli` |

**Luoghi disponibili:**
- `calli` - Viale Alessandro Manzoni 38, Vicenza
- `boraso` - Contrà Mure S. Michele 33, Vicenza

---

## 🗄️ Database

### Schema completo (6 tabelle)

Il database SQLite è **significativo e ben popolato**, viene automaticamente popolato durante l'uso del bot.

#### 1. **users** - Utenti registrati
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    telegram_id TEXT UNIQUE,
    username TEXT,
    first_name TEXT,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 2. **command_usage** - Statistiche comandi
```sql
CREATE TABLE command_usage (
    command TEXT PRIMARY KEY,
    count INTEGER DEFAULT 0
);
```

#### 3. **searched_cards** - Cronologia ricerche Pokemon
```sql
CREATE TABLE searched_cards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    card TEXT NOT NULL,
    telegram_id TEXT,
    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
);
```

#### 4. **type_searches** - Cronologia ricerche tipi
```sql
CREATE TABLE type_searches (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    telegram_id TEXT,
    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
);
```

#### 5. **user_decks** - Deck personalizzati
```sql
CREATE TABLE user_decks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    telegram_id TEXT,
    deck_name TEXT,
    card_name TEXT,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
);
```

#### 6. **comparisons** - Confronti effettuati
```sql
CREATE TABLE comparisons (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    telegram_id TEXT,
    card1 TEXT,
    card2 TEXT,
    compared_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(telegram_id) REFERENCES users(telegram_id)
);
```

### Query statistiche implementate

- **Top 5 Pokemon più cercati globalmente**
- **Top 5 tipi più popolari**
- **Top 5 comandi più usati**
- **Cronologia personale utente (ultime 15 ricerche)**
- **Totale utenti registrati**
- **Totale ricerche effettuate**
- **Deck salvati per utente**

---

## 📸 Esempi Output

### `/look pikachu`
```
🎴 Pikachu
❤️ HP: 35
```
*(accompagnato da immagine ufficiale del Pokemon)*

### `/stats charizard`
```
📊 STATISTICHE DETTAGLIATE

🃏 Charizard
❤️ HP: 78
⚡ Tipo: Fire Flying
⭐ Rarità: Common
📦 Set: PokeAPI Collection

💥 ATTACCHI:
- Mega punch (20)
- Fire punch (40)
- Thunder punch (60)
- Mega kick (80)
```
*(con immagine)*

### `/meta fire`
```
📊 META ANALYSIS — FIRE

🎴 Numero Pokémon di questo tipo: 20

🏆 TOP 5 PER HP:
1. Charizard (HP: 78)
2. Arcanine (HP: 90)
3. Moltres (HP: 90)
4. Rapidash (HP: 65)
5. Ninetales (HP: 73)

🧠 ANALISI:
✅ Tipo molto popolare con alta varietà
💡 Usa /deck fire per suggerimenti!
```

### `/compare pikachu;raichu`
```
⚔️ CONFRONTO CARTE

🎴 Pikachu
❤️ HP: 35
⚡ Tipo: Electric

VS

🎴 Raichu
❤️ HP: 60
⚡ Tipo: Electric

🏆 Seconda carta ha più HP!
```

### `/globalstats`
```
📊 STATISTICHE GLOBALI BOT

👥 Utenti registrati: 42

🔥 TOP 5 POKEMON PIÙ CERCATI:
1. Pikachu (127 ricerche)
2. Charizard (98 ricerche)
3. Mewtwo (65 ricerche)
4. Blastoise (52 ricerche)
5. Venusaur (48 ricerche)

⚡ TIPI PIÙ POPOLARI:
1. Fire (89x)
2. Water (76x)
3. Electric (71x)

📈 COMANDI PIÙ USATI:
1. /look: 543 volte
2. /stats: 298 volte
3. /type: 187 volte

🎯 Ricerche totali: 1247
```

### `/position calli`
```
📍 Calli
📫 Viale Alessandro Manzoni 38, Vicenza
```
*(accompagnato da mappa interattiva con pin)*

---

## 📦 API Utilizzata

**PokéAPI v2**
- URL: https://pokeapi.co/api/v2
- Documentazione: https://pokeapi.co/docs/v2
- Nessuna API Key richiesta (gratuita e pubblica)
- Dati: Pokemon, statistiche, tipi, mosse, immagini ufficiali
- **TUTTI i Pokemon** vengono scaricati dall'API in tempo reale (non liste hardcoded)

---



Buon divertimento con i Pokemon! ⚡🎮