package com.dynamaxunleashed.config

import com.dynamaxunleashed.DynamaxUnleashed
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Configuration data class for Dynamax Unleashed
 */
data class ModConfig(
    val enabled: Boolean = true,
    val cooldownSeconds: Int = 60,
    val dynamaxScale: Double = 2.0,
    val showCooldownMessage: Boolean = true,
    val allowGigantamax: Boolean = true,
    val requireDynamaxBand: Boolean = true,
    val maintainBattleRequirements: Boolean = true,
    val messages: Messages = Messages()
) {
    
    data class Messages(
        val cooldownActive: String = "§cYour Pokémon is too tired to Dynamax! Wait {time} seconds.",
        val noDynamaxBand: String = "§cYou need a Dynamax Band to use Dynamax outside of battle!",
        val cannotDynamax: String = "§cThis Pokémon cannot Dynamax!",
        val dynamaxActivated: String = "§b{pokemon} has Dynamaxed!",
        val dynamaxReverted: String = "§e{pokemon} returned to normal size."
    )
    
    companion object {
        private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        private val configFile: File = FabricLoader.getInstance()
            .configDir
            .resolve("dynamax-unleashed.json")
            .toFile()
        
        /**
         * Load configuration from file or create default if not exists
         */
        fun load(): ModConfig {
            return try {
                if (!configFile.exists()) {
                    val default = ModConfig()
                    save(default)
                    DynamaxUnleashed.LOGGER.info("Created default configuration file at ${configFile.absolutePath}")
                    default
                } else {
                    FileReader(configFile).use { reader ->
                        val config = gson.fromJson(reader, ModConfig::class.java)
                        DynamaxUnleashed.LOGGER.info("Loaded configuration from ${configFile.absolutePath}")
                        config
                    }
                }
            } catch (e: Exception) {
                DynamaxUnleashed.LOGGER.error("Failed to load configuration, using defaults", e)
                ModConfig()
            }
        }
        
        /**
         * Save configuration to file
         */
        private fun save(config: ModConfig) {
            try {
                configFile.parentFile?.mkdirs()
                FileWriter(configFile).use { writer ->
                    gson.toJson(config, writer)
                }
            } catch (e: Exception) {
                DynamaxUnleashed.LOGGER.error("Failed to save configuration", e)
            }
        }
    }
}
