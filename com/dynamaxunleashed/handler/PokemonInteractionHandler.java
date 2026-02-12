package com.dynamaxunleashed.handler;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.config.ModConfig;
import com.dynamaxunleashed.cooldown.CooldownManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

/**
 * Handles Pokémon interaction events to add Dynamax button to the GUI
 * Similar to how Mega Evolution works in the Pokémon interaction screen
 */
public class PokemonInteractionHandler {
    
    /**
     * Register event handlers for Pokémon interactions
     */
    public static void register() {
        // This will be called when a player interacts with their Pokémon
        // We'll add a custom interaction option for Dynamax
        CobblemonEvents.POKEMON_INTERACTION.subscribe(event -> {
            handlePokemonInteraction(event.getPlayer(), event.getPokemon());
            return null;
        });
        
        DynamaxUnleashed.LOGGER.info("Registered Pokémon interaction handlers");
    }
    
    /**
     * Handle when a player interacts with their Pokémon
     * This adds the Dynamax/Gigantamax option to the interaction menu
     */
    private static void handlePokemonInteraction(ServerPlayerEntity player, Pokemon pokemon) {
        // Only proceed if overworld Dynamax is enabled
        if (!DynamaxUnleashed.getConfig().enabled) return;
        
        // Check if player has Dynamax Band (if required)
        if (DynamaxUnleashed.getConfig().requireDynamaxBand && !hasDynamaxBand(player)) {
            return;
        }
        
        // Check if Pokémon can Dynamax
        if (!canPokemonDynamax(pokemon)) {
            return;
        }
        
        // Add interaction option (this will appear in the GUI)
        // Note: The actual GUI integration requires Cobblemon API hooks
        // This is a simplified version - full implementation needs Cobblemon's interaction system
    }
    
    /**
     * Attempt to toggle Dynamax for a Pokémon
     * Called when player clicks the Dynamax button in the GUI
     */
    public static boolean attemptDynamax(ServerPlayerEntity player, Pokemon pokemon) {
        ModConfig config = DynamaxUnleashed.getConfig();
        CooldownManager cooldownManager = DynamaxUnleashed.getCooldownManager();
        
        // Check if already Dynamaxed (revert to normal)
        String formName = pokemon.getForm().getName();
        if (formName.toLowerCase().contains("gmax") || pokemon.getScaleModifier() > 1.5f) {
            revertDynamax(player, pokemon);
            return true;
        }
        
        // Check cooldown
        UUID pokemonUuid = pokemon.getUuid();
        if (cooldownManager.isOnCooldown(pokemonUuid)) {
            int remaining = cooldownManager.getRemainingSeconds(pokemonUuid);
            String message = config.messages.cooldownActive.replace("{time}", String.valueOf(remaining));
            player.sendMessage(Text.literal(message), false);
            return false;
        }
        
        // Check if player has Dynamax Band
        if (config.requireDynamaxBand && !hasDynamaxBand(player)) {
            player.sendMessage(Text.literal(config.messages.noDynamaxBand), false);
            return false;
        }
        
        // Check if Pokémon can Dynamax (same requirements as battle)
        if (!canPokemonDynamax(pokemon)) {
            player.sendMessage(Text.literal(config.messages.cannotDynamax), false);
            return false;
        }
        
        // Apply Dynamax transformation
        applyDynamax(player, pokemon);
        
        // Start cooldown
        cooldownManager.startCooldown(pokemonUuid);
        
        return true;
    }
    
    /**
     * Apply Dynamax transformation to a Pokémon
     */
    private static void applyDynamax(ServerPlayerEntity player, Pokemon pokemon) {
        ModConfig config = DynamaxUnleashed.getConfig();
        
        // Check if Pokémon has Gigantamax form
        boolean hasGigantamax = config.allowGigantamax && hasGigantamaxForm(pokemon);
        
        if (hasGigantamax) {
            // Transform to Gigantamax form
            var gmaxForm = getGigantamaxForm(pokemon);
            if (gmaxForm != null) {
                pokemon.setForm(gmaxForm);
                DynamaxUnleashed.LOGGER.info("{} transformed to Gigantamax form: {}", 
                    pokemon.getSpecies().getName(), gmaxForm.getName());
            }
        }
        
        // Apply scale increase
        pokemon.setScaleModifier((float) config.dynamaxScale);
        
        // Send success message
        String message = config.messages.dynamaxActivated.replace("{pokemon}", 
            pokemon.getDisplayName().getString());
        player.sendMessage(Text.literal(message), false);
        
        DynamaxUnleashed.LOGGER.info("Player {} activated Dynamax on {}", 
            player.getName().getString(), pokemon.getSpecies().getName());
    }
    
    /**
     * Revert Pokémon from Dynamax to normal form
     */
    private static void revertDynamax(ServerPlayerEntity player, Pokemon pokemon) {
        ModConfig config = DynamaxUnleashed.getConfig();
        
        // Revert to base form if currently in Gigantamax
        String formName = pokemon.getForm().getName();
        if (formName.toLowerCase().contains("gmax")) {
            var baseForm = pokemon.getSpecies().getStandardForm();
            pokemon.setForm(baseForm);
        }
        
        // Reset scale
        pokemon.setScaleModifier(1.0f);
        
        // Send message
        String message = config.messages.dynamaxReverted.replace("{pokemon}", 
            pokemon.getDisplayName().getString());
        player.sendMessage(Text.literal(message), false);
        
        DynamaxUnleashed.LOGGER.info("Player {} reverted {} from Dynamax", 
            player.getName().getString(), pokemon.getSpecies().getName());
    }
    
    /**
     * Check if player has Dynamax Band in inventory
     */
    private static boolean hasDynamaxBand(ServerPlayerEntity player) {
        // Search for Dynamax Band item in player inventory
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.size(); slot++) {
            var stack = inventory.getStack(slot);
            // Check if item is Dynamax Band (from Mega Showdown mod)
            if (stack.getItem().toString().toLowerCase().contains("dynamax_band")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if Pokémon can Dynamax (same requirements as battle)
     * - Cannot be Mega Evolved
     * - Cannot be Primal
     * - Cannot be Ultra Burst
     * - Cannot have cannotDynamax flag
     */
    private static boolean canPokemonDynamax(Pokemon pokemon) {
        // Check if maintaining battle requirements
        if (!DynamaxUnleashed.getConfig().maintainBattleRequirements) {
            return true; // Allow any Pokémon if requirements disabled
        }
        
        String formName = pokemon.getForm().getName().toLowerCase();
        
        // Cannot Dynamax if Mega Evolved
        if (formName.contains("mega")) {
            return false;
        }
        
        // Cannot Dynamax if Primal
        if (formName.contains("primal")) {
            return false;
        }
        
        // Cannot Dynamax if Ultra Burst
        if (formName.contains("ultra")) {
            return false;
        }
        
        // Check species-specific cannotDynamax flag (if exists in species data)
        // This would need access to the species JSON data
        
        return true;
    }
    
    /**
     * Check if Pokémon has a Gigantamax form available
     */
    private static boolean hasGigantamaxForm(Pokemon pokemon) {
        var species = pokemon.getSpecies();
        return species.getForms().stream()
            .anyMatch(form -> form.getName().toLowerCase().contains("gmax"));
    }
    
    /**
     * Get the Gigantamax form for a Pokémon species
     */
    private static com.cobblemon.mod.common.api.pokemon.FormData getGigantamaxForm(Pokemon pokemon) {
        return pokemon.getSpecies().getForms().stream()
            .filter(form -> form.getName().toLowerCase().contains("gmax"))
            .findFirst()
            .orElse(null);
    }
}
