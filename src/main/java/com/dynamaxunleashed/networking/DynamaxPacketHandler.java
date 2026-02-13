package com.dynamaxunleashed.networking;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.gimmick.DynamaxGimmick;
import com.dynamaxunleashed.util.PlayerUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.network.ServerPlayerEntity;

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
        }
    }
}
