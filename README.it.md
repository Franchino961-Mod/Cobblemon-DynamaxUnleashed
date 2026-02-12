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
- **Fabric API**: Ultima versione
- **Cobblemon**: 1.7.0 o superiore
- **Mega Showdown**: 1.6.0 o superiore
- **Java**: 21 o superiore

## 📦 Installazione

1. Assicurati di avere installato Fabric Loader, Fabric API, Cobblemon e Mega Showdown
2. Scarica il file `.jar` della mod
3. Posiziona il file nella cartella `mods` della tua installazione di Minecraft
4. Avvia il gioco!

## 🎮 Come Usare

1. **Ottieni un Polsino Dynamax** - Richiesto di default (dalla mod Mega Showdown)
2. **Interagisci con il tuo Pokémon** nel mondo
3. **Seleziona "Dynamax"** dal menu di interazione (simile alla Megaevoluzione)
4. Il tuo Pokémon crescerà fino a dimensioni enormi!
5. Clicca di nuovo per tornare alle dimensioni normali

### Condizioni per il Dynamax

✅ **Puoi usare il Dynamax se:**
- Hai un Polsino Dynamax (se richiesto nella configurazione)
- Il Pokémon non è in cooldown
- Il Pokémon soddisfa i requisiti di battaglia

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
  "requireDynamaxBand": true,
  "maintainBattleRequirements": true,
  "messages": {
    "cooldownActive": "§cIl tuo Pokémon è troppo stanco per usare Dynamax! Aspetta {time} secondi.",
    "noDynamaxBand": "§cHai bisogno di un Polsino Dynamax per usare Dynamax fuori dalla battaglia!",
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
| `requireDynamaxBand` | Richiedi l'oggetto Polsino Dynamax | `true` |
| `maintainBattleRequirements` | Usa gli stessi requisiti della battaglia | `true` |

### Messaggi Personalizzati

Tutti i messaggi supportano codici colore (`§`) e segnaposto:
- `{time}` - Secondi di cooldown rimanenti
- `{pokemon}` - Nome visualizzato del Pokémon

## 🎮 Compatibilità

Questa mod si integra perfettamente con:
- **Cobblemon** - Dipendenza principale per le meccaniche dei Pokémon
- **Mega Showdown** - Fornisce il Polsino Dynamax e le funzionalità Dynamax di battaglia

La mod è progettata per funzionare insieme ad altri addon di Cobblemon senza conflitti.

## ❓ FAQ

**Q: Devo installare Mega Showdown?**  
A: Sì, Mega Showdown è richiesto poiché fornisce il Polsino Dynamax e le meccaniche base del Dynamax.

**Q: Posso usarla in multiplayer?**  
A: Sì! La mod funziona sia in singleplayer che in multiplayer. Deve essere installata sul server.

**Q: Il Dynamax persiste dopo il riavvio del server?**  
A: No, i Pokémon torneranno alle dimensioni normali al riavvio del server, ma i cooldown vengono resettati.

**Q: Posso cambiare la dimensione dei Pokémon Dynamax?**  
A: Sì! Regola il valore `dynamaxScale` nella configurazione. Valori superiori a 2.0 li rendono ancora più grandi!

**Q: Posso disabilitare il requisito del Polsino Dynamax?**  
A: Sì, imposta `requireDynamaxBand` su `false` nella configurazione.

**Q: Funziona con i modpack?**  
A: Sì! Sentiti libero di includere questa mod nel tuo modpack.

## 🔧 Sviluppo

### Struttura del Progetto

```
dynamax-unleashed/
├── build.gradle                    # Configurazione build
├── fabric.mod.json                 # Metadata della mod
├── dynamax-unleashed.mixins.json   # Configurazione mixin
├── config/
│   └── dynamax-unleashed.json      # Configurazione predefinita
├── com/dynamaxunleashed/
│   ├── DynamaxUnleashed.java       # Entry point
│   ├── config/
│   │   └── ModConfig.java          # Gestione configurazione
│   ├── cooldown/
│   │   └── CooldownManager.java    # Sistema cooldown
│   ├── handler/
│   │   └── PokemonInteractionHandler.java  # Gestore interazioni
│   └── mixin/
│       └── DynamaxRequestMixin.java  # Bypass restrizioni battaglia
└── assets/dynamax_unleashed/lang/
    ├── en_us.json                  # Traduzioni inglesi
    └── it_it.json                  # Traduzioni italiane
```

### Compilazione dal Codice Sorgente

```bash
./gradlew build
```

Il file `.jar` compilato sarà in `build/libs/`

## 📝 Problemi Noti

- Integrazione del pulsante GUI con la schermata di interazione di Cobblemon (richiede API più recente)
- Persistenza del cooldown tra riavvii del server

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
- Versioni di Minecraft/Fabric/Cobblemon/Mega Showdown
- Descrizione dettagliata del problema
- Log di crash (se applicabile)

## 🤝 Contributi

Contributi, issues e richieste di funzionalità sono benvenuti!

## 👏 Crediti

- **Cobblemon Team** - Per la fantastica mod Pokémon
- **Mega Showdown Team** - Per l'implementazione base del Dynamax
- **Community Modding** - Per supporto e testing

---

**Nota**: Questa è una mod fan-made non ufficiale. Pokémon è un marchio registrato di Nintendo/Game Freak/The Pokémon Company.

**Buon Dynamax!** 🔴⚡
