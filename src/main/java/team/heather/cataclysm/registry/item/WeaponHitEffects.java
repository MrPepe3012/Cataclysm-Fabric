package team.heather.cataclysm.registry.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class WeaponHitEffects {
    private WeaponHitEffects() {}

    public static void afterHit(ItemStack weapon, LivingEntity target, LivingEntity attacker) {
        if (weapon == null || target.level().isClientSide || !(attacker instanceof Player) || target instanceof Player) return;
        boolean pale = weapon.is(CataclysmItems.PALE_SWORD) || weapon.is(CataclysmItems.PALE_BOW);
        if (!pale && !weapon.is(CataclysmItems.TWISTED_SWORD)) return;
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120, 2), attacker);
        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 120, 2), attacker);
        if (pale) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 4), attacker);
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1), attacker);
        }
    }
}
