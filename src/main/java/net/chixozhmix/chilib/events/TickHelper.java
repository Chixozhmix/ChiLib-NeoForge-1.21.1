
package net.chixozhmix.chilib.events;

import net.chixozhmix.chilib.ChiLib;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

/*
 * Позволяет выполнять отложенные действия через определенное
 * количество серверных тиков.
 *
 * Например:
 * - Последовательный спавн частиц с интервалом.
 * - Последовательный спавн существ.
 * - Отложенное выполнение игровых эффектов.
 */

@EventBusSubscriber(modid = ChiLib.MODID)
public class TickHelper {
    private static final List<DelayedTask> TASKS = new ArrayList<>();

    public static void runLater(Level level, int delayTicks, Runnable action) {
        if (level.isClientSide())
            return;
        TASKS.add(new DelayedTask(Math.max(0, delayTicks), action));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (int i = TASKS.size() - 1; i >= 0; i--) {
            DelayedTask task = TASKS.get(i);
            task.ticksRemaining--;

            if (task.ticksRemaining <= 0) {
                TASKS.remove(i);

                try {
                    task.action.run();
                } catch (Exception e) {
                    ChiLib.LOGGER.error(
                            "Exception while executing delayed task", e
                    );
                }
            }
        }
    }

    private static class DelayedTask {
        private int ticksRemaining;
        private final Runnable action;

        private DelayedTask(int ticksRemaining, Runnable action) {
            this.ticksRemaining = ticksRemaining;
            this.action = action;
        }
    }
}
