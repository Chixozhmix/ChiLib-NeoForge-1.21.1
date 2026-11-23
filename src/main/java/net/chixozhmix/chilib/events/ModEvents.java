package net.chixozhmix.chilib.events;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.misc.entity.FunnyCatEntity;
import net.chixozhmix.chilib.registers.CLEntityRegister;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = ChiLib.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CLEntityRegister.FUNNY_CAT.get(), FunnyCatEntity.prepareAttributes().build());
    }
}
