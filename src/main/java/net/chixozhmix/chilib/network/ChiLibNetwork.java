package net.chixozhmix.chilib.network;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.network.packet.DangerZonesPacket;
import net.chixozhmix.chilib.network.packet.EntityEventPacket;
import net.chixozhmix.chilib.network.packet.PlayPlayerAnimationPacket;
import net.chixozhmix.chilib.network.packet.SyncAnimPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ChiLib.MODID)
public class ChiLibNetwork {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar payloadRegistrar = event.registrar(ChiLib.MODID).versioned("1.0.0").optional();

        //SERVER

        //CLIENT
        payloadRegistrar.playToClient(DangerZonesPacket.TYPE, DangerZonesPacket.STREAM_CODEC, DangerZonesPacket::handle);
        payloadRegistrar.playToClient(EntityEventPacket.TYPE, EntityEventPacket.STREAM_CODEC, EntityEventPacket::handle);
        payloadRegistrar.playToClient(SyncAnimPacket.TYPE, SyncAnimPacket.STREAM_CODEC, SyncAnimPacket::handle);
        payloadRegistrar.playToClient(PlayPlayerAnimationPacket.TYPE, PlayPlayerAnimationPacket.STREAM_CODEC, PlayPlayerAnimationPacket::handle);
    }
}
