package com.dynamaxunleashed.utils;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * Utility methods for Dynamax requirements validation
 * Pattern: Based on Mega Showdown's PlayerUtils and AccessoriesUtils
 */
public class DynamaxUtils {
    
    // Mega Showdown's Power Spot block tag
    private static final TagKey<Block> POWER_SPOT_TAG = TagKey.of(
        RegistryKeys.BLOCK, 
        Identifier.of("mega_showdown", "power_spot")
    );
    
    /**
     * Check if a Power Spot block is within range of the player
     * Uses Mega Showdown's power_spot tag for compatibility
     * @param player The player to check around
     * @param radius Search radius in blocks
     * @return true if at least one Power Spot is found within radius
     */
    public static boolean isPowerSpotNearby(ServerPlayerEntity player, int radius) {
        return isBlockNearby(player, POWER_SPOT_TAG, radius);
    }
    
    /**
     * Check if a specific block tag is within range of the player
     * @param player The player to check around
     * @param blockTag The block tag to search for
     * @param radius Search radius in blocks
     * @return true if at least one matching block is found within radius
     */
    public static boolean isBlockNearby(ServerPlayerEntity player, TagKey<Block> blockTag, int radius) {
        BlockPos playerPos = player.getBlockPos();
        ServerWorld level = player.getServerWorld();
        
        // Scan a cube around the player
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos checkPos = playerPos.add(dx, dy, dz);
                    if (level.getBlockState(checkPos).isIn(blockTag)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Check if player has a Dynamax Band in their inventory
     * Simplified version - checks main inventory only (no Accessories support yet)
     * @param player The player to check
     * @return true if player has at least one Dynamax Band
     */
    public static boolean hasDynamaxBand(ServerPlayerEntity player) {
        // TODO: When Accessories support is added, also check accessory slots
        // Check for "mega_showdown:dynamax_band" in player's inventory
        Identifier dynamaxBandId = Identifier.of("mega_showdown", "dynamax_band");
        return player.getInventory().containsAny(stack -> {
            Identifier itemId = Registries.ITEM.getId(stack.getItem());
            return itemId.equals(dynamaxBandId);
        });
    }
}
