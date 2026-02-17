package com.dynamaxunleashed.gimmick;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.cooldown.CooldownManager;
import com.dynamaxunleashed.config.ModConfig;
import com.dynamaxunleashed.utils.DynamaxUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages Dynamax transformation logic
 * Pattern: Based on MegaShowdown's MegaGimmick class
 */
public class DynamaxGimmick {
    
    private static final String DYNAMAX_TAG = "is_dynamax";
    
    /**
     * Toggle Dynamax state for a Pokémon
     * @param pokemon The Pokémon to toggle
     * @param player The player owning the Pokémon
     */
    public static void dynamaxToggle(Pokemon pokemon, ServerPlayerEntity player) {
        ModConfig config = DynamaxUnleashed.getConfig();
        
        // Check if mod is enabled
        if (!config.enabled) {
            return;
        }
        
        // Check if already Dynamaxed
        if (isDynamax(pokemon)) {
            undynamax(pokemon, player);
        } else {
            dynamax(pokemon, player);
        }
    }
    
    /**
     * Apply Dynamax transformation to a Pokémon
     * @param pokemon The Pokémon to Dynamax
     * @param player The player owning the Pokémon
     */
    private static void dynamax(Pokemon pokemon, ServerPlayerEntity player) {
        ModConfig config = DynamaxUnleashed.getConfig();
        CooldownManager cooldownManager = DynamaxUnleashed.getCooldownManager();
        
        // MSD-compatible check: Dynamax Band requirement
        if (config.requireDynamaxBand && !DynamaxUtils.hasDynamaxBand(player)) {
            player.sendMessage(Text.literal(config.messages.noDynamaxBand), false);
            return;
        }
        
        // MSD-compatible check: Power Spot requirement
        if (config.requirePowerSpot && !config.dynamaxAnywhere) {
            if (!DynamaxUtils.isPowerSpotNearby(player, config.powerSpotRange)) {
                String message = config.messages.noPowerSpot.replace("{range}", String.valueOf(config.powerSpotRange));
                player.sendMessage(Text.literal(message), false);
                return;
            }
        }
        
        // Check cooldown
        if (cooldownManager.isOnCooldown(pokemon.getUuid())) {
            int remaining = cooldownManager.getRemainingSeconds(pokemon.getUuid());
            String message = config.messages.cooldownActive.replace("{time}", String.valueOf(remaining));
            player.sendMessage(Text.literal(message), false);
            return;
        }
        
        // Check if Pokémon can Dynamax
        if (!canDynamax(pokemon)) {
            player.sendMessage(Text.literal(config.messages.cannotDynamax), false);
            return;
        }
        
        // Check if has Gigantamax form
        boolean hasGmaxForm = hasGigantamaxForm(pokemon);
        boolean canUseGmax = config.allowGigantamax && hasGmaxForm;
        
        // MSD-compatible check: GmaxFactor requirement for Gigantamax
        if (canUseGmax && config.requireGmaxFactor && !pokemon.getGmaxFactor()) {
            player.sendMessage(Text.literal(config.messages.noGmaxFactor), false);
            return;
        }
        
        if (canUseGmax) {
            // Add "gmax" to forcedAspects (auto-syncs client & triggers model change)
            Set<String> newAspects = new HashSet<>(pokemon.getForcedAspects());
            newAspects.add("gmax");
            pokemon.setForcedAspects(newAspects);
            DynamaxUnleashed.LOGGER.info(
                "{} transformed to Gigantamax (aspect applied via forcedAspects, GmaxFactor: {})",
                pokemon.getSpecies().getName(),
                pokemon.getGmaxFactor()
            );
        }
        
        // Apply scale modifier
        pokemon.setScaleModifier((float) config.dynamaxScale);
        
        // Mark as Dynamaxed in persistent data
        pokemon.getPersistentData().putBoolean(DYNAMAX_TAG, true);
        
        // Make untradeable while Dynamaxed
        pokemon.setTradeable(false);
        
        // Start cooldown
        cooldownManager.startCooldown(pokemon.getUuid());
        
        // Send success message
        String message = config.messages.dynamaxActivated
            .replace("{pokemon}", pokemon.getDisplayName(false).getString());
        player.sendMessage(Text.literal(message), false);
        
        DynamaxUnleashed.LOGGER.info(
            "Player {} activated Dynamax on {}",
            player.getName().getString(),
            pokemon.getSpecies().getName()
        );
    }
    
    /**
     * Revert Pokémon from Dynamax to normal form
     * @param pokemon The Pokémon to revert
     * @param player The player owning the Pokémon
     */
    private static void undynamax(Pokemon pokemon, ServerPlayerEntity player) {
        ModConfig config = DynamaxUnleashed.getConfig();
        
        // Remove "gmax" from forcedAspects if present
        if (pokemon.getAspects().contains("gmax")) {
            Set<String> newAspects = new HashSet<>(pokemon.getForcedAspects());
            newAspects.remove("gmax");
            pokemon.setForcedAspects(newAspects);
        }
        
        // Reset scale
        pokemon.setScaleModifier(1.0f);
        
        // Remove Dynamax tag
        pokemon.getPersistentData().remove(DYNAMAX_TAG);
        
        // Make tradeable again
        pokemon.setTradeable(true);
        
        // Send message
        String message = config.messages.dynamaxReverted
            .replace("{pokemon}", pokemon.getDisplayName(false).getString());
        player.sendMessage(Text.literal(message), false);
        
        DynamaxUnleashed.LOGGER.info(
            "Player {} reverted {} from Dynamax",
            player.getName().getString(),
            pokemon.getSpecies().getName()
        );
    }
    
    /**
     * Check if a Pokémon is currently Dynamaxed
     */
    public static boolean isDynamax(Pokemon pokemon) {
        return pokemon.getPersistentData().getBoolean(DYNAMAX_TAG);
    }
    
    /**
     * Check if a Pokémon can use Dynamax
     */
    public static boolean canDynamax(Pokemon pokemon) {
        ModConfig config = DynamaxUnleashed.getConfig();
        
        if (!config.maintainBattleRequirements) {
            return true;
        }
        
        String formName = pokemon.getForm().getName().toLowerCase();
        
        // Cannot Dynamax if Mega Evolved, Primal, or Ultra Burst
        return !formName.contains("mega") && 
               !formName.contains("primal") && 
               !formName.contains("ultra");
    }
    
    /**
     * Check if Pokémon has a Gigantamax form available
     */
    private static boolean hasGigantamaxForm(Pokemon pokemon) {
        return pokemon.getSpecies().getForms().stream()
            .anyMatch(form -> form.getName().toLowerCase().contains("gmax"));
    }
    
    /**
     * Get the Gigantamax form for a Pokémon
     */
    private static com.cobblemon.mod.common.pokemon.FormData getGigantamaxForm(Pokemon pokemon) {
        return pokemon.getSpecies().getForms().stream()
            .filter(form -> form.getName().toLowerCase().contains("gmax"))
            .findFirst()
            .orElse(null);
    }
}
