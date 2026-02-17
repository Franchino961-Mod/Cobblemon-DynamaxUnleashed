package com.dynamaxunleashed.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Tag definitions for Dynamax Unleashed
 * Pattern: Based on MegaShowdown's tag system
 */
public class DynamaxTags {
    
    public static class Items {
        /**
         * Tag for all Dynamax Band items
         * Compatible with Mega Showdown's dynamax_band tag
         */
        public static final TagKey<Item> DYNAMAX_BAND = createTag("mega_showdown", "dynamax_band");
        
        private static TagKey<Item> createTag(String namespace, String path) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path));
        }
    }
}
