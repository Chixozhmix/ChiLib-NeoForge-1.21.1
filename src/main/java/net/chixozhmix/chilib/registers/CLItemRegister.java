package net.chixozhmix.chilib.registers;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.misc.items.KefirBottleItem;
import net.chixozhmix.chilib.utils.items.PropertiesHelper;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CLItemRegister {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChiLib.MODID);

    public static final DeferredItem<Item> SALTED_CUCUMBERS = ITEMS.register("salted_cucumbers",
            () -> new Item(PropertiesHelper.stackItemProperties(64).food(CLFoodRegister.SALTED_CUCUMBERS)));
    public static final DeferredItem<Item> KEFIR_BOTTLE = ITEMS.register("kefir_bottle",
            () -> new KefirBottleItem(PropertiesHelper.stackItemProperties(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
