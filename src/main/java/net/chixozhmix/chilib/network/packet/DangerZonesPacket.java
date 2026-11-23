package net.chixozhmix.chilib.network.packet;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.client.danger_zone.DangerZonePacketHandler;
import net.chixozhmix.chilib.utils.entity.geckolib.ZoneType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;


/**
 * Вызывайте, чтобы правильно отобразить зоны на координатах.
 * Чтобы отчистить зоны нужно просто передавать пустой список.
 */

/**
 * Вызывать пакет нужно примерно так где-то в AI моба:
 * private List<DangerZonesPacket.DangerZoneData> createCircleDangerZone(Vec3 targetPos, float radius) {
 *     List<DangerZonesPacket.DangerZoneData> zones = new ArrayList<>();
 *
 *     zones.add(new DangerZonesPacket.DangerZoneData(
 *         ZoneType.CIRCLE,        // Тип — круг
 *         targetPos.x, targetPos.y, targetPos.z, // Где рисуем круг (например, под целью)
 *         0.0F,                   // Поворот (для круга не нужен)
 *         radius * 2.0F,          // scaleX (диаметр по X)
 *         1.0F,                   // scaleY (высота)
 *         radius * 2.0F           // scaleZ (диаметр по Z)
 *     ));
 *
 *     return zones;
 * }
 *
 * А затем нечто вроде:
 * private void sendZones() {
 *     List<DangerZonesPacket.DangerZoneData> zones = createCircleDangerZone();
 *     Utils.sendDangerZones(modeus, zones);
 * }
 *
 * private void clearZones() {
 *     Utils.clearDangerZones(modeus);
 * }
 *
 * и уже эти два класса вызывать, например, при старте и конце выполнения цели атаки
 */

public class DangerZonesPacket implements CustomPacketPayload {
    public final int entityId;
    public final List<DangerZoneData> zones;

    private static final int MAX_ZONES = 256;
    public static final Type<DangerZonesPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChiLib.MODID, "danger_zone"));

    public DangerZonesPacket(int entityId, List<DangerZoneData> zones) {
        this.entityId = entityId;
        this.zones = List.copyOf(zones);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Кодек отдельной зоны
    public record DangerZoneData(ZoneType type, double x, double y, double z, float rotation, float scaleX, float scaleY, float scaleZ) {

        public static final StreamCodec<RegistryFriendlyByteBuf, DangerZoneData> STREAM_CODEC =
                StreamCodec.of(
                        (buffer, data) -> {
                            buffer.writeEnum(data.type());
                            buffer.writeDouble(data.x());
                            buffer.writeDouble(data.y());
                            buffer.writeDouble(data.z());
                            buffer.writeFloat(data.rotation());
                            buffer.writeFloat(data.scaleX());
                            buffer.writeFloat(data.scaleY());
                            buffer.writeFloat(data.scaleZ());
                        },
                        buffer -> new DangerZoneData(
                                buffer.readEnum(ZoneType.class),
                                buffer.readDouble(),
                                buffer.readDouble(),
                                buffer.readDouble(),
                                buffer.readFloat(),
                                buffer.readFloat(),
                                buffer.readFloat(),
                                buffer.readFloat()
                        )
                );
    }

    // Кодек всего пакета
    public static final StreamCodec<RegistryFriendlyByteBuf, DangerZonesPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buffer, packet) -> {
                        if (packet.zones.size() > MAX_ZONES) {
                            throw new IllegalArgumentException("Too many danger zones!");
                        }

                        buffer.writeVarInt(packet.entityId);
                        buffer.writeVarInt(packet.zones.size());

                        for (DangerZoneData zone : packet.zones) {
                            DangerZoneData.STREAM_CODEC.encode(buffer, zone);
                        }
                    },
                    buffer -> {
                        int entityId = buffer.readVarInt();
                        int size = buffer.readVarInt();

                        if (size < 0 || size > MAX_ZONES) {
                            throw new IllegalArgumentException("Invalid danger zones count: " + size);
                        }

                        List<DangerZoneData> zones = new ArrayList<>(size);

                        for (int i = 0; i < size; i++) {
                            zones.add(DangerZoneData.STREAM_CODEC.decode(buffer));
                        }

                        return new DangerZonesPacket(entityId, zones);
                    }
            );

    /** * Обработка пакета на клиенте. */
    public static void handle(DangerZonesPacket packet, IPayloadContext context) {

        context.enqueueWork(() -> {
            DangerZonePacketHandler.handle(packet);
        });
    }
}
