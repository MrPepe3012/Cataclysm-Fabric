package team.heather.cataclysm.component.data.lemegeton;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import team.heather.cataclysm.item.effect.PassiveItemEffects;

import java.util.Locale;

/** Declaration order is the order of the seals around the circle. */
public enum LemegetonUpgrade implements StringRepresentable {

    REGENERATION(MobEffects.REGENERATION, 0, 1),
    JUMP_BOOST(MobEffects.JUMP_BOOST, 1),
    SPEED(MobEffects.SPEED, 1, 3),
    STRENGTH(MobEffects.STRENGTH, 0, 1, 2),
    DOLPHIN_GRACE(MobEffects.DOLPHINS_GRACE, 2, 4, 6),
    HEALTH_BLESSING(MobEffects.HEALTH_BOOST, 2, 4, 6),
    RESISTANCE(MobEffects.RESISTANCE, 0, 1, 2),
    HERO_OF_THE_VILLAGE(MobEffects.HERO_OF_THE_VILLAGE, 4),
    HASTE(MobEffects.HASTE, 0, 1, 2),
    INVISIBILITY(MobEffects.INVISIBILITY, 0),
    FIRE_RESISTANCE(MobEffects.FIRE_RESISTANCE, 0),
    CONDUIT_POWER(MobEffects.CONDUIT_POWER, 0),
    SLOW_FALLING(MobEffects.SLOW_FALLING, 0),
    CLEANSING(CataclysmicMobEffects.CLEANSING, 0, 1);

    public static final Codec<LemegetonUpgrade> CODEC = StringRepresentable.fromEnum(LemegetonUpgrade::values);

    private static final int FULL_CLEANSING_LEVEL = 2;
    private static final int SATURATION_AMPLIFIER = 10;

    private final Holder<MobEffect> effect;
    private final int[] amplifiers;

    LemegetonUpgrade(Holder<MobEffect> effect, int... amplifiers) {
        this.effect = effect;
        this.amplifiers = amplifiers.clone();
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String translationKey() {
        return "upgrade.cataclysm." + this.getSerializedName();
    }

    public Holder<MobEffect> effect() {
        return this.effect;
    }

    public String descriptionKey() {
        return this.translationKey() + ".description";
    }

    public int maxLevel() {
        return this.amplifiers.length;
    }

    public int effectLevel(int purchasedLevel) {
        return this.amplifiers[purchasedLevel - 1] + 1;
    }

    public void apply(ServerPlayer player, int purchasedLevel, int duration) {
        if (purchasedLevel < 1 || purchasedLevel > this.maxLevel()) {
            return;
        }
        int amplifier = this.amplifiers[purchasedLevel - 1];
        player.addEffect(new MobEffectInstance(this.effect, duration, amplifier));
        if (this == DOLPHIN_GRACE) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, duration, amplifier));
        } else if (this == HEALTH_BLESSING) {
            player.addEffect(new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, amplifier));
        } else if (this == CLEANSING && purchasedLevel >= FULL_CLEANSING_LEVEL) {
            restore(player, duration);
        }
    }

    private static void restore(ServerPlayer player, int duration) {
        PassiveItemEffects.restoreVitals(player);
        player.invulnerableTime = Math.max(player.invulnerableTime, 20);
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, duration, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, duration, SATURATION_AMPLIFIER, false, false));
    }
}
