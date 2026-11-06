package team.heather.cataclysm.effect.beneficial;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.network.payload.SuppressedDamagePayload;

/**
 * Cancels all damage taken by the affected entity. The damage it would have
 * taken is sent to players as a {@link SuppressedDamagePayload}, so their health
 * bar can show the hearts they were spared.
 */
public class ImmortalityMobEffect extends MobEffect {

    private static final double INTENSITY_GROWTH = 1.03D;
    private static final double PARTICLE_GROWTH = 1.4D;

    private static final float FREEZING_DAMAGE_MULTIPLIER = 5.0F;
    private static final float HELMET_DAMAGE_MULTIPLIER = 0.75F;
    private static final int RESISTANCE_DAMAGE_STEPS = 25;
    private static final int RESISTANCE_STEPS_PER_LEVEL = 5;

    private static final float SOUND_VOLUME = 0.7F;
    private static final float LOW_SOUND_PITCH = 0.85F;
    private static final float HIGH_SOUND_PITCH = 1.15F;

    public ImmortalityMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    /**
     * Cancels the damage if the entity has Immortality.
     *
     * @param entity the entity being hurt
     * @param source the source of the damage
     * @param damage the damage before armor and protection
     * @return {@code true} if the damage should be cancelled
     */
    public static boolean tryCancel(LivingEntity entity, DamageSource source, float damage) {
        if (damage <= 0.0F || !entity.hasEffect(CataclysmMobEffects.IMMORTALITY) || !(entity.level() instanceof ServerLevel level)) {
            return false;
        }

        float processedDamage = calculateDamage(level, entity, source, damage);

        if (entity instanceof ServerPlayer player && processedDamage > 0.0F) {
            ServerPlayNetworking.send(player, new SuppressedDamagePayload(processedDamage));
        }

        sendParticles(level, entity, Math.pow(INTENSITY_GROWTH, processedDamage));
        playSounds(level, entity);
        return true;
    }

    /** Mirrors the reductions vanilla applies in {@link LivingEntity#hurtServer}. */
    private static float calculateDamage(ServerLevel level, LivingEntity entity, DamageSource source, float damage) {
        float afterArmor = applyArmor(entity, source, scaleDamage(entity, source, damage));

        if (source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return Math.max(afterArmor, 0.0F);
        }

        return applyProtection(level, entity, source, applyResistance(entity, source, afterArmor));
    }

    private static float scaleDamage(LivingEntity entity, DamageSource source, float damage) {
        float scaled = damage;

        if (source.is(DamageTypeTags.IS_FREEZING) && entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            scaled *= FREEZING_DAMAGE_MULTIPLIER;
        }

        if (source.is(DamageTypeTags.DAMAGES_HELMET) && !entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            scaled *= HELMET_DAMAGE_MULTIPLIER;
        }

        return scaled;
    }

    private static float applyArmor(LivingEntity entity, DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            return damage;
        }

        float toughness = (float) entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
        return CombatRules.getDamageAfterAbsorb(entity, damage, source, entity.getArmorValue(), toughness);
    }

    private static float applyResistance(LivingEntity entity, DamageSource source, float damage) {
        MobEffectInstance resistance = entity.getEffect(MobEffects.RESISTANCE);

        if (resistance == null || source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return damage;
        }

        int remainingSteps = RESISTANCE_DAMAGE_STEPS - (resistance.getAmplifier() + 1) * RESISTANCE_STEPS_PER_LEVEL;
        return Math.max(damage * remainingSteps / RESISTANCE_DAMAGE_STEPS, 0.0F);
    }

    private static float applyProtection(ServerLevel level, LivingEntity entity, DamageSource source, float damage) {
        if (damage <= 0.0F || source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            return Math.max(damage, 0.0F);
        }

        float protection = EnchantmentHelper.getDamageProtection(level, entity, source);
        return protection > 0.0F ? CombatRules.getDamageAfterMagicAbsorb(damage, protection) : damage;
    }

    /**
     * Sends the particles used as visual feedback for Immortality.
     *
     * @param level the server level in which to send the particles
     * @param entity the protected entity
     * @param intensity how strong the cancelled hit was
     */
    public static void sendParticles(ServerLevel level, LivingEntity entity, double intensity) {
        double particleIntensity = Math.pow(intensity, PARTICLE_GROWTH);
        double horizontalSpread = Math.clamp(0.35D * particleIntensity, 0.35D, 1.1D);
        double verticalSpread = Math.clamp(0.55D * particleIntensity, 0.55D, 1.5D);
        double speed = Math.clamp(0.01D * particleIntensity, 0.01D, 0.1D);
        int count = (int) Math.clamp(Math.ceil(6.0D * particleIntensity), 6.0D, 40.0D);
        double y = entity.getY() + entity.getBbHeight() * 0.5D;

        level.sendParticles(ParticleTypes.END_ROD, entity.getX(), y, entity.getZ(), count, horizontalSpread, verticalSpread, horizontalSpread, speed);
    }

    /**
     * Plays the sounds used as audible feedback for Immortality.
     *
     * @param level the server level in which to play the sounds
     * @param entity the protected entity
     */
    public static void playSounds(ServerLevel level, LivingEntity entity) {
        level.playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_REPAIR, entity.getSoundSource(), SOUND_VOLUME, LOW_SOUND_PITCH);
        level.playSound(null, entity.blockPosition(), SoundEvents.IRON_GOLEM_REPAIR, entity.getSoundSource(), SOUND_VOLUME, HIGH_SOUND_PITCH);
    }
}
