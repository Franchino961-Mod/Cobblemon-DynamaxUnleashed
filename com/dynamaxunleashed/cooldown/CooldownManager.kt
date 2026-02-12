package com.dynamaxunleashed.cooldown

import com.dynamaxunleashed.DynamaxUnleashed
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Manages Dynamax cooldowns for Pokémon
 * Tracks when each Pokémon used Dynamax and enforces the configured cooldown period
 */
class CooldownManager {
    
    // Map of Pokemon UUID to the timestamp (in milliseconds) when they can Dynamax again
    private val cooldowns = ConcurrentHashMap<UUID, Long>()
    
    /**
     * Check if a Pokémon is currently on cooldown
     * @param pokemonUuid The unique ID of the Pokémon
     * @return true if on cooldown, false if ready to Dynamax
     */
    fun isOnCooldown(pokemonUuid: UUID): Boolean {
        val readyTime = cooldowns[pokemonUuid] ?: return false
        val currentTime = System.currentTimeMillis()
        
        // If cooldown expired, remove it and return false
        if (currentTime >= readyTime) {
            cooldowns.remove(pokemonUuid)
            return false
        }
        
        return true
    }
    
    /**
     * Get remaining cooldown time in seconds
     * @param pokemonUuid The unique ID of the Pokémon
     * @return Remaining seconds, or 0 if no cooldown
     */
    fun getRemainingSeconds(pokemonUuid: UUID): Int {
        val readyTime = cooldowns[pokemonUuid] ?: return 0
        val currentTime = System.currentTimeMillis()
        val remainingMs = readyTime - currentTime
        
        return if (remainingMs > 0) {
            (remainingMs / 1000).toInt() + 1 // Round up
        } else {
            cooldowns.remove(pokemonUuid)
            0
        }
    }
    
    /**
     * Start cooldown for a Pokémon after using Dynamax
     * @param pokemonUuid The unique ID of the Pokémon
     */
    fun startCooldown(pokemonUuid: UUID) {
        val cooldownMs = DynamaxUnleashed.config.cooldownSeconds * 1000L
        val readyTime = System.currentTimeMillis() + cooldownMs
        cooldowns[pokemonUuid] = readyTime
        
        DynamaxUnleashed.LOGGER.debug(
            "Started cooldown for Pokémon $pokemonUuid: ${DynamaxUnleashed.config.cooldownSeconds}s"
        )
    }
    
    /**
     * Manually clear cooldown for a Pokémon (admin/debug use)
     * @param pokemonUuid The unique ID of the Pokémon
     */
    fun clearCooldown(pokemonUuid: UUID) {
        cooldowns.remove(pokemonUuid)
        DynamaxUnleashed.LOGGER.debug("Cleared cooldown for Pokémon $pokemonUuid")
    }
    
    /**
     * Clear all cooldowns (called on server stop)
     */
    fun clearAll() {
        val count = cooldowns.size
        cooldowns.clear()
        DynamaxUnleashed.LOGGER.info("Cleared all cooldowns ($count entries)")
    }
    
    /**
     * Get total number of Pokémon currently on cooldown
     */
    fun getActiveCooldownCount(): Int {
        // Clean up expired cooldowns
        val currentTime = System.currentTimeMillis()
        cooldowns.entries.removeIf { it.value <= currentTime }
        return cooldowns.size
    }
}
