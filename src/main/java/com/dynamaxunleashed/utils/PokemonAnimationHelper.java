package com.dynamaxunleashed.utils;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.animation.PlayPosableAnimationPacket;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for playing Pokemon animations
 * Pattern: Java version of MegaShowdown's animation system
 */
public class PokemonAnimationHelper {
    
    /**
     * Play a Bedrock poseable animation on a Pokemon entity
     * Sends animation packet to all nearby players
     * 
     * @param pokemon The Pokemon entity to animate
     * @param animations Set of animation names to play (e.g., "dynamax", "revert")
     * @param expressions Optional MoLang expressions to execute with the animation
     */
    public static void playAnimation(Entity pokemon, Set<String> animations, List<String> expressions) {
        PlayPosableAnimationPacket packet = new PlayPosableAnimationPacket(
            pokemon.getId(), 
            animations, 
            expressions
        );
        
        // Send to all players within 128 blocks
        packet.sendToPlayersAround(
            pokemon.getX(),
            pokemon.getY(),
            pokemon.getZ(),
            128.0,
            pokemon.getWorld().getRegistryKey(),
            (player) -> false  // Send to all players (false = don't exclude)
        );
    }
    
    /**
     * Play Dynamax transformation animation
     * Uses Cobblemon's built-in dynamax animation
     * 
     * @param pokemonEntity The Pokemon entity to animate
     */
    public static void playDynamaxAnimation(PokemonEntity pokemonEntity) {
        // Cobblemon's Dynamax animation identifiers
        // These are the same used in battle
        Set<String> animations = new HashSet<>();
        animations.add("dynamax");
        
        playAnimation(pokemonEntity, animations, List.of());
    }
    
    /**
     * Play revert animation when undynamaxing
     * 
     * @param pokemonEntity The Pokemon entity to animate
     */
    public static void playUndynamaxAnimation(PokemonEntity pokemonEntity) {
        // Play a generic cry/idle animation on revert
        Set<String> animations = new HashSet<>();
        animations.add("cry");
        
        playAnimation(pokemonEntity, animations, List.of());
    }
}
