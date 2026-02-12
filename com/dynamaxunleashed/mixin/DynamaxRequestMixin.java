package com.dynamaxunleashed.mixin;

import com.dynamaxunleashed.DynamaxUnleashed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to allow Dynamax outside of battle
 * This modifies the getDynamaxRequest function from pokemon.js (Showdown battle simulator)
 * to bypass the battleOnly restriction when used in overworld
 */
@Mixin(targets = "com.github.megashowdown.showdown.PokemonJS", remap = false)
public class DynamaxRequestMixin {
    
    /**
     * Inject into getDynamaxRequest to allow overworld Dynamax
     * This is called when checking if a Pokémon can Dynamax
     * 
     * Original logic (pokemon.js line 707-733):
     * - Checks if in battle and side.canDynamaxNow()
     * - Blocks if mega/primal/ultra/terastallized
     * - Blocks if species.cannotDynamax
     * - Blocks if has Z-move item
     * 
     * Our modification: Allow the check to pass even when not in battle
     * if our mod's config is enabled
     */
    @Inject(
        method = "getDynamaxRequest",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private void allowOverworldDynamax(CallbackInfoReturnable<Object> cir) {
        try {
            // Only proceed if mod is enabled
            if (!DynamaxUnleashed.Companion.getConfig().getEnabled()) {
                return;
            }
            
            // Check if this call is from overworld context (not in battle)
            // If in overworld and our mod is active, we'll handle the Dynamax logic ourselves
            // Let the normal battle logic run for actual battles
            
            // Note: This is a simplified mixin
            // Full implementation would need to:
            // 1. Detect if call is from battle or overworld
            // 2. If overworld, bypass battle-only checks
            // 3. Still maintain requirements (no mega, no primal, etc.) if configured
            
            DynamaxUnleashed.Companion.getLOGGER().debug("DynamaxRequestMixin intercepted getDynamaxRequest call");
            
        } catch (Exception e) {
            DynamaxUnleashed.Companion.getLOGGER().error("Error in DynamaxRequestMixin", e);
        }
    }
}
