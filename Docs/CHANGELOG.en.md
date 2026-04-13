# Changelog - Dynamax Unleashed

All notable changes to **Dynamax Unleashed** will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Planned
- //

---

## [1.2.2] - 2026-04-14

### Added
- **Recovery Command for Stuck Scale** — Added `/dynamax fixscale <player> <slot>` (OP 2) to recover Pokémon stuck with enlarged normal size by clearing Dynamax-related state and normalizing scale.

### Fixed
- **Gigantamax Scale Snapshot Order** — Fixed a bug where, for some Gmax-capable species, the original scale snapshot could be captured after form/aspect changes. This could cause the normal form to remain enlarged after revert.
- **Consistent Scale Snapshot in Force Path** — Applied the same corrected ordering to admin force operations (`dynamaxForce()` / `undynamaxForce()`) so scale is captured before Gmax aspect application.

---

## [1.2.1] - 2026-03-18

### Fixed
- **Size Variation Preservation on Revert** — Fixed an issue where Pokémon with non-default size variations (e.g. Huge) could revert to average size after un-Dynamax/un-Gigantamax. The mod now stores each Pokémon's original scale modifier before Dynamax and restores it correctly on revert.
- **Force Command Scale Restore** — Applied the same scale-preservation logic to admin force operations (`dynamaxForce()` / `undynamaxForce()`), ensuring consistent behavior across normal and forced toggles.

---

## [1.2.0] - 2026-02-28

### Added
- **Admin Command Overhaul** - Redesigned `/dynamax` command with three subcommands:
  - `/dynamax <player> <slot>` — Force-toggle Dynamax, **bypassing all requirements** (Dynamax Band, Power Spot, G-Max Factor, cooldown). Requires OP level 2.
  - `/dynamax clear <player>` — Reset Dynamax cooldowns for all Pokémon in a player's party. Requires OP level 2.
  - `/dynamax reload` — Reload the config from disk without restarting the server. Requires OP level 4.
- **Config Validation** — Values are now validated on load:
  - `cooldownSeconds` must be ≥ 0 (resets to 60 if negative)
  - `dynamaxScale` must be > 0 (resets to 2.0 if zero or negative)
  - `powerSpotRange` must be between 1 and 256 (resets to 20 if out of range)
  - Malformed or empty config file now gracefully falls back to defaults
- **Config Auto-Update** — Config file is automatically re-saved after a successful load to add any new fields introduced by a mod update (no more missing keys for existing users)
- **Server-side Packet Rate-Limit** — The server now ignores `DynamaxPacket` requests sent within 500ms of the previous one from the same player, preventing potential packet flooding from malicious clients
- **New Message** — Added `pokemonNotFound` to the `messages` config section (previously hardcoded)

### Fixed
- **Cooldown now starts on Revert** — The cooldown timer now starts when the player reverts Dynamax, not when they activate it. This ensures the full cooldown must pass before re-activating, avoiding the previous behavior where reverting immediately would still consume most of the cooldown
- **Gigantamax Revert Fix** — `undynamax()` now correctly checks `getForcedAspects()` instead of `getAspects()` when removing the `gmax` aspect. Previously, if the aspect appeared in the merged set but not in the forced set, the model would not properly revert
- **Tradeable State Preservation** — Pokémon that were already non-tradeable before being Dynamaxed will now correctly remain non-tradeable after reverting. Previously, reverting always forced `setTradeable(true)` unconditionally
- **Dead Code Removed** — Removed unused private method `getGigantamaxForm()` from `DynamaxGimmick`
- **Safe Packet Handling** — `DynamaxPacketHandler` now uses a safe `instanceof` pattern check before casting the player object, preventing a potential `ClassCastException`
- **Silent Exception Logging** — `PlayerUtils.getPartyPokemonFromUUID()` no longer silently swallows exceptions; a `LOGGER.warn` is now emitted with player and UUID context

### Changed
- **Log Cleanup** — The `POKEMON_INTERACTION_GUI_CREATION` event log entry in `InteractionGUIHandler` has been lowered from `INFO` to `DEBUG`, eliminating log spam in production environments
- **`pokemonNotFound` message** moved from hardcoded string to `config.messages.pokemonNotFound`, consistent with all other user-facing messages

### Technical
- Added `dynamaxForce()` and `undynamaxForce()` public methods to `DynamaxGimmick` for admin bypass operations
- Added `setConfig(ModConfig)` static method to `DynamaxUnleashed` to support hot-reload
- Added `validate()` method to `ModConfig` for post-load range checks
- Per-player packet timestamp map in `DynamaxPacketHandler` using `ConcurrentHashMap<UUID, Long>`
- New NBT key `pre_dynamax_tradeable` persisted on Pokémon during Dynamax to track original tradeable state

---

## [1.1.0] - 2026-02-13

### Added
- **Mega Showdown Requirements Integration** - Full compatibility with Mega Showdown's battle requirements system
  - **Dynamax Band Requirement**: Players must have a Dynamax Band (`mega_showdown:dynamax_band`) in their inventory to use Dynamax
  - **Power Spot Proximity**: Players must be within range of a Power Spot block to Dynamax (default: 20 blocks)
  - **G-Max Factor Validation**: Pokémon must have G-Max Factor enabled to use Gigantamax forms
  - All three requirements are **enabled by default** for hardcore MSD-compatible experience
- New configuration options:
  - `requireDynamaxBand` (default: `true`) - Enforce Dynamax Band requirement
  - `requirePowerSpot` (default: `true`) - Enforce Power Spot proximity requirement
  - `powerSpotRange` (default: `20`) - Maximum distance from Power Spot in blocks
  - `dynamaxAnywhere` (default: `false`) - Bypass Power Spot requirement (creative/debug mode)
  - `requireGmaxFactor` (default: `true`) - Enforce G-Max Factor for Gigantamax forms
- New error messages with translations (EN/IT):
  - `noDynamaxBand` - "§cYou need a Dynamax Band to use Dynamax!"
  - `noPowerSpot` - "§cYou must be near a Power Spot to use Dynamax!"
  - `noGmaxFactor` - "§cThis Pokémon cannot Gigantamax! (Missing G-Max Factor)"
- New utility class `DynamaxUtils` with helper methods:
  - `isPowerSpotNearby()` - Checks for Power Spot blocks using MSD's `mega_showdown:power_spot` tag
  - `isBlockNearby()` - Generic block proximity scanner (cubic radius search)
  - `hasDynamaxBand()` - Validates Dynamax Band via Accessories API (slots, main hand, offhand)
- New utility class `AccessoriesUtils` for Accessories API integration
- New `DynamaxTags` class for Dynamax Band item tag definition

### Changed
- **Default behavior**: All MSD requirements now enabled by default
- Power Spot check uses Mega Showdown's official `mega_showdown:power_spot` block tag
- Requirements validation executes **before** cooldown check (priority: Band → Power Spot → G-Max Factor → Cooldown)
- Dynamax Band detection upgraded from string matching to Accessories API (equip slot support)

### Technical
- Consolidated package structure (`utils/` folder)
- Fixed import mappings for Fabric Yarn

### Dependencies
- **Mega Showdown 1.6.0+** now required for Power Spot block tag
- **Accessories API 1.1.0-beta.52+1.21.1** added for Dynamax Band equip slot detection

### Notes
- Use `dynamaxAnywhere: true` in config to bypass Power Spot requirement for testing/creative mode

---

## [1.0.0] - Initial Release

### Added
- **Out-of-Battle Dynamax** - Use Dynamax transformation anywhere in the overworld
- **Gigantamax Visual Support** - Full 3D model transformations for all Gigantamax forms (requires Mega Showdown)
- **GUI Integration** - Dynamax button in Cobblemon's interaction wheel with custom icon and translated tooltips
- **Cooldown System** - Configurable per-Pokémon cooldown (default: 60s)
- **Battle Requirement Enforcement** - Blocks Dynamax when Mega Evolved, Primal, or Ultra Burst
- **Configuration System** - JSON config at `config/dynamax-unleashed.json` with customizable messages
- **Client-Server Networking** - `DynamaxPacket` C2S system with server-side validation
- **Multilingual Support** - English and Italian translations

### Technical
- Uses `pokemon.setForcedAspects()` for client-side Gigantamax model sync
- Mixin-free implementation using Cobblemon API hooks
- Modular package structure

### Fixed
- Namespace consistency: assets use `dynamax_unleashed` (underscores)
- Translation keys normalized to `dynamax_unleashed.button.*`
- Gigantamax model sync switched from `FlagSpeciesFeature` to `forcedAspects`

### Known Issues
- Cooldown does not persist across server restarts
- No visual effects when activating Dynamax

---

**Versioning Guide**:
- **MAJOR** (X.0.0) - Incompatible API changes, major rewrites
- **MINOR** (1.X.0) - New features, backwards-compatible additions
- **PATCH** (1.1.X) - Bug fixes, minor improvements

[1.2.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.0
[1.2.1]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.1
[1.2.2]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.2.2
[1.1.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.1.0
[1.0.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.0.0
