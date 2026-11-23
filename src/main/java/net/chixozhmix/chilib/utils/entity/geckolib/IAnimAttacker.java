package net.chixozhmix.chilib.utils.entity.geckolib;

import net.chixozhmix.chilib.network.ChiLibNetwork;
import net.chixozhmix.chilib.network.packet.SyncAnimPacket;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;


/**
 * Для существ, которые должны анимировать атаку.
 */
public interface IAnimAttacker {
    void playAnimation(String animationId);

    default <T extends Entity & IAnimAttacker> void serverTriggerAnimation(String animId) {
        PacketDistributor.sendToPlayersTrackingEntity((T) this, new SyncAnimPacket<>(animId, (T) this));
    }
}
