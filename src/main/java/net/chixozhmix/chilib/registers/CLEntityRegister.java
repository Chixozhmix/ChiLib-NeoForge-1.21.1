package net.chixozhmix.chilib.registers;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.misc.entity.FunnyCatEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CLEntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(Registries.ENTITY_TYPE, ChiLib.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FunnyCatEntity>> FUNNY_CAT = ENTITY.register("funny_cat",
            () -> EntityType.Builder.of(FunnyCatEntity::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.2F)
                    .clientTrackingRange(32)
                    .build("funny_cat"));


    public static void register(IEventBus eventBus) {
        ENTITY.register(eventBus);
    }
}
