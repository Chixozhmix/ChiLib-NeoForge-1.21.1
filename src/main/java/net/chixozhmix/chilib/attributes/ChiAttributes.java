package net.chixozhmix.chilib.attributes;

import net.chixozhmix.chilib.ChiLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//Здесь регестрируются атрибуты игрока
@EventBusSubscriber(modid = ChiLib.MODID)
public class ChiAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES;

    public static final DeferredHolder<Attribute, Attribute> ARROW_DAMAGE;
    public static final DeferredHolder<Attribute, Attribute> ARROW_VELOCITY;
    public static final DeferredHolder<Attribute, Attribute> CRIT_DAMAGE;
    public static final DeferredHolder<Attribute, Attribute> CRIT_CHANCE;
    public static final DeferredHolder<Attribute, Attribute> LIFE_STEAL;
    public static final DeferredHolder<Attribute, Attribute> HEAL;
    public static final DeferredHolder<Attribute, Attribute> JUMP;
    public static final DeferredHolder<Attribute, Attribute> PHYSIC_RESISTANCE;

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach((entity) ->
                ATTRIBUTES.getEntries().forEach((attribute) -> {
                    if (!event.has(entity, attribute))
                        event.add(entity, attribute);
                }));
    }

    public static DeferredHolder<Attribute, Attribute> baseRangedAttribute(String id, double defaultValue, double minValue, double maxValue) {
        return ATTRIBUTES.register(id, () -> (new RangedAttribute("attribute." + ChiLib.MODID + "." + id, defaultValue, minValue, maxValue)).setSyncable(true));
    }

    static {
            ATTRIBUTES =DeferredRegister.create(Registries.ATTRIBUTE, ChiLib.MODID);
            ARROW_DAMAGE = baseRangedAttribute("arrow_damage", 1.0D, 0.0D, 10D);
            ARROW_VELOCITY = baseRangedAttribute("arrow_velocity", 1.0D, 0.0D, 10D);
            CRIT_CHANCE = baseRangedAttribute("crit_chance", 0.05D, 0.0D, 1.0D);
            CRIT_DAMAGE = baseRangedAttribute("crit_damage", 1.5D, 1.0D, 10.0D);
            LIFE_STEAL = baseRangedAttribute("life_steal", 0.0D, 0.0D, 1.0D);
            HEAL = baseRangedAttribute("heal", 1.0D, 0.0D, 2.0D);
            JUMP = baseRangedAttribute("jump", 1.0D, 1.0D, 5.0D);
            PHYSIC_RESISTANCE = baseRangedAttribute("physic_resistance", 1.0D, 0.0D, 2.0D);
    }
}
