package com.github.poitchin.common;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class DeathMessageHelper {
    /**
     * Builds the death message Component for a given player.
     *
     * The message includes:
     * - The death coordinates,
     * - The dimension the player is in,
     * - The horizontal distance (in blocks) from the player's respawn point.
     * If the player's game mode is not survival, the coordinates are clickable to teleport.
     *
     * @param player The server player who died.
     * @return The formatted chat Component.
     */
    public static Component buildDeathMessage(ServerPlayer player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        // Get the player's respawn position (bed / /spawnpoint) or fallback to the world spawn.
        BlockPos respawnPos = Optional.ofNullable(player.getRespawnPosition())
                .orElse(player.level().getSharedSpawnPos());

        // Calculate horizontal distance using integer block positions.
        BlockPos deathPos = player.blockPosition();
        int dx = deathPos.getX() - respawnPos.getX();
        int dz = deathPos.getZ() - respawnPos.getZ();
        int blocksAway = (int) Math.floor(Math.sqrt(dx * dx + dz * dz));

        // Get the current dimension name.
        ResourceKey<Level> dimensionKey = player.level().dimension();
        String dimensionName = dimensionKey.location().toString();

        Component message;
        if (player.gameMode.getGameModeForPlayer() != GameType.SURVIVAL) {
            message = Component.literal("You died at ")
                    .append(Component.literal(String.format("[%1.0f, %1.0f, %1.0f]", x, y, z))
                            .withStyle(style -> style
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @s " + x + " " + y + " " + z))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to teleport")))
                                    .withColor(ChatFormatting.GREEN)))
                    .append(Component.literal(" in dimension " + dimensionName + " "))
                    .append(Component.literal("(" + blocksAway + " blocks away)"));
        } else {
            message = Component.literal("You died at " + String.format("[%1.0f, %1.0f, %1.0f]", x, y, z))
                    .append(Component.literal(" in dimension " + dimensionName + " "))
                    .append(Component.literal("(" + blocksAway + " blocks away)"));
        }
        return message;
    }
}
