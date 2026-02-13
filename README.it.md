# Dynamax Unleashed

Una mod addon per Cobblemon che permette ai Pokémon di usare Dynamax e Gigantamax fuori dalla battaglia con un sistema di cooldown configurabile!

[![en](https://img.shields.io/badge/lang-en-red.svg)](README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](README.it.md)

## 📋 Descrizione

Questa mod estende la funzionalità Dynamax di Cobblemon oltre le battaglie, permettendo ai tuoi Pokémon di trasformarsi nelle loro forme giganti Dynamax o Gigantamax nell'overworld. Perfetta per mostrare i tuoi Pokémon preferiti o semplicemente per divertirsi con le loro forme massicce!

## ✨ Caratteristiche

- **🔴 Dynamax nell'Overworld**: Usa il Dynamax ovunque, non solo in battaglia
- **⏱️ Sistema di Cooldown**: Cooldown configurabile tra un uso e l'altro (default: 60 secondi)
- **📏 Scala Configurabile**: Regola la dimensione dei Pokémon Dynamaxed (default: 2.0x)
- **🎨 Supporto Gigantamax**: Tutte le forme Gigantamax sono supportate
- **🛡️ Requisiti di Battaglia**: Mantiene le stesse limitazioni del Dynamax in battaglia
- **🌍 Multilingua**: Traduzioni in inglese e italiano incluse
- **⚙️ Completamente Configurabile**: Personalizza ogni aspetto tramite file di configurazione

## 📋 Requisiti

- **Minecraft**: 1.21.1
- **Fabric Loader**: 0.16.0 o superiore
- **Fabric API**: 0.108.0+1.21.1 o superiore
- **Cobblemon**: 1.7.1+1.21.1 o superiore
- **Mega Showdown**: 1.6.0 o superiore **(OBBLIGATORIO)**
- **Architectury API**: 13.0.6 o superiore
- **Java**: 21 o superiore

> ⚠️ **Importante**: Mega Showdown è obbligatorio in quanto fornisce i modelli 3D e le texture Gigantamax. Senza, i Pokémon aumenteranno solo di dimensione senza cambiamenti visivi al modello.

## 📦 Installazione

1. Assicurati di avere installato Fabric Loader, Fabric API, Cobblemon, **Mega Showdown** e Architectury API
2. Scarica il file `.jar` della mod
3. Posiziona il file nella cartella `mods` della tua installazione di Minecraft
4. Avvia il gioco!

> 💡 **Nota**: Mega Showdown deve essere installato affinché le forme Gigantamax vengano visualizzate correttamente con modelli 3D personalizzati. La mod non si caricherà senza.

## 🎮 Come Usare

1. **Click destro** sul tuo Pokémon nel mondo
2. **Apri la ruota di interazione** - La stessa GUI usata per cavalcare, dare oggetti, ecc.
3. **Seleziona l'opzione "Dynamax"** dalla ruota
4. Il tuo Pokémon crescerà fino a dimensioni enormi!
5. Interagisci di nuovo e seleziona "Annulla Dynamax" per tornare alle dimensioni normali

### Condizioni per il Dynamax

✅ **Puoi usare il Dynamax se:**
- Il Pokémon non è in cooldown
- Il Pokémon soddisfa i requisiti di battaglia (se abilitato nella config)
- La mod è abilitata nella configurazione

❌ **Non puoi usare il Dynamax se:**
- Il Pokémon è Megaevoluto
- Il Pokémon è in forma Primordiale
- Il Pokémon è in Ultraesplosione
- Il Pokémon è attualmente in cooldown

## ⚙️ Configurazione

Il file di configurazione si trova in `config/dynamax-unleashed.json`:

```json
{
  "enabled": true,
  "cooldownSeconds": 60,
  "dynamaxScale": 2.0,
  "showCooldownMessage": true,
  "allowGigantamax": true,
  "maintainBattleRequirements": true,
  "messages": {
    "cooldownActive": "§cIl tuo Pokémon è troppo stanco per usare Dynamax! Aspetta {time} secondi.",
    "cannotDynamax": "§cQuesto Pokémon non può usare Dynamax!",
    "dynamaxActivated": "§b{pokemon} ha usato Dynamax!",
    "dynamaxReverted": "§e{pokemon} è tornato alle dimensioni normali."
  }
}
```

### Opzioni di Configurazione

| Opzione | Descrizione | Default |
|---------|-------------|---------|
| `enabled` | Abilita/disabilita la mod | `true` |
| `cooldownSeconds` | Tempo di cooldown in secondi | `60` |
| `dynamaxScale` | Moltiplicatore dimensione (1.0 = normale) | `2.0` |
| `showCooldownMessage` | Mostra messaggi di cooldown ai giocatori | `true` |
| `allowGigantamax` | Permetti forme Gigantamax | `true` |
| `maintainBattleRequirements` | Usa gli stessi requisiti della battaglia | `true` |

### Messaggi Personalizzati

Tutti i messaggi supportano codici colore (`§`) e segnaposto:
- `{time}` - Secondi di cooldown rimanenti
- `{pokemon}` - Nome visualizzato del Pokémon

## 🎮 Compatibilità

Questa mod **richiede e si integra** con:
- **Cobblemon** - Dipendenza principale per le meccaniche dei Pokémon
- **Mega Showdown** - **OBBLIGATORIO** - Fornisce modelli 3D, texture e animazioni Gigantamax
- **Architectury API** - Per il networking cross-platform

La mod funziona utilizzando le risorse Gigantamax di Mega Showdown aggiungendo la funzionalità di usare Dynamax fuori dalla battaglia con un sistema di cooldown. Senza Mega Showdown, i Pokémon aumenteranno solo di dimensione senza cambiare i loro modelli 3D.

## ❓ FAQ

**Q: Mega Showdown è obbligatorio?**  
A: Sì! Mega Showdown è una **dipendenza obbligatoria**. Fornisce i modelli 3D e le texture Gigantamax. Senza, la mod non si caricherà.

**Q: Cosa succede se non installo Mega Showdown?**  
A: Il gioco non caricherà questa mod poiché Mega Showdown è segnato come dipendenza obbligatoria nel fabric.mod.json.

**Q: Posso usarla in multiplayer?**  
A: Sì! La mod funziona sia in singleplayer che in multiplayer. Deve essere installata sul server.

**Q: Il Dynamax persiste dopo il riavvio del server?**  
A: No, i Pokémon torneranno alle dimensioni normali al riavvio del server, ma i cooldown vengono resettati.

**Q: Posso cambiare la dimensione dei Pokémon Dynamax?**  
A: Sì! Regola il valore `dynamaxScale` nella configurazione. Valori superiori a 2.0 li rendono ancora più grandi!

**Q: Funziona con i modpack?**  
A: Sì! Sentiti libero di includere questa mod nel tuo modpack.

## 🔧 Sviluppo

### Struttura del Progetto

```
dynamax-unleashed/
├── build.gradle                    # Configurazione build
├── gradle.properties               # Proprietà progetto
├── settings.gradle                 # Impostazioni Gradle
├── src/main/
│   ├── java/com/dynamaxunleashed/
│   │   ├── DynamaxUnleashed.java           # Entry point principale
│   │   ├── DynamaxUnleashedClient.java     # Entry point client
│   │   ├── config/
│   │   │   └── ModConfig.java              # Configurazione
│   │   ├── cooldown/
│   │   │   └── CooldownManager.java        # Gestione cooldown
│   │   ├── gimmick/
│   │   │   └── DynamaxGimmick.java         # Logica Dynamax principale
│   │   ├── handler/
│   │   │   └── InteractionGUIHandler.java  # Integrazione GUI
│   │   ├── networking/
│   │   │   ├── DynamaxPacket.java          # Pacchetto C2S
│   │   │   ├── DynamaxPacketHandler.java   # Gestore pacchetti
│   │   │   └── DynamaxNetworking.java      # Registrazione rete
│   │   └── util/
│   │       └── PlayerUtils.java            # Utilità helper
│   └── resources/
│       ├── fabric.mod.json                 # Metadata mod
│       ├── dynamax-unleashed.mixins.json   # Config mixin
│       └── assets/dynamax-unleashed/
│           ├── lang/
│           │   ├── en_us.json              # Traduzioni inglesi
│           │   └── it_it.json              # Traduzioni italiane
│           └── textures/gui/
│               └── dynamax_icon.png        # Icona ruota interazione
└── config/
    └── dynamax-unleashed.json              # Config predefinita
```

### Compilazione dal Codice Sorgente

**Requisiti:**
- JDK 21 o superiore
- Git (opzionale)

**Passaggi:**
```bash
# Clona o scarica il repository
git clone <repository-url>
cd dynamax-unleashed

# Compila la mod
./gradlew build

# Su Windows, usa:
.\gradlew.bat build
```

Il file `.jar` compilato sarà in `build/libs/dynamax-unleashed-1.0.0.jar`

## 📝 Problemi Noti
- Persistenza del cooldown tra riavvii del server non ancora implementata
- Nessun effetto visivo o particelle quando si attiva il Dynamax

## 🚀 Funzionalità Pianificate

- [ ] Comando admin `/dynamax clear <player/pokemon>` per resettare i cooldown
- [ ] Particelle visive quando si attiva il Dynamax
- [ ] Effetti sonori personalizzati
- [ ] Integrazione ModMenu per GUI di configurazione in-game
- [ ] Sistema di permessi per server multiplayer

## 📝 Licenza

Questa mod è rilasciata sotto licenza MIT. Sentiti libero di includerla nei tuoi modpack!

## 🐛 Segnalazione Bug

Se incontri problemi o bug, per favore segnalali con:
- Versione della mod
- Versioni di Minecraft/Fabric/Cobblemon/Architectury API
- Descrizione dettagliata del problema
- Log di crash (se applicabile)
- Passaggi per riprodurre il problema

## 🤝 Contributi

Contributi, issues e richieste di funzionalità sono benvenuti!

## 👏 Crediti

- **Cobblemon Team** - Per la fantastica mod Pokémon e l'API completa
- **Mega Showdown Team** - Per i modelli 3D Gigantamax, texture, animazioni e l'implementazione base del Dynamax su cui si basa questa mod
- **Architectury Team** - Per l'API di networking cross-platform
- **Community Modding** - Per supporto e testing

> 📝 **Ringraziamenti Speciali**: Questa mod utilizza le risorse Gigantamax create dal team di Mega Showdown. Tutti i modelli, texture e animazioni Gigantamax fanno parte della mod Mega Showdown.

---

**Nota**: Questa è una mod fan-made non ufficiale. Pokémon è un marchio registrato di Nintendo/Game Freak/The Pokémon Company.

**Buon Dynamax!** 🔴⚡
