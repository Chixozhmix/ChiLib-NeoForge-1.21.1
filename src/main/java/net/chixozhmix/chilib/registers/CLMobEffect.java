package net.chixozhmix.chilib.registers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/*Для регистрации простых эффектов, которые не требуют сложной логики.
    Здесь три вида - обычный, с эффектом постоянного урона раз в секунду и модифицирующий атрибут (пока только один)
 */
public class CLMobEffect {
    public static class SimpleEffect extends MobEffect {
        public SimpleEffect(MobEffectCategory pCategory, int pColor) {
            super(pCategory, pColor);
        }
    }
    public static class DamageTickEffect extends MobEffect {
        private final float damage;
        public DamageTickEffect(int pColor, float damage) {
            super(MobEffectCategory.HARMFUL, pColor);
            this.damage = damage;
        }

        @Override
        public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
            if(livingEntity.level().getGameTime() % 20 == 0) {
                float applyDamage = damage * (amplifier + 1);
                livingEntity.hurt(livingEntity.damageSources().magic(), applyDamage);
            }

            return super.applyEffectTick(livingEntity, amplifier);
        }

        @Override
        public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
            return true;
        }
    }

    public static class AttributeEffect extends MobEffect {
        public AttributeEffect(MobEffectCategory pCategory, int pColor) {
            super(pCategory, pColor);
        }

        @Override
        public MobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation) {
            return super.addAttributeModifier(attribute, id, amount, operation);
        }
    }
}
