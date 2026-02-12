package com.dynamaxunleashed

import com.dynamaxunleashed.config.ModConfig
import com.dynamaxunleashed.cooldown.CooldownManager
import com.dynamaxunleashed.handler.PokemonInteractionHandler
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.LoggerFactory

/**
 * Dynamax Unleashed - Main mod entry point
 * Allows Pokémon to Dynamax/Gigantamax outside of battle with configurable cooldown
 */
class DynamaxUnleashed : ModInitializer {
    
    companion object {
        const val MOD_ID = "dynamax_unleashed"
        const val MOD_NAME = "Dynamax Unleashed"
        val LOGGER = LoggerFactory.getLogger(MOD_NAME)
        
        lateinit var config: ModConfig
            private set
        
        val cooldownManager = CooldownManager()
    }
    
    override fun onInitialize() {
        LOGGER.info("Initializing $MOD_NAME...")
        
        // Load configuration
        config = ModConfig.load()
        LOGGER.info("Configuration loaded: Cooldown=${config.cooldownSeconds}s, Scale=${config.dynamaxScale}x")
        
        // Register event handlers
        PokemonInteractionHandler.register()
        
        // Register server lifecycle events
        ServerLifecycleEvents.SERVER_STOPPED.register { _ ->
            cooldownManager.clearAll()
        }
        
        LOGGER.info("$MOD_NAME initialized successfully!")
    }
}
