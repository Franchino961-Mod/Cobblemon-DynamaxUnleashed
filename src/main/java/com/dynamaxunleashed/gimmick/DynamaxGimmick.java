package com.dynamaxunleashed.gimmick;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.cooldown.CooldownManager;
import com.dynamaxunleashed.config.ModConfig;
import com.dynamaxunleashed.utils.DynamaxUtils;
import com.dynamaxunleashed.utils.PokemonAnimationHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

// NBT key used to store pre-dynamax tradeable state
// so we can restore it correctly on revert

/**
 * Manages Dynamax transformation logic
 * Pattern: Based on MegaShowdown's MegaGimmick class
 */
public class DynamaxGimmick {
    
    private static final String DYNAMAX_TAG = "is_dynamax";
    private static final String TRADEABLE_STATE_TAG = "pre_dynamax_tradeable";
    
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
        
        // Play Dynamax animation if entity exists
        if (pokemon.getEntity() != null) {
            PokemonAnimationHelper.playDynamaxAnimation(pokemon.getEntity());
        }
        
        // Mark as Dynamaxed in persistent data
        pokemon.getPersistentData().putBoolean(DYNAMAX_TAG, true);
        
        // Save tradeable state before changing it, then make untradeable while Dynamaxed
        pokemon.getPersistentData().putBoolean(TRADEABLE_STATE_TAG, pokemon.getTradeable());
        pokemon.setTradeable(false);
        
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
        CooldownManager cooldownManager = DynamaxUnleashed.getCooldownManager();
        
        // Remove "gmax" from forcedAspects if present (check forcedAspects, not all aspects)
        if (pokemon.getForcedAspects().contains("gmax")) {
            Set<String> newAspects = new HashSet<>(pokemon.getForcedAspects());
            newAspects.remove("gmax");
            pokemon.setForcedAspects(newAspects);
        }
        
        // Reset scale
        pokemon.setScaleModifier(1.0f);
        
        // Play revert animation if entity exists
        if (pokemon.getEntity() != null) {
            PokemonAnimationHelper.playUndynamaxAnimation(pokemon.getEntity());
        }
        
        // Remove Dynamax tag
        pokemon.getPersistentData().remove(DYNAMAX_TAG);
        
        // Restore original tradeable state (saved before dynamax activation)
        boolean originalTradeable = pokemon.getPersistentData().getBoolean(TRADEABLE_STATE_TAG);
        pokemon.setTradeable(originalTradeable);
        pokemon.getPersistentData().remove(TRADEABLE_STATE_TAG);
        
        // Start cooldown now (on revert, not on activation)
        cooldownManager.startCooldown(pokemon.getUuid());
        
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
     * Admin-only: Force apply Dynamax, bypassing all requirements (band, power spot, cooldown, form checks).
     * Called from /dynamax <player> <slot> command.
     */
    public static void dynamaxForce(Pokemon pokemon, ServerPlayerEntity player) {
        ModConfig config = DynamaxUnleashed.getConfig();

        boolean hasGmaxForm = hasGigantamaxForm(pokemon);
        if (config.allowGigantamax && hasGmaxForm) {
            Set<String> newAspects = new HashSet<>(pokemon.getForcedAspects());
            newAspects.add("gmax");
            pokemon.setForcedAspects(newAspects);
        }

        pokemon.setScaleModifier((float) config.dynamaxScale);

        if (pokemon.getEntity() != null) {
            PokemonAnimationHelper.playDynamaxAnimation(pokemon.getEntity());
        }

        pokemon.getPersistentData().putBoolean(DYNAMAX_TAG, true);
        pokemon.getPersistentData().putBoolean(TRADEABLE_STATE_TAG, pokemon.getTradeable());
        pokemon.setTradeable(false);

        DynamaxUnleashed.LOGGER.info("[Admin] Force-dynamaxed {} for player {}", pokemon.getSpecies().getName(), player.getName().getString());
    }

    /**
     * Admin-only: Force revert Dynamax, bypassing cooldown.
     * Called from /dynamax <player> <slot> command.
     */
    public static void undynamaxForce(Pokemon pokemon, ServerPlayerEntity player) {
        if (pokemon.getForcedAspects().contains("gmax")) {
            Set<String> newAspects = new HashSet<>(pokemon.getForcedAspects());
            newAspects.remove("gmax");
            pokemon.setForcedAspects(newAspects);
        }

        pokemon.setScaleModifier(1.0f);

        if (pokemon.getEntity() != null) {
            PokemonAnimationHelper.playUndynamaxAnimation(pokemon.getEntity());
        }

        pokemon.getPersistentData().remove(DYNAMAX_TAG);

        boolean originalTradeable = pokemon.getPersistentData().getBoolean(TRADEABLE_STATE_TAG);
        pokemon.setTradeable(originalTradeable);
        pokemon.getPersistentData().remove(TRADEABLE_STATE_TAG);

        DynamaxUnleashed.LOGGER.info("[Admin] Force-reverted {} for player {}", pokemon.getSpecies().getName(), player.getName().getString());
    }
}
