package team.heather.cataclysm.registry.item.effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.item.CataclysmItems;

public final class WeaponHitEffects {

    private static final int DEBUFF_TICKS = 120;
    private static final int PALE_DEBUFF_TICKS = 200;

    private WeaponHitEffects() {}

    public static void afterHit(ItemStack weapon, LivingEntity target, LivingEntity attacker) {
        if (weapon == null || target.level().isClientSide || !(attacker instanceof Player) || target instanceof Player) {
            return;
        }
        boolean pale = weapon.is(CataclysmItems.PALE_SWORD) || weapon.is(CataclysmItems.PALE_BOW);
        if (!pale && !weapon.is(CataclysmItems.TWISTED_SWORD)) {
            return;
        }
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, DEBUFF_TICKS, 2), attacker);
        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, DEBUFF_TICKS, 2), attacker);
        if (pale) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, PALE_DEBUFF_TICKS, 4), attacker);
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, PALE_DEBUFF_TICKS, 1), attacker);
        }
    }
}
