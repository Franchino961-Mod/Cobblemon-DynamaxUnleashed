package com.dynamaxunleashed.handler

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.pokemon.Pokemon
import com.dynamaxunleashed.DynamaxUnleashed
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

/**
 * Handles Pokémon interaction events to add Dynamax button to the GUI
 * Similar to how Mega Evolution works in the Pokémon interaction screen
 */
object PokemonInteractionHandler {
    
    /**
     * Register event handlers for Pokémon interactions
     */
    fun register() {
        // This will be called when a player interacts with their Pokémon
        // We'll add a custom interaction option for Dynamax
        CobblemonEvents.POKEMON_INTERACTION.subscribe { event ->
            handlePokemonInteraction(event.player, event.pokemon)
        }
        
        DynamaxUnleashed.LOGGER.info("Registered Pokémon interaction handlers")
    }
    
    /**
     * Handle when a player interacts with their Pokémon
     * This adds the Dynamax/Gigantamax option to the interaction menu
     */
    private fun handlePokemonInteraction(player: ServerPlayerEntity, pokemon: Pokemon) {
        // Only proceed if overworld Dynamax is enabled
        if (!DynamaxUnleashed.config.enabled) return
        
        // Check if player has Dynamax Band (if required)
        if (DynamaxUnleashed.config.requireDynamaxBand && !hasDynamaxBand(player)) {
            return
        }
        
        // Check if Pokémon can Dynamax
        if (!canPokemonDynamax(pokemon)) {
            return
        }
        
        // Add interaction option (this will appear in the GUI)
        // Note: The actual GUI integration requires Cobblemon API hooks
        // This is a simplified version - full implementation needs Cobblemon's interaction system
    }
    
    /**
     * Attempt to toggle Dynamax for a Pokémon
     * Called when player clicks the Dynamax button in the GUI
     */
    fun attemptDynamax(player: ServerPlayerEntity, pokemon: Pokemon): Boolean {
        val config = DynamaxUnleashed.config
        val cooldownManager = DynamaxUnleashed.cooldownManager
        
        // Check if already Dynamaxed (revert to normal)
        if (pokemon.form.name.contains("gmax", ignoreCase = true) || 
            pokemon.scaleModifier > 1.5f) {
            revertDynamax(player, pokemon)
            return true
        }
        
        // Check cooldown
        val pokemonUuid = pokemon.uuid
        if (cooldownManager.isOnCooldown(pokemonUuid)) {
            val remaining = cooldownManager.getRemainingSeconds(pokemonUuid)
            val message = config.messages.cooldownActive.replace("{time}", remaining.toString())
            player.sendMessage(Text.literal(message), false)
            return false
        }
        
        // Check if player has Dynamax Band
        if (config.requireDynamaxBand && !hasDynamaxBand(player)) {
            player.sendMessage(Text.literal(config.messages.noDynamaxBand), false)
            return false
        }
        
        // Check if Pokémon can Dynamax (same requirements as battle)
        if (!canPokemonDynamax(pokemon)) {
            player.sendMessage(Text.literal(config.messages.cannotDynamax), false)
            return false
        }
        
        // Apply Dynamax transformation
        applyDynamax(player, pokemon)
        
        // Start cooldown
        cooldownManager.startCooldown(pokemonUuid)
        
        return true
    }
    
    /**
     * Apply Dynamax transformation to a Pokémon
     */
    private fun applyDynamax(player: ServerPlayerEntity, pokemon: Pokemon) {
        val config = DynamaxUnleashed.config
        
        // Check if Pokémon has Gigantamax form
        val hasGigantamax = config.allowGigantamax && hasGigantamaxForm(pokemon)
        
        if (hasGigantamax) {
            // Transform to Gigantamax form
            val gmaxForm = getGigantamaxForm(pokemon)
            if (gmaxForm != null) {
                pokemon.form = gmaxForm
                DynamaxUnleashed.LOGGER.info("${pokemon.species.name} transformed to Gigantamax form: ${gmaxForm.name}")
            }
        }
        
        // Apply scale increase
        pokemon.scaleModifier = config.dynamaxScale.toFloat()
        
        // Send success message
        val message = config.messages.dynamaxActivated.replace("{pokemon}", pokemon.getDisplayName().string)
        player.sendMessage(Text.literal(message), false)
        
        DynamaxUnleashed.LOGGER.info("Player ${player.name.string} activated Dynamax on ${pokemon.species.name}")
    }
    
    /**
     * Revert Pokémon from Dynamax to normal form
     */
    private fun revertDynamax(player: ServerPlayerEntity, pokemon: Pokemon) {
        val config = DynamaxUnleashed.config
        
        // Revert to base form if currently in Gigantamax
        if (pokemon.form.name.contains("gmax", ignoreCase = true)) {
            val baseForm = pokemon.species.standardForm
            pokemon.form = baseForm
        }
        
        // Reset scale
        pokemon.scaleModifier = 1.0f
        
        // Send message
        val message = config.messages.dynamaxReverted.replace("{pokemon}", pokemon.getDisplayName().string)
        player.sendMessage(Text.literal(message), false)
        
        DynamaxUnleashed.LOGGER.info("Player ${player.name.string} reverted ${pokemon.species.name} from Dynamax")
    }
    
    /**
     * Check if player has Dynamax Band in inventory
     */
    private fun hasDynamaxBand(player: ServerPlayerEntity): Boolean {
        // Search for Dynamax Band item in player inventory
        val inventory = player.inventory
        for (slot in 0 until inventory.size()) {
            val stack = inventory.getStack(slot)
            // Check if item is Dynamax Band (from Mega Showdown mod)
            if (stack.item.toString().contains("dynamax_band", ignoreCase = true)) {
                return true
            }
        }
        return false
    }
    
    /**
     * Check if Pokémon can Dynamax (same requirements as battle)
     * - Cannot be Mega Evolved
     * - Cannot be Primal
     * - Cannot be Ultra Burst
     * - Cannot have cannotDynamax flag
     */
    private fun canPokemonDynamax(pokemon: Pokemon): Boolean {
        // Check if maintaining battle requirements
        if (!DynamaxUnleashed.config.maintainBattleRequirements) {
            return true // Allow any Pokémon if requirements disabled
        }
        
        val formName = pokemon.form.name.lowercase()
        
        // Cannot Dynamax if Mega Evolved
        if (formName.contains("mega")) {
            return false
        }
        
        // Cannot Dynamax if Primal
        if (formName.contains("primal")) {
            return false
        }
        
        // Cannot Dynamax if Ultra Burst
        if (formName.contains("ultra")) {
            return false
        }
        
        // Check species-specific cannotDynamax flag (if exists in species data)
        // This would need access to the species JSON data
        
        return true
    }
    
    /**
     * Check if Pokémon has a Gigantamax form available
     */
    private fun hasGigantamaxForm(pokemon: Pokemon): Boolean {
        val species = pokemon.species
        return species.forms.any { it.name.contains("gmax", ignoreCase = true) }
    }
    
    /**
     * Get the Gigantamax form for a Pokémon species
     */
    private fun getGigantamaxForm(pokemon: Pokemon) =
        pokemon.species.forms.firstOrNull { it.name.contains("gmax", ignoreCase = true) }
}
