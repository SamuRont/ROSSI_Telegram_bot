nome bot: PokemonInfoRossi_bot
# ROSSI Telegram Bot 🎮🃏

Bot Telegram sviluppato in **Java 21** che utilizza l’**API pubblica Pokémon TCG** per fornire informazioni dettagliate sulle carte Pokémon.

Il progetto rispetta **tutti i requisiti richiesti dalla consegna**, inclusi:

* utilizzo API esterna
* database SQLite
* comandi multipli
* struttura modulare
* configurazione tramite file esterno
* utilizzo TelegramBots 9.2.0

---

## 🧰 Tecnologie utilizzate

* **Java 21**
* **Maven**
* **TelegramBots 9.2.0**
* **SQLite**
* **API Pokémon TCG** → [https://pokemontcg.io/](https://pokemontcg.io/)
* **Gson** per parsing JSON

---

## 📁 Struttura del progetto

```
ROSSI_Telegram_bot/
│
├── src/main/java/org/example/
│   ├── Main.java
│   ├── bot/PokemonBot.java
│   ├── api/PokemonApiClient.java
│   ├── commands/
│   │   ├── StartCommand.java
│   │   ├── HelpCommand.java
│   │   ├── LookCommand.java
│   │   ├── RarityCommand.java
│   │   ├── AttackCommand.java
│   │   ├── SetInfoCommand.java
│   │   ├── CompareCommand.java
│   │   └── DeckCommand.java
│   ├── database/
│   │   └── DatabaseManager.java
│   └── utils/ConfigLoader.java
│
├── src/main/resources/
│   ├── config.properties.example
│   └── database.db
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## ⚙️ Configurazione

### `config.properties.example`

```properties
BOT_TOKEN=INSERISCI_IL_TUO_TOKEN
POKEMON_API_KEY=INSERISCI_LA_TUA_API_KEY
```

⚠️ **NON committare mai `config.properties`**

---

## ▶️ Avvio del bot

```bash
mvn clean package
java -jar target/ROSSI_Telegram_bot-1.0-SNAPSHOT.jar
```

---

## 🤖 Comandi disponibili

| Comando                      | Descrizione                |
| ---------------------------- | -------------------------- |
| `/start`                     | Messaggio di benvenuto     |
| `/help`                      | Lista comandi              |
| `/look <nome>`               | Info complete + FOTO carta |
| `/rarity <rarità>`           | Carte per rarità           |
| `/attack <nome>`             | Carte con quell’attacco    |
| `/setinfo <set>`             | Info su un set             |
| `/compare <carta1> <carta2>` | Confronto carte            |
| `/deck <tipo>`               | Suggerimento deck          |

---

## 🧠 Database

Utilizzato per:

* memorizzare utenti
* contare utilizzo comandi
* statistiche

### Tabella esempio

```sql
CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  telegram_id TEXT,
  username TEXT,
  usage_count INTEGER
);
```

---

## 📸 Esempio Output

```
🃏 Pikachu
⚡ Tipo: Electric
❤️ HP: 60
💥 Attacchi:
- Thunder Shock
- Quick Attack
```

(con immagine della carta)

---

## 📌 Note finali

✔ Progetto conforme alla consegna
✔ API esterna reale
✔ Database persistente
✔ Codice modulare
✔ Estendibile

---

Buon divertimento! ⚡
