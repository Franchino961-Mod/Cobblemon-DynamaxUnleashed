package com.dynamaxunleashed;

import com.dynamaxunleashed.command.DynamaxCommand;
import com.dynamaxunleashed.config.ModConfig;
import com.dynamaxunleashed.cooldown.CooldownManager;
import com.dynamaxunleashed.networking.DynamaxNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamax Unleashed - Main mod entry point
 * Allows Pokémon to Dynamax/Gigantamax outside of battle with configurable cooldown
 */
public class DynamaxUnleashed implements ModInitializer {
    
    public static final String MOD_ID = "dynamax_unleashed";
    public static final String MOD_NAME = "Dynamax Unleashed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    
    private static ModConfig config;
    private static final CooldownManager cooldownManager = new CooldownManager();
    
    public static ModConfig getConfig() {
        return config;
    }

    public static void setConfig(ModConfig newConfig) {
        config = newConfig;
    }
    
    public static CooldownManager getCooldownManager() {
        return cooldownManager;
    }
    
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {}...", MOD_NAME);
        
        // Load configuration
        config = ModConfig.load();
        LOGGER.info("Configuration loaded: Cooldown={}s, Scale={}x", 
            config.cooldownSeconds, config.dynamaxScale);
        
        // Register networking
        DynamaxNetworking.register();
        
        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DynamaxCommand.register(dispatcher);
        });
        
        // Register server lifecycle events
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            cooldownManager.clearAll();
        });
        
        LOGGER.info("{} initialized successfully!", MOD_NAME);
    }
}
