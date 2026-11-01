package team.heather.cataclysm.registry.item.pale;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class PaleWeaponItem extends Item {

    private static final int EFFECT_DURATION = 200;

    private static final int WITHER_AMPLIFIER = 2;
    private static final int SLOWNESS_AMPLIFIER = 1;
    private static final int WEAKNESS_AMPLIFIER = 1;
    private static final int GLOWING_AMPLIFIER = 0;

    public PaleWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public DamageSource getDamageSource(LivingEntity attacker) {
        return attacker.damageSources().magic();
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, EFFECT_DURATION, WITHER_AMPLIFIER));
        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, EFFECT_DURATION, SLOWNESS_AMPLIFIER));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, EFFECT_DURATION, WEAKNESS_AMPLIFIER));
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, EFFECT_DURATION, GLOWING_AMPLIFIER));
    }
}
