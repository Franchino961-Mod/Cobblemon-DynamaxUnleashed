# Dynamax Unleashed

A Cobblemon addon mod that allows Pokémon to Dynamax and Gigantamax outside of battle with a configurable cooldown system!

[![en](https://img.shields.io/badge/lang-en-red.svg)](README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](README.it.md)

## 📋 Description

This mod extends Cobblemon's Dynamax feature beyond battles, allowing your Pokémon to transform into their giant Dynamax or Gigantamax forms in the overworld. Perfect for showcasing your favorite Pokémon or just having fun with their massive forms!

## ✨ Features

- **🔴 Overworld Dynamax**: Use Dynamax anywhere, not just in battle
- **⏱️ Cooldown System**: Configurable cooldown between uses (default: 60 seconds)
- **📏 Configurable Scale**: Adjust Pokémon size when Dynamaxed (default: 2.0x)
- **🎨 Gigantamax Support**: All Gigantamax forms are supported
- **🛡️ Battle Requirements**: Maintains the same limitations as battle Dynamax
- **🌍 Multilingual**: English and Italian translations included
- **⚙️ Fully Configurable**: Customize every aspect via config file

## 📋 Requirements

- **Minecraft**: 1.21.1
- **Fabric Loader**: 0.16.0 or higher
- **Fabric API**: Latest version
- **Cobblemon**: 1.7.0 or higher
- **Mega Showdown**: 1.6.0 or higher
- **Java**: 21 or higher

## 📦 Installation

1. Make sure you have Fabric Loader, Fabric API, Cobblemon, and Mega Showdown installed
2. Download the mod `.jar` file
3. Place the file in your Minecraft installation's `mods` folder
4. Launch the game!

## 🎮 How to Use

1. **Obtain a Dynamax Band** - Required by default (from Mega Showdown mod)
2. **Interact with your Pokémon** in the world
3. **Select "Dynamax"** from the interaction menu (similar to Mega Evolution)
4. Your Pokémon will grow to massive size!
5. Click again to revert to normal size

### Dynamax Conditions

✅ **You can use Dynamax if:**
- You have a Dynamax Band (if required in config)
- The Pokémon is not on cooldown
- The Pokémon meets battle requirements

❌ **You cannot use Dynamax if:**
- The Pokémon is Mega Evolved
- The Pokémon is in Primal form
- The Pokémon is in Ultra Burst form
- The Pokémon is currently on cooldown

## ⚙️ Configuration

The configuration file is located at `config/dynamax-unleashed.json`:

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
    "cooldownActive": "§cYour Pokémon is too tired to Dynamax! Wait {time} seconds.",
    "noDynamaxBand": "§cYou need a Dynamax Band to use Dynamax outside of battle!",
    "cannotDynamax": "§cThis Pokémon cannot Dynamax!",
    "dynamaxActivated": "§b{pokemon} has Dynamaxed!",
    "dynamaxReverted": "§e{pokemon} returned to normal size."
  }
}
```

### Configuration Options

| Option | Description | Default |
|--------|-------------|---------|
| `enabled` | Enable/disable the mod | `true` |
| `cooldownSeconds` | Cooldown time in seconds | `60` |
| `dynamaxScale` | Size multiplier (1.0 = normal) | `2.0` |
| `showCooldownMessage` | Show cooldown messages to players | `true` |
| `allowGigantamax` | Allow Gigantamax forms | `true` |
| `requireDynamaxBand` | Require Dynamax Band item | `true` |
| `maintainBattleRequirements` | Use same requirements as battle | `true` |

### Custom Messages

All messages support color codes (`§`) and placeholders:
- `{time}` - Remaining cooldown seconds
- `{pokemon}` - Pokémon display name

## 🎮 Compatibility

This mod integrates seamlessly with:
- **Cobblemon** - Main dependency for Pokémon mechanics
- **Mega Showdown** - Provides Dynamax Band and battle Dynamax features

The mod is designed to work alongside other Cobblemon addons without conflicts.

## ❓ FAQ

**Q: Do I need Mega Showdown installed?**  
A: Yes, Mega Showdown is required as it provides the Dynamax Band and base Dynamax mechanics.

**Q: Can I use this in multiplayer?**  
A: Yes! The mod works in both singleplayer and multiplayer. It must be installed on the server.

**Q: Does the Dynamax persist after server restart?**  
A: No, Pokémon will revert to normal size on server restart, but cooldowns are reset.

**Q: Can I change the size of Dynamax Pokémon?**  
A: Yes! Adjust the `dynamaxScale` value in the config. Values higher than 2.0 make them even larger!

**Q: Can I disable the Dynamax Band requirement?**  
A: Yes, set `requireDynamaxBand` to `false` in the config.

**Q: Does this work with modpacks?**  
A: Yes! Feel free to include this mod in your modpack.

## 🔧 Development

### Project Structure

```
dynamax-unleashed/
├── build.gradle                    # Build configuration
├── fabric.mod.json                 # Mod metadata
├── dynamax-unleashed.mixins.json   # Mixin configuration
├── config/
│   └── dynamax-unleashed.json      # Default config
├── com/dynamaxunleashed/
│   ├── DynamaxUnleashed.java       # Entry point
│   ├── config/
│   │   └── ModConfig.java          # Configuration management
│   ├── cooldown/
│   │   └── CooldownManager.java    # Cooldown system
│   ├── handler/
│   │   └── PokemonInteractionHandler.java  # Interaction handler
│   └── mixin/
│       └── DynamaxRequestMixin.java  # Battle restriction bypass
└── assets/dynamax_unleashed/lang/
    ├── en_us.json                  # English translations
    └── it_it.json                  # Italian translations
```

### Building from Source

```bash
./gradlew build
```

The compiled `.jar` file will be in `build/libs/`

## 📝 Known Issues

- GUI button integration with Cobblemon interaction screen (requires newer API)
- Cooldown persistence between server restarts

## 🚀 Planned Features

- [ ] Admin command `/dynamax clear <player/pokemon>` to reset cooldowns
- [ ] Visual particles when activating Dynamax
- [ ] Custom sound effects
- [ ] ModMenu integration for in-game config GUI
- [ ] Permission system for multiplayer servers

## 📝 License

This mod is released under the MIT license. Feel free to include it in your modpacks!

## 🐛 Bug Reports

If you encounter issues or bugs, please report them with:
- Mod version
- Minecraft/Fabric/Cobblemon/Mega Showdown versions
- Detailed description of the problem
- Crash logs (if applicable)

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

## 👏 Credits

- **Cobblemon Team** - For the amazing Pokémon mod
- **Mega Showdown Team** - For the base Dynamax implementation
- **Modding Community** - For support and testing

---

**Note**: This is an unofficial fan-made mod. Pokémon is a registered trademark of Nintendo/Game Freak/The Pokémon Company.

**Happy Dynamaxing!** 🔴⚡
