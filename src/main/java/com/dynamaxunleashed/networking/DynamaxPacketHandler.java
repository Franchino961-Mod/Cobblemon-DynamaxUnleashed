package com.dynamaxunleashed.networking;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.gimmick.DynamaxGimmick;
import com.dynamaxunleashed.utils.PlayerUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles incoming Dynamax toggle packets from clients
 * Pattern: Based on MegaShowdown's MegaEvoHandler
 */
public class DynamaxPacketHandler {
    
    // Rate-limit: minimum ms between packet processing per player (500ms)
    private static final long RATE_LIMIT_MS = 500L;
    private static final Map<UUID, Long> lastPacketTime = new ConcurrentHashMap<>();
    
    public static void handle(DynamaxPacket packet, NetworkManager.PacketContext context) {
        // Safe instanceof check before cast
        if (!(context.getPlayer() instanceof ServerPlayerEntity player)) {
            DynamaxUnleashed.LOGGER.warn("Received DynamaxPacket from a non-ServerPlayerEntity, ignoring.");
            return;
        }
        
        // Rate-limit: ignore packets sent too fast
        UUID playerId = player.getUuid();
        long now = System.currentTimeMillis();
        Long last = lastPacketTime.get(playerId);
        if (last != null && (now - last) < RATE_LIMIT_MS) {
            DynamaxUnleashed.LOGGER.debug("Rate-limiting Dynamax packet from player {}", player.getName().getString());
            return;
        }
        lastPacketTime.put(playerId, now);
        
        Pokemon pokemon = PlayerUtils.getPartyPokemonFromUUID(player, packet.pokemonId());

        if (pokemon != null) {
            DynamaxGimmick.dynamaxToggle(pokemon, player);
        } else {
            // Pokemon not found in party - send error message from config
            player.sendMessage(Text.translatable("dynamax_unleashed.message.pokemon_not_found"), false);
            DynamaxUnleashed.LOGGER.warn("Player {} tried to Dynamax non-existent Pokemon UUID: {}", 
                player.getName().getString(), packet.pokemonId());
        }
    }
}
