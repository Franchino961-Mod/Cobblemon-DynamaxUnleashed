# Changelog

All notable changes to Dynamax Unleashed will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
  - `hasDynamaxBand()` - Validates Dynamax Band in player inventory using item registry

### Changed
- **Default behavior**: All MSD requirements now enabled by default (was disabled in early development)
- Power Spot check now uses Mega Showdown's official `mega_showdown:power_spot` block tag for compatibility
- Requirements validation now executes **before** cooldown check (priority order: Band → Power Spot → G-Max Factor → Cooldown)
- Improved Dynamax Band detection using proper `Registries.ITEM.getId()` instead of string matching

### Technical
- Added `net.minecraft.registry.Registries` import for proper item ID validation
- Consolidated `util/` and `utils/` packages into single `utils/` folder
- Fixed import mappings for Fabric Yarn (`.util.math.BlockPos` not `.core.BlockPos`)
- Package structure cleaned up for consistency

### Dependencies
- **Mega Showdown 1.6.0+** now provides Power Spot blocks and tags (required for Power Spot checks to work)

### Notes
- Power Spot requirement will only work when Mega Showdown is installed (provides the block tag)
- Dynamax Band detection currently checks main inventory only (Accessories API integration planned)
- Use `dynamaxAnywhere: true` in config to bypass Power Spot requirement for testing/creative mode

---

## [1.0.0] - Initial Release

### Added
- **Out-of-Battle Dynamax** - Use Dynamax transformation anywhere in the overworld, not just in battles
- **Gigantamax Visual Support** - Full 3D model transformations for all Gigantamax forms (requires Mega Showdown)
- **GUI Integration** - Dynamax button in Cobblemon's interaction wheel
  - Custom Dynamax icon with proper rendering
  - Translated tooltips (EN/IT)
  - "Revert Dynamax" option when Pokémon is Dynamaxed
- **Cooldown System** - Configurable cooldown between Dynamax uses
  - Default: 60 seconds
  - Per-Pokémon tracking (UUID-based)
  - Customizable cooldown messages with `{time}` placeholder
- **Battle Requirement Enforcement** - Prevents Dynamax when Pokémon has other gimmicks:
  - Cannot Dynamax while Mega Evolved
  - Cannot Dynamax while in Primal form
  - Cannot Dynamax while in Ultra Burst form
- **Configuration System** - JSON-based config at `config/dynamax-unleashed.json`:
  - `enabled` - Enable/disable mod functionality
  - `cooldownSeconds` - Cooldown duration (default: 60)
  - `dynamaxScale` - Size multiplier (default: 2.0x)
  - `showCooldownMessage` - Display cooldown warnings
  - `allowGigantamax` - Enable/disable Gigantamax forms
  - `maintainBattleRequirements` - Block Dynamax during other gimmicks
  - Customizable messages with color code support (`§`)
- **Client-Server Networking** - Custom packet system for Dynamax requests
  - `DynamaxPacket` - C2S communication
  - Proper server-side validation
  - Client-side GUI updates via `AspectsUpdatePacket`
- **Command System** - Admin commands for Dynamax control:
  - `/dynamax <player> <slot>` - Force Dynamax on specific Pokémon
  - Permission level: 2 (operator)
- **Multilingual Support** - Full translations:
  - English (en_us.json)
  - Italian (it_it.json)

### Technical
- **Aspect System Integration** - Uses `pokemon.setForcedAspects()` to trigger client-side model updates
  - Proper `AspectsUpdatePacket` synchronization for Gigantamax models
  - Aspect handling: `newAspects.add("gmax")` for Gigantamax-capable Pokémon
- **Dependencies**:
  - Minecraft 1.21.1
  - Fabric Loader 0.16.0+
  - Fabric API 0.108.0+1.21.1
  - Cobblemon 1.7.1+1.21.1
  - **Mega Showdown 1.6.0+** (required - provides Gigantamax assets)
  - Architectury API 13.0.6
  - Yarn mappings for Fabric
- **Architecture**:
  - Modular package structure (`config/`, `cooldown/`, `gimmick/`, `handler/`, `networking/`, `util/`)
  - Proper separation of client and server logic
  - Mixin-free implementation (uses Cobblemon API hooks)

### Fixed
- Namespace consistency: All assets use `dynamax_unleashed` (underscores, not hyphens)
- Translation keys: Fixed from `dynamax-unleashed.ui.*` to `dynamax_unleashed.button.*`
- Icon loading: Moved textures from `assets/dynamax-unleashed/` to `assets/dynamax_unleashed/`
- Visual model updates: Switched from `FlagSpeciesFeature` to `forcedAspects` for proper client sync

### Known Issues
- Cooldown data does not persist across server restarts
- No visual effects (particles/animations) when activating Dynamax
- Dynamax Band detection does not check Accessories slots (only main inventory)

### Credits
- **Cobblemon Team** - Pokémon mod framework and comprehensive API
- **Mega Showdown Team** - Gigantamax 3D models, textures, animations, and aspect system patterns
- **Architectury Team** - Cross-platform networking API
- **Community** - Testing and feedback

---

**Versioning Guide**:
- **MAJOR** (X.0.0) - Incompatible API changes, major rewrites
- **MINOR** (1.X.0) - New features, backwards-compatible additions
- **PATCH** (1.1.X) - Bug fixes, minor improvements

[1.1.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.1.0
[1.0.0]: https://github.com/Franchino961-Mod/Cobblemon-DynamaxUnleashed/releases/tag/v1.0.0
