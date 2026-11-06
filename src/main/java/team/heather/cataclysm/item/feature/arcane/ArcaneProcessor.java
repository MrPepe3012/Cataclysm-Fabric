package team.heather.cataclysm.item.feature.arcane;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.sound.SoundMashup;

public final class ArcaneProcessor {

    private static final int ARROW_EFFECT_TICKS = 100;
    private static final int SHIELD_SLOW_TICKS = 60;
    private static final double SHIELD_KNOCKBACK = 0.5;

    private ArcaneProcessor() {}

    public static void onArrowHit(AbstractArrow arrow,
                                  LivingEntity target) {
        var weapon = arrow.getWeaponItem();

        if (arrow.level().isClientSide() || !weapon.is(CataclysmItems.ARCANE_BOW)) {
            return;
        }

        var effect = target.hasEffect(MobEffects.GLOWING)
            ? MobEffects.SLOWNESS
            : MobEffects.GLOWING;
        var owner = arrow.getOwner();
        target.addEffect(new MobEffectInstance(effect, ARROW_EFFECT_TICKS, 0), owner);
    }

    public static void onBlockedDamage(LivingEntity defender,
                                       ItemStack shield,
                                       DamageSource source,
                                       float blocked) {
        if (blocked <= 0 || !shield.is(CataclysmItems.ARCANE_SHIELD)) {
            return;
        }

        var attacker = source.getDirectEntity();
        if (attacker == null || attacker == defender) {
            return;
        }

        var direction = attacker.position().subtract(defender.position()).normalize().scale(SHIELD_KNOCKBACK);
        attacker.setDeltaMovement(direction);
        attacker.hurtMarked = true;

        if (attacker instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, SHIELD_SLOW_TICKS, 1), defender);
            SoundMashup.ARCANE_SHIELD.playAtEntity(livingEntity);
        }
    }
}
