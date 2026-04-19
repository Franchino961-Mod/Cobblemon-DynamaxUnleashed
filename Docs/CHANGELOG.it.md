# Changelog - Dynamax Unleashed

Tutte le modifiche rilevanti a Dynamax Unleashed verranno documentate in questo file.

Il formato è basato su [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e questo progetto aderisce al [Versionamento Semantico](https://semver.org/spec/v2.0.0.html).

---

## [Non Rilasciato]

### Pianificato
- //

---

## [1.2.4] - 2026-04-19

### Corretto
- **Race sul Re-Dynamax durante Shrink** — Corretto un problema di timing in cui riattivando Dynamax troppo velocemente mentre la transizione di shrink post-revert/post-battaglia era ancora in corso, poteva essere salvata una scala instabile causando dimensioni errate ai revert successivi.
- **Guardia di Stabilizzazione Riattivazione** — Aggiunto un breve lock server-side dopo i percorsi di revert/recovery per impedire il re-Dynamax immediato durante stati di transizione.

---

## [1.2.3] - 2026-04-16

### Aggiunto
- **Traduzione Francese (`fr_fr`)** — Aggiunto il file lingua francese completo per pulsanti, messaggi di gameplay e feedback dei comandi admin.

### Corretto
- **Messaggi Gameplay Localizzati** — Sostituiti i messaggi hardcoded/literal con chiavi traducibili, cosi la lingua client non inglese viene applicata correttamente nei messaggi in chat.
- **Desync Etichetta Pulsante Dynamax** — Corretto il desync della ruota interazione dove il pulsante poteva restare su "Annulla Dynamax" dopo il revert; ora la GUI usa un controllo visual-state sicuro lato client.

---

## [1.2.2] - 2026-04-14

### Aggiunto
- **Comando di Recupero Scala Bloccata** — Aggiunto `/dynamax fixscale <giocatore> <slot>` (OP 2) per recuperare Pokémon bloccati con dimensione normale aumentata, ripulendo lo stato legato al Dynamax e normalizzando la scala.

### Corretto
- **Ordine Salvataggio Scala in Gigantamax** — Corretto un bug per cui, su alcune specie compatibili Gmax, lo snapshot della scala originale poteva essere salvato dopo i cambi forma/aspect. Questo poteva lasciare la forma normale ingrandita dopo il revert.
- **Ordine Corretto anche nel Percorso Force** — Applicato lo stesso ordine corretto anche alle operazioni admin forzate (`dynamaxForce()` / `undynamaxForce()`), così la scala viene sempre salvata prima dell'applicazione dell'aspect Gmax.

---

## [1.2.1] - 2026-03-18

### Corretto
- **Preservazione Size Variation al Revert** — Corretto un problema per cui i Pokémon con size variation non standard (es. Huge) potevano tornare a dimensione media dopo un-Dynamax/un-Gigamax. Ora la mod salva lo scale modifier originale prima del Dynamax e lo ripristina correttamente al revert.
- **Ripristino Scala nei Comandi Force** — Applicata la stessa logica di preservazione della scala anche alle operazioni admin forzate (`dynamaxForce()` / `undynamaxForce()`), garantendo comportamento coerente tra toggle normali e forzati.

---

## [1.2.0] - 2026-02-28

### Aggiunto
- **Revisione dei Comandi Admin** — Il comando `/dynamax` è stato ristrutturato con tre sottocomandi:
  - `/dynamax <giocatore> <slot>` — Attiva/disattiva il Dynamax forzatamente, **bypassando tutti i requisiti** (Dynamax Band, Power Spot, Fattore G-Max, cooldown). Richiede OP livello 2.
  - `/dynamax clear <giocatore>` — Azzera i cooldown Dynamax di tutti i Pokémon nella squadra di un giocatore. Richiede OP livello 2.
  - `/dynamax reload` — Ricarica il file di configurazione senza riavviare il server. Richiede OP livello 4.
- **Validazione della Configurazione** — I valori del config vengono ora validati al caricamento:
  - `cooldownSeconds` deve essere ≥ 0 (viene resettato a 60 se negativo)
  - `dynamaxScale` deve essere > 0 (viene resettato a 2.0 se zero o negativo)
  - `powerSpotRange` deve essere tra 1 e 256 (viene resettato a 20 se fuori range)
  - Un file di config malformato o vuoto ora ricade sui valori predefiniti in modo sicuro
- **Aggiornamento Automatico del Config** — Il file di config viene risalvato automaticamente dopo un caricamento riuscito per aggiungere eventuali nuovi campi introdotti da un aggiornamento della mod
- **Limite di Frequenza Pacchetti Lato Server** — Il server ora ignora le richieste `DynamaxPacket` inviate entro 500ms dall'ultima dello stesso giocatore, prevenendo potenziali attacchi di flooding da client malevoli
- **Nuovo Messaggio** — Aggiunto `pokemonNotFound` nella sezione `messages` del config (precedentemente era hardcoded nel codice)

### Corretto
- **Il Cooldown parte ora al Revert** — Il timer del cooldown ora inizia quando il giocatore annulla il Dynamax, non quando lo attiva. Questo garantisce che l'intero cooldown debba trascorrere prima di riattivarlo
- **Fix Revert Gigantamax** — `undynamax()` ora correttamente controlla `getForcedAspects()` invece di `getAspects()` quando rimuove l'aspetto `gmax`. In precedenza, se l'aspetto era presente nel set unificato ma non in quello forzato, il modello non tornava correttamente alla forma normale
- **Preservazione dello Stato Tradeable** — I Pokémon che erano già non-scambiabili prima di essere Dynamaxati ora rimarranno correttamente non-scambiabili dopo il revert. In precedenza il revert impostava sempre `setTradeable(true)` in modo incondizionato
- **Codice Morto Rimosso** — Rimosso il metodo privato inutilizzato `getGigantamaxForm()` da `DynamaxGimmick`
- **Gestione Sicura dei Pacchetti** — `DynamaxPacketHandler` ora usa un check `instanceof` sicuro prima di fare il cast del player, prevenendo una potenziale `ClassCastException`
- **Log delle Eccezioni Silenziate** — `PlayerUtils.getPartyPokemonFromUUID()` non ignora più silenziosamente le eccezioni; viene ora emesso un `LOGGER.warn` con il contesto del giocatore e dell'UUID

### Modificato
- **Pulizia dei Log** — Il messaggio di log dell'evento `POKEMON_INTERACTION_GUI_CREATION` in `InteractionGUIHandler` è stato abbassato da `INFO` a `DEBUG`, eliminando lo spam nei log in produzione
- Il messaggio `pokemonNotFound` è stato spostato dalla stringa hardcoded a `config.messages.pokemonNotFound`, in linea con tutti gli altri messaggi visibili all'utente

### Tecnico
- Aggiunti i metodi pubblici `dynamaxForce()` e `undynamaxForce()` a `DynamaxGimmick` per le operazioni di bypass admin
- Aggiunto il metodo statico `setConfig(ModConfig)` a `DynamaxUnleashed` per supportare il reload a caldo
- Aggiunto il metodo `validate()` a `ModConfig` per i controlli di range post-caricamento
- Mappa timestamp per-giocatore in `DynamaxPacketHandler` tramite `ConcurrentHashMap<UUID, Long>`
- Nuova chiave NBT `pre_dynamax_tradeable` persistita sul Pokémon durante il Dynamax per tracciare lo stato tradeable originale

---

## [1.1.0] - 2026-02-13

### Aggiunto
- **Integrazione Requisiti Mega Showdown** — Compatibilità completa con il sistema di requisiti di battaglia di Mega Showdown
  - **Requisito Dynamax Band**: I giocatori devono avere una Dynamax Band (`mega_showdown:dynamax_band`) nell'inventario
  - **Prossimità Power Spot**: I giocatori devono trovarsi entro il raggio di un blocco Power Spot (default: 20 blocchi)
  - **Validazione Fattore G-Max**: I Pokémon devono avere il Fattore G-Max abilitato per usare le forme Gigantamax
  - Tutti e tre i requisiti sono **attivi per default** per un'esperienza compatibile MSD
- Nuove opzioni di configurazione:
  - `requireDynamaxBand` (default: `true`) - Richiedi Dynamax Band
  - `requirePowerSpot` (default: `true`) - Richiedi prossimità Power Spot
  - `powerSpotRange` (default: `20`) - Distanza massima dal Power Spot in blocchi
  - `dynamaxAnywhere` (default: `false`) - Bypass requisito Power Spot (modalità creativa/debug)
  - `requireGmaxFactor` (default: `true`) - Richiedi Fattore G-Max per Gigantamax
- Nuovi messaggi di errore con traduzioni (EN/IT)
- Nuova classe `DynamaxUtils` con metodi di utilità:
  - `isPowerSpotNearby()` - Controlla i blocchi Power Spot con il tag MSD `mega_showdown:power_spot`
  - `isBlockNearby()` - Scanner di prossimità blocchi generico (ricerca sferica a raggio cubico)
  - `hasDynamaxBand()` - Valida la Dynamax Band tramite Accessories API (slot equipaggiamento, mano principale e secondaria)
- Nuova classe `AccessoriesUtils` per l'integrazione con Accessories API
- Nuova classe `DynamaxTags` per la definizione del tag dell'oggetto Dynamax Band

### Modificato
- Comportamento di default: tutti i requisiti MSD ora abilitati per default
- Il controllo Power Spot usa il tag ufficiale `mega_showdown:power_spot` di Mega Showdown
- I controlli requisiti vengono eseguiti **prima** del controllo cooldown (priorità: Band → Power Spot → Fattore G-Max → Cooldown)
- Rilevamento Dynamax Band aggiornato dall'analisi di stringhe alle Accessories API

### Dipendenze
- **Mega Showdown 1.6.0+** ora richiesto per il tag del blocco Power Spot
- **Accessories API 1.1.0-beta.52+1.21.1** aggiunto per il rilevamento della Dynamax Band negli slot equipaggiamento

### Note
- Usa `dynamaxAnywhere: true` nel config per bypassare il requisito Power Spot in modalità testing/creativa

---

## [1.0.0] - Rilascio Iniziale

### Aggiunto
- **Dynamax Fuori dalla Battaglia** — Usa la trasformazione Dynamax ovunque nell'overworld
- **Supporto Visivo Gigantamax** — Trasformazioni del modello 3D per tutte le forme Gigantamax (richiede Mega Showdown)
- **Integrazione GUI** — Pulsante Dynamax nella ruota di interazione di Cobblemon con icona personalizzata e tooltip tradotti
- **Sistema di Cooldown** — Cooldown configurabile per Pokémon (default: 60s)
- **Requisiti di Battaglia** — Blocca il Dynamax durante Megaevoluzione, Archeorisveglio o Ultraesplosione
- **Sistema di Configurazione** — Config JSON in `config/dynamax-unleashed.json` con messaggi personalizzabili
- **Networking Client-Server** — Sistema di pacchetti `DynamaxPacket` C2S con validazione lato server
- **Supporto Multilingua** — Traduzioni in inglese e italiano

### Tecnico
- Usa `pokemon.setForcedAspects()` per la sincronizzazione dei modelli Gigantamax lato client
- Implementazione senza Mixin, tramite hook dell'API Cobblemon
- Struttura modulare a pacchetti

### Corretto
- Coerenza del namespace: gli asset usano `dynamax_unleashed` (underscore)
- Chiavi di traduzione normalizzate a `dynamax_unleashed.button.*`
- Sincronizzazione modello Gigantamax passata da `FlagSpeciesFeature` a `forcedAspects`

### Problemi Noti
- Il cooldown non persiste tra i riavvii del server
- Nessun effetto visivo all'attivazione del Dynamax

---

**Guida al Versionamento**:
- **MAJOR** (X.0.0) - Modifiche API incompatibili, riscritture importanti
- **MINOR** (1.X.0) - Nuove funzionalità, aggiunte retrocompatibili
- **PATCH** (1.1.X) - Correzioni di bug, miglioramenti minori

[1.2.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.0
[1.2.1]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.1
[1.2.2]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.2
[1.2.3]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.3
[1.2.4]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.4
[1.1.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.1.0
[1.0.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.0.0
