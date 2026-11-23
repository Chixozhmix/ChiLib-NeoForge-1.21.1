package net.chixozhmix.chilib.registers;

import net.chixozhmix.chilib.ChiLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CLSoundsRegister {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT =
            DeferredRegister.create(Registries.SOUND_EVENT, ChiLib.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> NECO_EMBIENT = registerSoundEvents("nya_nya");
    public static final DeferredHolder<SoundEvent, SoundEvent> NECO_HURT = registerSoundEvents("pyai_pyai");
    public static final DeferredHolder<SoundEvent, SoundEvent> NECO_EATING = registerSoundEvents("neco_eating");
    public static final DeferredHolder<SoundEvent, SoundEvent> NECO_DEATH = registerSoundEvents("neco_death");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENT.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ChiLib.MODID, name)));
    }

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENT.register(eventBus);
    }
}
