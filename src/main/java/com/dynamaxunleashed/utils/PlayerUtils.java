package com.dynamaxunleashed.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

/**
 * Utility methods for player and Pokémon operations
 * Pattern: Same as MegaShowdown's PlayerUtils
 */
public class PlayerUtils {
    
    /**
     * Get a Pokémon from a player's party by its UUID
     * @param player The player whose party to search
     * @param pokemonId The UUID of the Pokémon to find
     * @return The Pokémon if found, null otherwise
     */
    public static Pokemon getPartyPokemonFromUUID(ServerPlayerEntity player, UUID pokemonId) {
        try {
            PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
            
            // PlayerPartyStore is a Kotlin Iterable, iterate directly
            for (Pokemon partyPokemon : party) {
                if (partyPokemon.getUuid().equals(pokemonId)) {
                    return partyPokemon;
                }
            }
            
            return null;
                
        } catch (Exception e) {
            return null;
        }
    }
}
