package net.chixozhmix.chilib.network.packet;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.client.bossbar.IEntityClientEvent;
import net.chixozhmix.chilib.utils.minecraft.MInecraftInstanceHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;

public class EntityEventPacket<T extends Entity & IEntityClientEvent> implements CustomPacketPayload {
    private final int entityId;
    private final byte eventId;

    public static final CustomPacketPayload.Type<EntityEventPacket<?>> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChiLib.MODID, "entity_event"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityEventPacket<?>> STREAM_CODEC = CustomPacketPayload.codec(EntityEventPacket::toBytes, EntityEventPacket::new);


    public EntityEventPacket(Entity pEntity, byte pEventId) {
        this.entityId = pEntity.getId();
        this.eventId = pEventId;
    }

    public EntityEventPacket(FriendlyByteBuf pBuffer) {
        this.entityId = pBuffer.readInt();
        this.eventId = pBuffer.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void toBytes(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.entityId);
        pBuffer.writeByte(this.eventId);
    }


    public static void handle(EntityEventPacket<?> packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MInecraftInstanceHelper.ifPlayerPresent(player -> {
                if (packet.getEntity(player.level()) instanceof IEntityClientEvent entity) {
                    entity.handleClientEvent(packet.getEventId());
                }
            });
        });
    }

    @Nullable
    public Entity getEntity(Level pLevel) {
        return pLevel.getEntity(this.entityId);
    }

    public byte getEventId() {
        return this.eventId;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
