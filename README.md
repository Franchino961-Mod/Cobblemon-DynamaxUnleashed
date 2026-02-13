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
- **Fabric API**: 0.108.0+1.21.1 or higher
- **Cobblemon**: 1.7.1+1.21.1 or higher
- **Mega Showdown**: 1.6.0 or higher **(REQUIRED)**
- **Architectury API**: 13.0.6 or higher
- **Java**: 21 or higher

> ⚠️ **Important**: Mega Showdown is required as it provides the Gigantamax 3D models and textures. Without it, Pokémon will only increase in size without visual model changes.

## 📦 Installation

1. Make sure you have Fabric Loader, Fabric API, Cobblemon, **Mega Showdown**, and Architectury API installed
2. Download the mod `.jar` file
3. Place the file in your Minecraft installation's `mods` folder
4. Launch the game!

> 💡 **Note**: Mega Showdown must be installed for Gigantamax forms to display correctly with custom 3D models. The mod will not load without it.

## 🎮 How to Use

1. **Right-click** on your Pokémon in the world
2. **Open the interaction wheel** - The same GUI used for riding, giving items, etc.
3. **Select the "Dynamax" option** from the wheel
4. Your Pokémon will grow to massive size!
5. Interact again and select "Revert Dynamax" to return to normal size

### Dynamax Conditions

✅ **You can use Dynamax if:**
- The Pokémon is not on cooldown
- The Pokémon meets battle requirements (if enabled in config)
- The mod is enabled in the config

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
  "maintainBattleRequirements": true,
  "messages": {
    "cooldownActive": "§cYour Pokémon is too tired to Dynamax! Wait {time} seconds.",
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
| `maintainBattleRequirements` | Use same requirements as battle | `true` |

### Custom Messages

All messages support color codes (`§`) and placeholders:
- `{time}` - Remaining cooldown seconds
- `{pokemon}` - Pokémon display name

## 🎮 Compatibility

This mod **requires and integrates** with:
- **Cobblemon** - Main dependency for Pokémon mechanics
- **Mega Showdown** - **REQUIRED** - Provides Gigantamax 3D models, textures, and animations
- **Architectury API** - For cross-platform networking

The mod works by utilizing Mega Showdown's Gigantamax assets while adding the functionality to use Dynamax outside of battle with a cooldown system. Without Mega Showdown, Pokémon will only scale in size without changing their 3D models.

## ❓ FAQ

**Q: Is Mega Showdown required?**  
A: Yes! Mega Showdown is a **required dependency**. It provides the Gigantamax 3D models and textures. Without it, the mod will not load.

**Q: What happens if I don't install Mega Showdown?**  
A: The game will not load this mod as Mega Showdown is marked as a required dependency in fabric.mod.json.

**Q: Can I use this in multiplayer?**  
A: Yes! The mod works in both singleplayer and multiplayer. It must be installed on the server.

**Q: Does the Dynamax persist after server restart?**  
A: No, Pokémon will revert to normal size on server restart, but cooldowns are reset.

**Q: Can I change the size of Dynamax Pokémon?**  
A: Yes! Adjust the `dynamaxScale` value in the config. Values higher than 2.0 make them even larger!

**Q: Does this work with modpacks?**  
A: Yes! Feel free to include this mod in your modpack.

## 🔧 Development

### Project Structure

```
dynamax-unleashed/
├── build.gradle                    # Build configuration
├── gradle.properties               # Project properties
├── settings.gradle                 # Gradle settings
├── src/main/
│   ├── java/com/dynamaxunleashed/
│   │   ├── DynamaxUnleashed.java           # Main mod entry
│   │   ├── DynamaxUnleashedClient.java     # Client entry
│   │   ├── config/
│   │   │   └── ModConfig.java              # Configuration
│   │   ├── cooldown/
│   │   │   └── CooldownManager.java        # Cooldown tracking
│   │   ├── gimmick/
│   │   │   └── DynamaxGimmick.java         # Core Dynamax logic
│   │   ├── handler/
│   │   │   └── InteractionGUIHandler.java  # GUI integration
│   │   ├── networking/
│   │   │   ├── DynamaxPacket.java          # C2S packet
│   │   │   ├── DynamaxPacketHandler.java   # Packet handler
│   │   │   └── DynamaxNetworking.java      # Network registration
│   │   └── util/
│   │       └── PlayerUtils.java            # Helper utilities
│   └── resources/
│       ├── fabric.mod.json                 # Mod metadata
│       ├── dynamax-unleashed.mixins.json   # Mixin config
│       └── assets/dynamax-unleashed/
│           ├── lang/
│           │   ├── en_us.json              # English translations
│           │   └── it_it.json              # Italian translations
│           └── textures/gui/
│               └── dynamax_icon.png        # Interaction wheel icon
└── config/
    └── dynamax-unleashed.json              # Default config
```

### Building from Source

**Requirements:**
- JDK 21 or higher
- Git (optional)

**Steps:**
```bash
# Clone or download the repository
git clone <repository-url>
cd dynamax-unleashed

# Build the mod
./gradlew build

# On Windows, use:
.\gradlew.bat build
```

The compiled `.jar` file will be in `build/libs/dynamax-unleashed-1.0.0.jar`

## 📝 Known Issues

- Cooldown persistence between server restarts not yet implemented
- No visual effects or particles when activating Dynamax

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
- Minecraft/Fabric/Cobblemon/Architectury API versions
- Detailed description of the problem
- Crash logs (if applicable)
- Steps to reproduce

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

## 👏 Credits

- **Cobblemon Team** - For the amazing Pokémon mod and comprehensive API
- **Mega Showdown Team** - For the Gigantamax 3D models, textures, animations, and base Dynamax implementation that this mod builds upon
- **Architectury Team** - For the cross-platform networking API
- **Modding Community** - For support and testing

> 📝 **Special Thanks**: This mod utilizes the Gigantamax assets created by the Mega Showdown team. All Gigantamax models, textures, and animations are part of the Mega Showdown mod.

---

**Note**: This is an unofficial fan-made mod. Pokémon is a registered trademark of Nintendo/Game Freak/The Pokémon Company.

**Happy Dynamaxing!** 🔴⚡
