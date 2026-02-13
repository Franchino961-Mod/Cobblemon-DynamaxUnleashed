package com.dynamaxunleashed.networking;

import com.dynamaxunleashed.DynamaxUnleashed;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

/**
 * Packet sent from client to server when player toggles Dynamax on a Pokémon
 * Pattern: Based on MegaShowdown but using Fabric mappings
 */
public record DynamaxPacket(UUID pokemonId) implements CustomPayload {
    public static final Id<DynamaxPacket> ID = new Id<>(
        Identifier.of(DynamaxUnleashed.MOD_ID, "dynamax_toggle"));

    public static final PacketCodec<PacketByteBuf, DynamaxPacket> CODEC = 
        PacketCodec.of(
            (value, buf) -> buf.writeUuid(value.pokemonId),
            buf -> new DynamaxPacket(buf.readUuid())
        );

    @Override
    public Id<DynamaxPacket> getId() {
        return ID;
    }
}
