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
    
    public boolean enabled = true;
    public int cooldownSeconds = 60;
    public double dynamaxScale = 2.0;
    public boolean showCooldownMessage = true;
    public boolean allowGigantamax = true;
    public boolean maintainBattleRequirements = true;
    public Messages messages = new Messages();
    
    public static class Messages {
        public String cooldownActive = "§cYour Pokémon is too tired to Dynamax! Wait {time} seconds.";
        public String cannotDynamax = "§cThis Pokémon cannot Dynamax!";
        public String dynamaxActivated = "§b{pokemon} has Dynamaxed!";
        public String dynamaxReverted = "§e{pokemon} returned to normal size.";
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
