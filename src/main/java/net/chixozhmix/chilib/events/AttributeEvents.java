package net.chixozhmix.chilib.events;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.attributes.ChiAttributes;
import net.chixozhmix.chilib.utils.AttributeUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;


//Атрибуты игрока применяются здесь. Каждый евент отвечает за свой атрибут или сразу за несколько
@EventBusSubscriber(modid = ChiLib.MODID)
public class AttributeEvents {
    //Arrow Damage and Arrow Velocity
    @SubscribeEvent
    public static void arrow(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.level().isClientSide || arrow.getPersistentData().getBoolean("chilib.arrow.done")) return;

            if (arrow.getOwner() instanceof LivingEntity le) {
                arrow.setBaseDamage(arrow.getBaseDamage() * le.getAttributeValue(ChiAttributes.ARROW_DAMAGE));
                arrow.setDeltaMovement(arrow.getDeltaMovement().scale(le.getAttributeValue(ChiAttributes.ARROW_VELOCITY)));
            }
            arrow.getPersistentData().putBoolean("chilib.arrow.done", true);
        }
    }

    //Crit Chance and Crit Damage
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void chiCriticalStrike(LivingDamageEvent.Pre e) {
        LivingEntity attacker = e.getSource().getEntity() instanceof LivingEntity le ? le : null;
        if (attacker == null) return;

        double critChance = attacker.getAttributeValue(ChiAttributes.CRIT_CHANCE);
        float critDmg = (float) attacker.getAttributeValue(ChiAttributes.CRIT_DAMAGE);

        RandomSource rand = e.getEntity().getRandom();

        float critMult = 1.0F;

        while (rand.nextFloat() <= critChance && critDmg > 1.0F) {
            critChance--;
            critMult *= critDmg;
            critDmg *= 0.85F;
        }

        e.setNewDamage(e.getOriginalDamage() * critMult);
    }

    //Life Steal and Melee Resistance
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void lifeSteal(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        float startAmount = event.getOriginalDamage();

        float resist = (float) entity.getAttributeValue(ChiAttributes.PHYSIC_RESISTANCE);

        if(resist > 1.0 && AttributeUtil.isPhysicalDamage(event.getSource())) {
            float damageMultiplier = 2.0f - resist;
            float finalDamage = startAmount * damageMultiplier;

            event.setNewDamage(finalDamage);
        }

        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker && AttributeUtil.isPhysicalDamage(event.getSource())) {
            float lifesteal = (float) attacker.getAttributeValue(ChiAttributes.LIFE_STEAL);
            float dmg = Math.min(event.getOriginalDamage(), event.getEntity().getHealth());
            if (lifesteal > 0.001) {
                attacker.heal(dmg * lifesteal);
            }
        }
    }

    //Heal
    @SubscribeEvent
    public static void healingEvent(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        float startAmount = event.getAmount();

        float heal = (float) entity.getAttributeValue(ChiAttributes.HEAL);

        if(heal > 1.0) {
            event.setAmount(startAmount * heal);
        }
    }
}
