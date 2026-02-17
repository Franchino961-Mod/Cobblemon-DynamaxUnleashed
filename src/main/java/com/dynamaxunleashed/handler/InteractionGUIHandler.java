package com.dynamaxunleashed.handler;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.interact.wheel.InteractWheelOption;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.gimmick.DynamaxGimmick;
import com.dynamaxunleashed.networking.DynamaxPacket;
import dev.architectury.networking.NetworkManager;
import kotlin.Unit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import java.util.Objects;

/**
 * Handles adding Dynamax button to Pokemon interaction GUI
 * Uses POKEMON_INTERACTION_GUI_CREATION event like MegaShowdown does
 */
public class InteractionGUIHandler {
    
    private static final Identifier DYNAMAX_ICON = Identifier.of(
        DynamaxUnleashed.MOD_ID, "textures/gui/dynamax_icon.png"
    );
    
    /**
     * Register the GUI interaction event handler
     * Called from client initialization
     */
    public static void register() {
        CobblemonEvents.POKEMON_INTERACTION_GUI_CREATION.subscribe(Priority.NORMAL, event -> {
            DynamaxUnleashed.LOGGER.info("EVENT FIRED! PokemonID: {}", event.getPokemonID());
            
            // Get Pokemon from client storage by entity UUID (not pokemon UUID)
            Pokemon pokemon = null;
            for (Pokemon partyMon : CobblemonClient.INSTANCE.getStorage().getParty()) {
                if (partyMon != null && partyMon.getEntity() != null) {
                    if (partyMon.getEntity().getUuid().equals(event.getPokemonID())) {
                        pokemon = partyMon;
                        break;
                    }
                }
            }
            
            if (pokemon == null) {
                DynamaxUnleashed.LOGGER.warn("Pokemon not found in party! Entity UUID: {}", event.getPokemonID());
                return Unit.INSTANCE;
            }
            
            // Only add button if config is enabled
            if (!DynamaxUnleashed.getConfig().enabled) {
                return Unit.INSTANCE;
            }
            
            // Check if already Dynamaxed
            boolean isDynamaxed = DynamaxGimmick.isDynamax(pokemon);
            
            // Make pokemon final for lambda
            final Pokemon finalPokemon = pokemon;
            
            // Always enable button - let server handle validation and send appropriate messages
            // Create InteractWheelOption for Dynamax
            InteractWheelOption dynamaxOption = new InteractWheelOption(
                DYNAMAX_ICON,
                null,                                    // no secondary icon
                true,                                    // always enabled - server will validate
                isDynamaxed ? "dynamax_unleashed.button.revert" : "dynamax_unleashed.button.dynamax",
                () -> new Vector3f(1, 1, 1),           // white color
                () -> {
                    // Send packet to server for validation and execution
                    // Server will check all requirements and send appropriate messages
                    NetworkManager.sendToServer(new DynamaxPacket(finalPokemon.getUuid()));
                    
                    // Close GUI
                    MinecraftClient.getInstance().setScreen(null);
                    
                    return Unit.INSTANCE;
                }
            );
            
            // Add option to interaction wheel
            event.addFillingOption(dynamaxOption);
            
            DynamaxUnleashed.LOGGER.debug("Added Dynamax option to interaction GUI for Pokemon: {}", pokemon.getSpecies().getName());
            
            return Unit.INSTANCE;
        });
        
        DynamaxUnleashed.LOGGER.info("Registered Pokemon interaction GUI handler");
    }
}
