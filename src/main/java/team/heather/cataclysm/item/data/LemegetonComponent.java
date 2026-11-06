package team.heather.cataclysm.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.item.behavior.PassiveItemProcessor;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/** Immutable state owned by one Lemegeton stack, never by a player. */
public record LemegetonComponent(Map<Upgrade, Integer> upgrades, int bonusSlots) {

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 3;

    private static final Codec<Map<Upgrade, Integer>> UPGRADES_CODEC = Codec.unboundedMap(
        Upgrade.CODEC, Codec.intRange(MIN_LEVEL, MAX_LEVEL)).validate(levels -> {
            for (var entry : levels.entrySet()) {
                if (entry.getValue() > entry.getKey().maxLevel()) {
                    return DataResult.error(() -> "Invalid level for " + entry.getKey().getSerializedName());
                }
            }
            return DataResult.success(levels);
        });

    public static final Codec<LemegetonComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UPGRADES_CODEC.optionalFieldOf("upgrades", Map.of()).forGetter(LemegetonComponent::upgrades),
        Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("bonus_slots", 0).forGetter(LemegetonComponent::bonusSlots)
    ).apply(instance, LemegetonComponent::new));
    public static final LemegetonComponent EMPTY = new LemegetonComponent(Map.of(), 0);

    public LemegetonComponent {
        upgrades = Map.copyOf(upgrades);
        if (bonusSlots < 0) {
            throw new IllegalArgumentException("Negative Lemegeton state");
        }
        for (var entry : upgrades.entrySet()) {
            if (entry.getValue() < MIN_LEVEL || entry.getValue() > entry.getKey().maxLevel()) {
                throw new IllegalArgumentException("Invalid upgrade level");
            }
        }
    }

    public int level(Upgrade upgrade) {
        return this.upgrades.getOrDefault(upgrade, 0);
    }

    public int totalLevels() {
        return this.upgrades.values().stream().mapToInt(Integer::intValue).sum();
    }

    public boolean hasUpgrades() {
        return !this.upgrades.isEmpty();
    }

    public LemegetonComponent upgrade(Upgrade upgrade) {
        int next = this.level(upgrade) + 1;
        if (next > upgrade.maxLevel()) {
            throw new IllegalStateException("Upgrade is already maxed");
        }
        return this.withLevel(upgrade, next);
    }

    /** Level zero removes the upgrade. */
    public LemegetonComponent withLevel(Upgrade upgrade, int level) {
        var levels = new EnumMap<Upgrade, Integer>(Upgrade.class);
        levels.putAll(this.upgrades);
        if (level == 0) {
            levels.remove(upgrade);
        } else {
            levels.put(upgrade, level);
        }
        return new LemegetonComponent(levels, this.bonusSlots);
    }

    /** Integration point for a future incursion reward; the reward stays with this book. */
    public LemegetonComponent withBonusSlots(int slots) {
        return new LemegetonComponent(this.upgrades, slots);
    }

    /** Declaration order is the order of the seals around the circle. */
    public enum Upgrade implements StringRepresentable {

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
        CLEANSING(CataclysmMobEffects.CLEANSING, 0, 1);

        public static final Codec<Upgrade> CODEC = StringRepresentable.fromEnum(Upgrade::values);

        private static final int FULL_CLEANSING_LEVEL = 2;
        private static final int SATURATION_AMPLIFIER = 10;
        private static final int RESTORE_INVULNERABILITY_TICKS = 20;

        private final Holder<MobEffect> effect;
        private final int[] amplifiers;

        Upgrade(Holder<MobEffect> effect, int... amplifiers) {
            this.effect = effect;
            this.amplifiers = amplifiers.clone();
        }

        @Override
        public @NotNull String getSerializedName() {
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
            PassiveItemProcessor.restoreVitals(player);
            player.invulnerableTime = Math.max(player.invulnerableTime, RESTORE_INVULNERABILITY_TICKS);
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, duration, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, duration, SATURATION_AMPLIFIER, false, false));
        }
    }
}
