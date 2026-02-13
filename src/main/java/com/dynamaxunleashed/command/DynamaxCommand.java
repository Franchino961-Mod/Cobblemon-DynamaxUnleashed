package com.dynamaxunleashed.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.gimmick.DynamaxGimmick;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Command to toggle Dynamax on a player's Pokemon
 * Usage: /dynamax <player> <slot>
 * Permission: Requires operator level 2
 */
public class DynamaxCommand {
    
    /**
     * Register the /dynamax command
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("dynamax")
                .requires(source -> source.hasPermissionLevel(2)) // Requires OP level 2
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .then(CommandManager.argument("slot", IntegerArgumentType.integer(1, 6))
                        .executes(DynamaxCommand::execute)
                    )
                )
        );
        
        DynamaxUnleashed.LOGGER.info("Registered /dynamax command");
    }
    
    /**
     * Execute the dynamax command
     */
    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "player");
        int slot = IntegerArgumentType.getInteger(context, "slot");
        
        // Convert 1-6 to 0-5 index
        int slotIndex = slot - 1;
        
        // Get player's party
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(targetPlayer);
        
        if (party == null) {
            source.sendError(Text.literal("§cCannot access party for player: " + targetPlayer.getName().getString()));
            return 0;
        }
        
        // Get Pokemon in slot
        Pokemon pokemon = party.get(slotIndex);
        
        if (pokemon == null) {
            source.sendError(Text.literal("§cNo Pokemon in slot " + slot + " for player: " + targetPlayer.getName().getString()));
            return 0;
        }
        
        // Check if config is enabled
        if (!DynamaxUnleashed.getConfig().enabled) {
            source.sendError(Text.literal("§cDynamax Unleashed is disabled in the config!"));
            return 0;
        }
        
        // Store initial state
        boolean wasDynamaxed = DynamaxGimmick.isDynamax(pokemon);
        
        // Toggle Dynamax
        DynamaxGimmick.dynamaxToggle(pokemon, targetPlayer);
        
        // Check if state changed
        boolean isDynamaxed = DynamaxGimmick.isDynamax(pokemon);
        
        if (isDynamaxed != wasDynamaxed) {
            String action = isDynamaxed ? "Dynamaxed" : "reverted";
            
            source.sendFeedback(
                () -> Text.literal("§a" + action + " " + pokemon.getDisplayName(false).getString() + 
                                 " (slot " + slot + ") for player " + targetPlayer.getName().getString()),
                true
            );
            return 1;
        } else {
            source.sendError(Text.literal("§cFailed to toggle Dynamax - check if Pokemon meets requirements or is on cooldown"));
            return 0;
        }
    }
}
