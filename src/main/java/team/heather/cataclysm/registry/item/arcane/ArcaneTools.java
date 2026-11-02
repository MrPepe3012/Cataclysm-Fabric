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
    private ArcaneTools() {}

    public static void onArrowHit(AbstractArrow arrow, LivingEntity target) {
        ItemStack weapon = arrow.getWeaponItem();
        if (arrow.level().isClientSide || weapon == null || !weapon.is(CataclysmItems.ARCANE_BOW)) return;

        boolean glowing = target.hasEffect(MobEffects.GLOWING);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0), arrow.getOwner());
        if (glowing) {
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 0), arrow.getOwner());
        }
    }

    public static void onBlockedDamage(LivingEntity defender, ItemStack shield, DamageSource source, float blocked) {
        if (blocked <= 0 || !shield.is(CataclysmItems.ARCANE_SHIELD)) return;
        var attacker = source.getDirectEntity();
        if (attacker == null || attacker == defender) return;

        var direction = attacker.position().subtract(defender.position()).normalize().scale(0.5);
        attacker.setDeltaMovement(direction);
        attacker.hurtMarked = true;
        if (attacker instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 1), defender);
            SoundMashup.ARCANE_SHIELD.playAtEntity(living);
        }
    }
}
