package net.chixozhmix.chilib.network.packet;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.network.packet.handler.AttackAnimPacketHandler;
import net.chixozhmix.chilib.utils.entity.geckolib.IAnimAttacker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncAnimPacket<T extends Entity & IAnimAttacker> implements CustomPacketPayload {
    public int entityId;
    public String animationId;

    public static final CustomPacketPayload.Type<SyncAnimPacket<?>> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChiLib.MODID, "sync_anim_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAnimPacket<?>> STREAM_CODEC = CustomPacketPayload.codec(SyncAnimPacket::toBytes, SyncAnimPacket::new);

    public SyncAnimPacket(String animationId, T entity) {
        this.entityId = entity.getId();
        this.animationId = animationId;
    }

    public SyncAnimPacket(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        animationId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUtf(animationId);
    }

    public static void handle(SyncAnimPacket<?> packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            AttackAnimPacketHandler.handle(packet.entityId, packet.animationId);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
