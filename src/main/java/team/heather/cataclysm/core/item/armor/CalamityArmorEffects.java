package team.heather.cataclysm.core.item.armor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.core.item.CataclysmItems;

public final class CalamityArmorEffects {

    private static final float FIRE_DAMAGE_MULTIPLIER = 0.5F;

    private CalamityArmorEffects() {}

    /**
     * Called from {@code LivingEntityMixin} at the head of {@code hurtServer}, so the
     * reduced amount flows through the vanilla pipeline (armor, i-frames, events) once.
     */
    public static float modifyDamage(LivingEntity entity,
                                     DamageSource source,
                                     float amount) {
        return isWearingLeggings(entity)
                ? reduceDamage(source, amount)
                : amount;
    }

    /** Calamity damage reductions, shared with {@link PaleArmorEffects}. */
    public static float reduceDamage(DamageSource source,
                                     float amount) {
        return source.is(DamageTypeTags.IS_FIRE)
                ? amount * FIRE_DAMAGE_MULTIPLIER
                : amount;
    }

    private static boolean isWearingLeggings(LivingEntity entity) {
        return entity instanceof ServerPlayer player
                && player.getItemBySlot(EquipmentSlot.LEGS).is(CataclysmItems.CALAMITY_LEGGINGS);
    }
}
