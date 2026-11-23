
package net.chixozhmix.chilib.utils.teleport;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class TeleportData {

    /**
     * Безопасная телепортация игрока между измерениями.
     *
     * @param player игрок, которого нужно телепортировать
     * @param targetLevel измерение назначения
     */
    public static void teleportPlayer(Player player, ServerLevel targetLevel) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (targetLevel == null) return;

        BlockPos safePos = findSafeSpawnLocation(targetLevel);

        serverPlayer.teleportTo(
                targetLevel,
                safePos.getX() + 0.5,
                safePos.getY(),
                safePos.getZ() + 0.5,
                serverPlayer.getYRot(),
                serverPlayer.getXRot()
        );
    }

    /**
     * Поиск безопасной позиции появления.
     */
    protected static BlockPos findSafeSpawnLocation(ServerLevel level) {
        BlockPos targetPos = new BlockPos(0, 60, 0);

        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {

                for (int y = Math.min(targetPos.getY() + 10, maxY - 3);
                     y >= minY; y--) {

                    BlockPos checkPos = new BlockPos(
                            targetPos.getX() + x,
                            y,
                            targetPos.getZ() + z
                    );

                    if (isSafeSpawnPosition(level, checkPos)) {
                        return checkPos.above();
                    }
                }
            }
        }

        BlockPos spawn = level.getSharedSpawnPos();
        return spawn.atY(Math.max(spawn.getY(), minY + 10));
    }

    /**
     * Проверка безопасности позиции.
     */
    protected static boolean isSafeSpawnPosition(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).isSolid()
                || level.getBlockState(pos).is(Blocks.BEDROCK)
                || level.getBlockState(pos).is(Blocks.LAVA)) {
            return false;
        }

        return level.getBlockState(pos.above()).isAir()
                && level.getBlockState(pos.above(2)).isAir();
    }
}
