package com.dynamaxunleashed;

import com.dynamaxunleashed.handler.InteractionGUIHandler;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client-side entry point for Dynamax Unleashed
 * Handles client-only initialization like GUI and events
 */
public class DynamaxUnleashedClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        DynamaxUnleashed.LOGGER.info("Initializing Dynamax Unleashed client...");
        
        // Register GUI event handlers
        InteractionGUIHandler.register();
        
        DynamaxUnleashed.LOGGER.info("Dynamax Unleashed client initialized!");
    }
}
