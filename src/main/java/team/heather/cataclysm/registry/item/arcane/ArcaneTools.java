package team.heather.cataclysm.registry.item.arcane;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.item.CataclysmItems;
import team.heather.cataclysm.registry.sound.SoundMashup;

public final class ArcaneTools {

    private static final int ARROW_EFFECT_TICKS = 100;
    private static final int SHIELD_SLOW_TICKS = 60;
    private static final double SHIELD_KNOCKBACK = 0.5;

    private ArcaneTools() {}

    public static void onArrowHit(AbstractArrow arrow, LivingEntity target) {
        ItemStack weapon = arrow.getWeaponItem();
        if (arrow.level().isClientSide || weapon == null || !weapon.is(CataclysmItems.ARCANE_BOW)) {
            return;
        }
        boolean glowing = target.hasEffect(MobEffects.GLOWING);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, ARROW_EFFECT_TICKS, 0), arrow.getOwner());
        if (glowing) {
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, ARROW_EFFECT_TICKS, 0), arrow.getOwner());
        }
    }

    public static void onBlockedDamage(LivingEntity defender, ItemStack shield, DamageSource source, float blocked) {
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
        if (attacker instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, SHIELD_SLOW_TICKS, 1), defender);
            SoundMashup.ARCANE_SHIELD.playAtEntity(living);
        }
    }
}
