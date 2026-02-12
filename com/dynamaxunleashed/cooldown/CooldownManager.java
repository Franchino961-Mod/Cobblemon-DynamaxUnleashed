package com.dynamaxunleashed.cooldown;

import com.dynamaxunleashed.DynamaxUnleashed;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages Dynamax cooldowns for Pokémon
 * Tracks when each Pokémon used Dynamax and enforces the configured cooldown period
 */
public class CooldownManager {
    
    // Map of Pokemon UUID to the timestamp (in milliseconds) when they can Dynamax again
    private final ConcurrentHashMap<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    
    /**
     * Check if a Pokémon is currently on cooldown
     * @param pokemonUuid The unique ID of the Pokémon
     * @return true if on cooldown, false if ready to Dynamax
     */
    public boolean isOnCooldown(UUID pokemonUuid) {
        Long readyTime = cooldowns.get(pokemonUuid);
        if (readyTime == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        
        // If cooldown expired, remove it and return false
        if (currentTime >= readyTime) {
            cooldowns.remove(pokemonUuid);
            return false;
        }
        
        return true;
    }
    
    /**
     * Get remaining cooldown time in seconds
     * @param pokemonUuid The unique ID of the Pokémon
     * @return Remaining seconds, or 0 if no cooldown
     */
    public int getRemainingSeconds(UUID pokemonUuid) {
        Long readyTime = cooldowns.get(pokemonUuid);
        if (readyTime == null) {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        long remainingMs = readyTime - currentTime;
        
        if (remainingMs > 0) {
            return (int) (remainingMs / 1000) + 1; // Round up
        } else {
            cooldowns.remove(pokemonUuid);
            return 0;
        }
    }
    
    /**
     * Start cooldown for a Pokémon after using Dynamax
     * @param pokemonUuid The unique ID of the Pokémon
     */
    public void startCooldown(UUID pokemonUuid) {
        long cooldownMs = DynamaxUnleashed.getConfig().cooldownSeconds * 1000L;
        long readyTime = System.currentTimeMillis() + cooldownMs;
        cooldowns.put(pokemonUuid, readyTime);
        
        DynamaxUnleashed.LOGGER.debug(
            "Started cooldown for Pokémon {}: {}s",
            pokemonUuid, DynamaxUnleashed.getConfig().cooldownSeconds
        );
    }
    
    /**
     * Manually clear cooldown for a Pokémon (admin/debug use)
     * @param pokemonUuid The unique ID of the Pokémon
     */
    public void clearCooldown(UUID pokemonUuid) {
        cooldowns.remove(pokemonUuid);
        DynamaxUnleashed.LOGGER.debug("Cleared cooldown for Pokémon {}", pokemonUuid);
    }
    
    /**
     * Clear all cooldowns (called on server stop)
     */
    public void clearAll() {
        int count = cooldowns.size();
        cooldowns.clear();
        DynamaxUnleashed.LOGGER.info("Cleared all cooldowns ({} entries)", count);
    }
    
    /**
     * Get total number of Pokémon currently on cooldown
     */
    public int getActiveCooldownCount() {
        // Clean up expired cooldowns
        long currentTime = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
        return cooldowns.size();
    }
}
