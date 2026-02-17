package com.dynamaxunleashed.networking;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.gimmick.DynamaxGimmick;
import com.dynamaxunleashed.utils.PlayerUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Handles incoming Dynamax toggle packets from clients
 * Pattern: Based on MegaShowdown's MegaEvoHandler
 */
public class DynamaxPacketHandler {
    
    public static void handle(DynamaxPacket packet, NetworkManager.PacketContext context) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        Pokemon pokemon = PlayerUtils.getPartyPokemonFromUUID(player, packet.pokemonId());

        if (pokemon != null) {
            DynamaxGimmick.dynamaxToggle(pokemon, player);
        } else {
            // Pokemon not found in party - send error message
            player.sendMessage(Text.literal("§cPokémon not found in your party!"), false);
            DynamaxUnleashed.LOGGER.warn("Player {} tried to Dynamax non-existent Pokemon UUID: {}", 
                player.getName().getString(), packet.pokemonId());
        }
    }
}
