package com.dynamaxunleashed.networking;

import com.dynamaxunleashed.DynamaxUnleashed;
import dev.architectury.networking.NetworkManager;

/**
 * Handles network communication between client and server for Dynamax operations
 * Uses Architectury API for cross-platform compatibility
 */
public class DynamaxNetworking {
    
    public static void register() {
        NetworkManager.registerReceiver(
            NetworkManager.Side.C2S, 
            DynamaxPacket.ID, 
            DynamaxPacket.CODEC, 
            DynamaxPacketHandler::handle
        );
        
        DynamaxUnleashed.LOGGER.info("Registered Dynamax network packets");
    }
}
