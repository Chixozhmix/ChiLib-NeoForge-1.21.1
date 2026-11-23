package net.chixozhmix.chilib.network.packet;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.network.packet.handler.PlayAnimPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class PlayPlayerAnimationPacket implements CustomPacketPayload {
    private final UUID playerId;
    private final ResourceLocation animation;

    public static final CustomPacketPayload.Type<PlayPlayerAnimationPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChiLib.MODID, "play_player_animation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayPlayerAnimationPacket> STREAM_CODEC = CustomPacketPayload.codec(PlayPlayerAnimationPacket::toBytes, PlayPlayerAnimationPacket::new);

    public PlayPlayerAnimationPacket(UUID playerId, ResourceLocation animation) {
        this.playerId = playerId;
        this.animation = animation;
    }

    public PlayPlayerAnimationPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.animation = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeResourceLocation(animation);
    }

    public static void handle(PlayPlayerAnimationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            PlayAnimPacketHandler.handle(packet.playerId, packet.animation);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
