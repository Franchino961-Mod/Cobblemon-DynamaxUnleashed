# 🔴 Dynamax Unleashed

Una mod addon per Cobblemon che permette ai Pokémon di usare Dynamax e Gigantamax fuori dalla battaglia con un sistema di cooldown configurabile!

[![Download on CurseForge](https://img.shields.io/badge/Download_on-CurseForge-orange?style=for-the-badge&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/cobblemon-dynamax-unleashed)

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green.svg)](https://www.minecraft.net/)
[![Versione](https://img.shields.io/badge/versione-1.2.4-blue.svg)]()
[![Fabric](https://img.shields.io/badge/Fabric-0.16.9-blue.svg)](https://fabricmc.net/)
[![Licenza](https://img.shields.io/badge/Licenza-MIT-yellow.svg)](../LICENSE)

[![en](https://img.shields.io/badge/lang-en-red.svg)](../README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](README.it.md)

> 📝 **Changelog**: Vedi [CHANGELOG.it.md](CHANGELOG.it.md) per la cronologia delle versioni.

## 📋 Descrizione

Questa mod estende la funzionalità Dynamax di Cobblemon oltre le battaglie, permettendo ai tuoi Pokémon di trasformarsi nelle loro forme giganti Dynamax o Gigantamax nell'overworld. Perfetta per mostrare i tuoi Pokémon preferiti o semplicemente per divertirsi con le loro forme massicce!

## ✨ Funzionalità

- **🔴 Dynamax nell'Overworld**: Usa il Dynamax ovunque, non solo in battaglia
- **⭐ Integrazione Mega Showdown**: Compatibilità totale con i requisiti MSD (v1.1.0)
  - **Dynamax Band Richiesta**: I giocatori necessitano di una Dynamax Band per usare Dynamax
  - **Prossimità Power Spot**: Bisogna essere vicini a un blocco Power Spot (raggio 20 blocchi)
  - **Validazione Fattore G-Max**: I Pokémon devono avere il Fattore G-Max per le forme Gigantamax
- **⏱️ Sistema di Cooldown**: Cooldown configurabile tra un uso e l'altro (default: 60 secondi)
- **📏 Scala Configurabile**: Regola la dimensione dei Pokémon Dynamaxed (default: 2.0x)
- **🎨 Supporto Gigantamax**: Tutte le forme Gigantamax con modelli 3D completi
- **🛡️ Requisiti di Battaglia**: Previene Dynamax durante Megaevoluzione, Archeorisveglio, Ultraesplosione
- **🌍 Multilingua**: Traduzioni in inglese, italiano e francese incluse
- **⚙️ Completamente Configurabile**: Personalizza ogni aspetto tramite file di configurazione, con validazione automatica dei valori
- **🔧 Comandi Admin**: Controllo admin completo via sottocomandi `/dynamax`

## 📦 Requisiti

- **Minecraft**: 1.21.1
- **Fabric Loader**: 0.16.0 o superiore
- **Fabric API**: 0.108.0+1.21.1 o superiore
- **Cobblemon**: 1.7.1+1.21.1 o superiore
- **Mega Showdown**: 1.6.0 o superiore **(OBBLIGATORIO)**
- **Architectury API**: 13.0.6 o superiore
- **Java**: 21 o superiore

> ⚠️ **Importante**: Mega Showdown è obbligatorio in quanto fornisce i modelli 3D, le texture e le animazioni Gigantamax. Senza, i Pokémon aumenteranno solo di dimensione senza cambiamenti visivi al modello.

## 📦 Installazione

1. Assicurati di avere installato Fabric Loader, Fabric API, Cobblemon, **Mega Showdown** e Architectury API
2. Scarica il file `.jar` della mod
3. Posiziona il file nella cartella `mods` della tua installazione di Minecraft
4. Avvia il gioco!

> 💡 **Nota**: Mega Showdown deve essere installato affinché le forme Gigantamax vengano visualizzate correttamente con modelli 3D personalizzati. La mod non si caricherà senza.

## 🎮 Come Usare

1. **Click destro** sul tuo Pokémon nel mondo
2. **Apri la ruota di interazione** — La stessa GUI usata per cavalcare, dare oggetti, ecc.
3. **Seleziona l'opzione "Dynamax"** dalla ruota
4. Il tuo Pokémon crescerà fino a dimensioni enormi!
5. Interagisci di nuovo e seleziona "Annulla Dynamax" per tornare alle dimensioni normali

### Condizioni per il Dynamax

✅ **Puoi usare il Dynamax se:**
- Hai una **Dynamax Band** nel tuo inventario (se abilitato)
- Sei vicino a un blocco **Power Spot** entro il raggio (se abilitato)
- Il Pokémon non è in cooldown
- Il Pokémon soddisfa i requisiti di battaglia (nessun altro gimmick attivo)
- La mod è abilitata nella configurazione

✅ **Puoi usare Gigantamax se:**
- Tutte le condizioni Dynamax sopra sono soddisfatte
- Il Pokémon ha il **Fattore G-Max** abilitato (se richiesto nella config)
- La specie del Pokémon ha una forma Gigantamax

❌ **Non puoi usare il Dynamax se:**
- Non hai una Dynamax Band (quando `requireDynamaxBand: true`)
- Nessun Power Spot è nelle vicinanze (quando `requirePowerSpot: true` e `dynamaxAnywhere: false`)
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
  "requireDynamaxBand": true,
  "requirePowerSpot": true,
  "powerSpotRange": 20,
  "dynamaxAnywhere": false,
  "requireGmaxFactor": true,
  "messages": {
    "cooldownActive": "§cIl tuo Pokémon è troppo stanco per usare Dynamax! Aspetta {time} secondi.",
    "cannotDynamax": "§cQuesto Pokémon non può usare Dynamax!",
    "dynamaxActivated": "§b{pokemon} ha usato Dynamax!",
    "dynamaxReverted": "§e{pokemon} è tornato alle dimensioni normali.",
    "noDynamaxBand": "§cHai bisogno di una Dynamax Band per usare Dynamax!",
    "noPowerSpot": "§cDevi essere vicino a un Power Spot per usare Dynamax!",
    "noGmaxFactor": "§cQuesto Pokémon non può usare Gigantamax! (Manca il Fattore G-Max)",
    "pokemonNotFound": "§cPokémon non trovato nella tua squadra!"
  }
}
```

| Opzione | Descrizione | Default |
|---------|-------------|---------|
| `enabled` | Abilita/disabilita la mod | `true` |
| `cooldownSeconds` | Tempo di cooldown in secondi | `60` |
| `dynamaxScale` | Moltiplicatore dimensione (1.0 = normale) | `2.0` |
| `showCooldownMessage` | Mostra messaggi di cooldown ai giocatori | `true` |
| `allowGigantamax` | Permetti forme Gigantamax | `true` |
| `maintainBattleRequirements` | Usa gli stessi requisiti della battaglia | `true` |
| `requireDynamaxBand` | **[v1.1.0]** Richiedi Dynamax Band nell'inventario | `true` |
| `requirePowerSpot` | **[v1.1.0]** Richiedi prossimità Power Spot | `true` |
| `powerSpotRange` | **[v1.1.0]** Raggio di ricerca Power Spot in blocchi | `20` |
| `dynamaxAnywhere` | **[v1.1.0]** Ignora requisito Power Spot | `false` |
| `requireGmaxFactor` | **[v1.1.0]** Richiedi Fattore G-Max per Gigantamax | `true` |

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
│   │   ├── command/
│   │   │   └── DynamaxCommand.java         # Comandi admin (force/clear/fixscale/reload)
│   │   ├── config/
│   │   │   └── ModConfig.java              # Configurazione + validazione
│   │   ├── cooldown/
│   │   │   └── CooldownManager.java        # Gestione cooldown
│   │   ├── gimmick/
│   │   │   └── DynamaxGimmick.java         # Logica Dynamax principale
│   │   ├── handler/
│   │   │   └── InteractionGUIHandler.java  # Integrazione GUI
│   │   ├── networking/
│   │   │   ├── DynamaxPacket.java          # Pacchetto C2S
│   │   │   ├── DynamaxPacketHandler.java   # Gestore pacchetti + rate-limit
│   │   │   └── DynamaxNetworking.java      # Registrazione rete
│   │   ├── tag/
│   │   │   └── DynamaxTags.java            # Tag oggetto (Dynamax Band)
│   │   └── utils/
│   │       ├── AccessoriesUtils.java       # Integrazione Accessories API
│   │       ├── DynamaxUtils.java           # Validazione requisiti
│   │       ├── PlayerUtils.java            # Helper lookup squadra
│   │       └── PokemonAnimationHelper.java # Helper pacchetti animazione
│   └── resources/
│       ├── fabric.mod.json                 # Metadata mod
│       ├── dynamax-unleashed.mixins.json   # Config mixin
│       └── assets/dynamax_unleashed/
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

Il file `.jar` compilato sarà in `build/libs/cobblemon-dynamax-unleashed-1.2.4.jar`

## 📝 Problemi Noti

- Persistenza del cooldown tra riavvii del server non ancora implementata
- Nessun effetto visivo o particelle quando si attiva il Dynamax
- Il pulsante Dynamax nella GUI non mostra visivamente lo stato del cooldown (appare sempre attivo)

## 🚀 Funzionalità Pianificate

- [x] ~~Comando admin `/dynamax clear <player>` per resettare i cooldown~~ *(aggiunto nella v1.2.0)*
- [x] ~~Ricarica config con `/dynamax reload`~~ *(aggiunto nella v1.2.0)*
- [x] ~~Comando admin `/dynamax fixscale <player> <slot>` per recuperare Pokémon bloccati a scala aumentata~~ *(aggiunto nella v1.2.2)*
- [ ] Particelle visive all'attivazione del Dynamax
- [ ] Effetti sonori personalizzati
- [ ] Integrazione ModMenu per GUI di configurazione in-game
- [ ] Sistema di permessi per server multiplayer
- [ ] Indicatore visivo del cooldown sul pulsante Dynamax nella ruota di interazione

## 🔧 Comandi Admin

| Comando | Descrizione | Permesso |
|---------|-------------|----------|
| `/dynamax <giocatore> <slot>` | Attiva/disattiva il Dynamax forzatamente, bypassando tutti i requisiti | OP 2 |
| `/dynamax clear <giocatore>` | Azzera i cooldown Dynamax di tutti i Pokémon nella squadra | OP 2 |
| `/dynamax fixscale <giocatore> <slot>` | Ripristina scala/stato di un Pokémon bloccato e lo riporta alle dimensioni normali | OP 2 |
| `/dynamax reload` | Ricarica il config da disco senza riavviare | OP 4 |

## 📄 Licenza

Questa mod è rilasciata sotto la [Licenza MIT](../LICENSE). Sentiti libero di includerla nei tuoi modpack!

## 👤 Autore

**Franchino961** — [GitHub](https://github.com/Franchino961-Mod)

## 🤝 Contributi

Contributi, issue e richieste di funzionalità sono benvenuti!
- Apri una [Issue](../../issues) per segnalare bug o suggerire funzionalità
- Apri una [Pull Request](../../pulls) per contribuire al codice

## 💬 Supporto

Se riscontri problemi o bug, segnalali includendo:
- Versione della mod
- Versioni di Minecraft / Fabric / Cobblemon / Architectury API
- Descrizione dettagliata del problema
- Log di crash (se applicabili)
- Passaggi per riprodurre il problema
- [Apri una Issue](../../issues)

## 🙏 Crediti

- **Cobblemon Team** - Per la fantastica mod Pokémon e l'API completa
- **Mega Showdown Team** - Per i modelli 3D Gigantamax, texture, animazioni e l'implementazione base del Dynamax su cui si basa questa mod
- **Architectury Team** - Per l'API di networking cross-platform
- **Community Modding** - Per supporto e testing

> 📝 **Ringraziamenti Speciali**: Questa mod utilizza le risorse Gigantamax create dal team di Mega Showdown. Tutti i modelli, texture e animazioni Gigantamax fanno parte della mod Mega Showdown.

## 🔗 Link

- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/cobblemon-dynamax-unleashed)
- [Mod Cobblemon](https://cobblemon.com)
- [CobblemonMegaShowdown](https://www.curseforge.com/minecraft/mc-mods/cobblemon-megashowdown) — Mod richiesta
- [Architectury API](https://www.curseforge.com/minecraft/mc-mods/architectury-api)
- [Fabric](https://fabricmc.net/)

## 📝 Changelog

Vedi [CHANGELOG.it.md](CHANGELOG.it.md) per la cronologia completa delle versioni.

---

**Nota**: Questa è una mod fan-made non ufficiale. Pokémon è un marchio registrato di Nintendo/Game Freak/The Pokémon Company.

**Buon Dynamax!** 🔴⚡
