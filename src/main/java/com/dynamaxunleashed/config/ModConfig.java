package com.dynamaxunleashed.config;

import com.dynamaxunleashed.DynamaxUnleashed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Configuration data class for Dynamax Unleashed
 */
public class ModConfig {
    
    // General settings
    public boolean enabled = true;
    public int cooldownSeconds = 60;
    public double dynamaxScale = 2.0;
    public boolean showCooldownMessage = true;
    public boolean allowGigantamax = true;
    public boolean maintainBattleRequirements = true;
    
    // MSD-compatible requirements (v1.1.0+)
    public boolean requireDynamaxBand = true;         // Require Dynamax Band in inventory
    public boolean requirePowerSpot = true;           // Require Power Spot block nearby
    public int powerSpotRange = 20;                   // Range to search for Power Spot
    public boolean dynamaxAnywhere = false;           // Bypass Power Spot requirement
    public boolean requireGmaxFactor = true;          // Require GmaxFactor for Gigantamax
    
    public Messages messages = new Messages();
    
    public static class Messages {
        public String cooldownActive = "§cYour Pokémon is too tired to Dynamax! Wait {time} seconds.";
        public String cannotDynamax = "§cThis Pokémon cannot Dynamax! (Cannot Dynamax if Mega Evolved, Primal, or Ultra Burst)";
        public String dynamaxActivated = "§b{pokemon} has Dynamaxed!";
        public String dynamaxReverted = "§e{pokemon} returned to normal size.";
        public String noDynamaxBand = "§cYou need a Dynamax Band to use Dynamax! (Equip it in Accessories slots)";
        public String noPowerSpot = "§cYou must be near a Power Spot to Dynamax! (Range: {range} blocks)";
        public String noGmaxFactor = "§cThis Pokémon cannot Gigantamax! (Missing G-Max Factor)";
        public String pokemonNotFound = "§cPokémon not found in your party!";
    }
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = FabricLoader.getInstance()
        .getConfigDir()
        .resolve("dynamax-unleashed.json")
        .toFile();
    
    /**
     * Load configuration from file or create default if not exists
     */
    public static ModConfig load() {
        try {
            if (!configFile.exists()) {
                ModConfig defaultConfig = new ModConfig();
                save(defaultConfig);
                DynamaxUnleashed.LOGGER.info("Created default configuration file at {}", 
                    configFile.getAbsolutePath());
                return defaultConfig;
            } else {
                try (FileReader reader = new FileReader(configFile)) {
                    ModConfig config = gson.fromJson(reader, ModConfig.class);
                    if (config == null) {
                        DynamaxUnleashed.LOGGER.warn("Config file was empty or malformed, using defaults.");
                        config = new ModConfig();
                    }
                    // Validate values and clamp to sane ranges
                    config.validate();
                    // Re-save to add any new fields from this mod version
                    save(config);
                    DynamaxUnleashed.LOGGER.info("Loaded configuration from {}", 
                        configFile.getAbsolutePath());
                    return config;
                }
            }
        } catch (Exception e) {
            DynamaxUnleashed.LOGGER.error("Failed to load configuration, using defaults", e);
            return new ModConfig();
        }
    }
    
    /**
     * Validate config values and clamp to sane ranges.
     * Resets invalid values to defaults.
     */
    public void validate() {
        if (cooldownSeconds < 0) {
            DynamaxUnleashed.LOGGER.warn("Config: cooldownSeconds ({}) is negative, resetting to default (60).", cooldownSeconds);
            cooldownSeconds = 60;
        }
        if (dynamaxScale <= 0) {
            DynamaxUnleashed.LOGGER.warn("Config: dynamaxScale ({}) must be > 0, resetting to default (2.0).", dynamaxScale);
            dynamaxScale = 2.0;
        }
        if (powerSpotRange <= 0 || powerSpotRange > 256) {
            DynamaxUnleashed.LOGGER.warn("Config: powerSpotRange ({}) must be between 1-256, resetting to default (20).", powerSpotRange);
            powerSpotRange = 20;
        }
        if (messages == null) {
            DynamaxUnleashed.LOGGER.warn("Config: messages section is missing, using defaults.");
            messages = new Messages();
        }
    }
    
    /**
     * Save configuration to file
     */
    private static void save(ModConfig config) {
        try {
            File parentDir = configFile.getParentFile();
            if (parentDir != null) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(config, writer);
            }
        } catch (Exception e) {
            DynamaxUnleashed.LOGGER.error("Failed to save configuration", e);
        }
    }
}
