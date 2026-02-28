package com.dynamaxunleashed.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dynamaxunleashed.DynamaxUnleashed;
import com.dynamaxunleashed.cooldown.CooldownManager;
import com.dynamaxunleashed.config.ModConfig;
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
 * Admin commands for Dynamax Unleashed.
 *
 * /dynamax <player> <slot>       — Force toggle Dynamax, bypassing all requirements (OP lv 2)
 * /dynamax clear <player>        — Clear cooldown for a specific player's active Pokémon (OP lv 2)
 * /dynamax reload                — Reload config from disk (OP lv 4)
 */
public class DynamaxCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("dynamax")
                .requires(source -> source.hasPermissionLevel(2)) // Requires OP level 2

                // /dynamax <player> <slot> — admin force toggle, bypasses requirements
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .then(CommandManager.argument("slot", IntegerArgumentType.integer(1, 6))
                        .executes(DynamaxCommand::executeForceDynamax)
                    )
                )

                // /dynamax clear <player> — clear that player's cooldown
                .then(CommandManager.literal("clear")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(DynamaxCommand::executeClearCooldown)
                    )
                )

                // /dynamax reload — reload config from disk (OP level 4)
                .then(CommandManager.literal("reload")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(DynamaxCommand::executeReload)
                )
        );

        DynamaxUnleashed.LOGGER.info("Registered /dynamax command (force, clear, reload)");
    }

    /**
     * Force toggle Dynamax on the target player's Pokémon, bypassing all requirements.
     * Requires OP level 2.
     */
    private static int executeForceDynamax(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "player");
        int slot = IntegerArgumentType.getInteger(context, "slot");
        int slotIndex = slot - 1;

        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(targetPlayer);

        if (party == null) {
            source.sendError(Text.literal("§cCannot access party for player: " + targetPlayer.getName().getString()));
            return 0;
        }

        Pokemon pokemon = party.get(slotIndex);

        if (pokemon == null) {
            source.sendError(Text.literal("§cNo Pokémon in slot " + slot + " for player: " + targetPlayer.getName().getString()));
            return 0;
        }

        if (!DynamaxUnleashed.getConfig().enabled) {
            source.sendError(Text.literal("§cDynamax Unleashed is disabled in the config!"));
            return 0;
        }

        // Admin bypass: toggle state directly without any requirement checks
        boolean wasDynamaxed = DynamaxGimmick.isDynamax(pokemon);
        if (wasDynamaxed) {
            DynamaxGimmick.undynamaxForce(pokemon, targetPlayer);
        } else {
            DynamaxGimmick.dynamaxForce(pokemon, targetPlayer);
        }

        boolean isDynamaxed = DynamaxGimmick.isDynamax(pokemon);

        if (isDynamaxed != wasDynamaxed) {
            String action = isDynamaxed ? "Dynamaxed" : "reverted";
            source.sendFeedback(
                () -> Text.literal("§a[Admin] " + action + " " +
                    pokemon.getDisplayName(false).getString() +
                    " (slot " + slot + ") for " + targetPlayer.getName().getString()),
                true
            );
            return 1;
        } else {
            source.sendError(Text.literal("§cFailed to force toggle Dynamax."));
            return 0;
        }
    }

    /**
     * Clear Dynamax cooldown for all Pokémon of the target player.
     * Requires OP level 2.
     */
    private static int executeClearCooldown(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "player");
        CooldownManager cooldownManager = DynamaxUnleashed.getCooldownManager();

        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(targetPlayer);
        if (party == null) {
            source.sendError(Text.literal("§cCannot access party for player: " + targetPlayer.getName().getString()));
            return 0;
        }

        int cleared = 0;
        for (Pokemon pokemon : party) {
            if (pokemon != null) {
                cooldownManager.clearCooldown(pokemon.getUuid());
                cleared++;
            }
        }

        final int count = cleared;
        final String playerName = targetPlayer.getName().getString();
        source.sendFeedback(
            () -> Text.literal("§aCleared Dynamax cooldowns for " + count + " Pokémon of " + playerName),
            true
        );
        DynamaxUnleashed.LOGGER.info("Admin {} cleared Dynamax cooldowns for {}", source.getName(), playerName);
        return 1;
    }

    /**
     * Reload the mod config from disk.
     * Requires OP level 4.
     */
    private static int executeReload(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        try {
            ModConfig newConfig = ModConfig.load();
            DynamaxUnleashed.setConfig(newConfig);
            source.sendFeedback(
                () -> Text.literal("§aDynamax Unleashed config reloaded successfully!"),
                true
            );
            DynamaxUnleashed.LOGGER.info("Config reloaded by admin {}", source.getName());
            return 1;
        } catch (Exception e) {
            source.sendError(Text.literal("§cFailed to reload config: " + e.getMessage()));
            DynamaxUnleashed.LOGGER.error("Failed to reload config", e);
            return 0;
        }
    }
}
