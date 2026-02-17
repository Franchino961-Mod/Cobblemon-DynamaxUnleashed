package com.dynamaxunleashed.utils;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

/**
 * Utility methods for checking Accessories API integration
 * Pattern: Based on MegaShowdown's AccessoriesUtils (adapted for Fabric)
 */
public class AccessoriesUtils {
    
    /**
     * Check if a player has an item with a specific tag equipped in their accessories slots
     * Also checks main hand and offhand for convenience
     * 
     * @param player The player/entity to check
     * @param tag The item tag to search for
     * @return true if the player has the item equipped in accessories, main hand, or offhand
     */
    public static boolean checkTagInAccessories(LivingEntity player, TagKey<Item> tag) {
        // Check main hand and offhand first
        if (player.getMainHandStack().isIn(tag) || player.getOffHandStack().isIn(tag)) {
            return true;
        }
        
        // Check accessories slots using Accessories API
        return AccessoriesCapability.getOptionally(player)
                .map(capability -> capability.getAllEquipped()
                        .stream()
                        .anyMatch(stack -> stack.stack().isIn(tag)))
                .orElse(false);
    }
}
