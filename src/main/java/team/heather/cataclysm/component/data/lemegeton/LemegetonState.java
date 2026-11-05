package team.heather.cataclysm.component.data.lemegeton;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.EnumMap;
import java.util.Map;

/** Immutable state owned by one Lemegeton stack, never by a player. */
public record LemegetonState(Map<LemegetonUpgrade, Integer> upgrades, int bonusSlots) {

    private static final Codec<Map<LemegetonUpgrade, Integer>> UPGRADES_CODEC = Codec.unboundedMap(
        LemegetonUpgrade.CODEC, Codec.intRange(1, 3)).validate(levels -> {
            for (var entry : levels.entrySet()) {
                if (entry.getValue() > entry.getKey().maxLevel()) {
                    return DataResult.error(() -> "Invalid level for " + entry.getKey().getSerializedName());
                }
            }
            return DataResult.success(levels);
        });

    public static final Codec<LemegetonState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UPGRADES_CODEC.optionalFieldOf("upgrades", Map.of()).forGetter(LemegetonState::upgrades),
        Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("bonus_slots", 0).forGetter(LemegetonState::bonusSlots)
    ).apply(instance, LemegetonState::new));
    public static final LemegetonState EMPTY = new LemegetonState(Map.of(), 0);

    public LemegetonState {
        upgrades = Map.copyOf(upgrades);
        if (bonusSlots < 0) {
            throw new IllegalArgumentException("Negative Lemegeton state");
        }
        for (var entry : upgrades.entrySet()) {
            if (entry.getValue() < 1 || entry.getValue() > entry.getKey().maxLevel()) {
                throw new IllegalArgumentException("Invalid upgrade level");
            }
        }
    }

    public int level(LemegetonUpgrade upgrade) {
        return this.upgrades.getOrDefault(upgrade, 0);
    }

    public int totalLevels() {
        return this.upgrades.values().stream().mapToInt(Integer::intValue).sum();
    }

    public boolean hasUpgrades() {
        return !this.upgrades.isEmpty();
    }

    public LemegetonState upgrade(LemegetonUpgrade upgrade) {
        int next = this.level(upgrade) + 1;
        if (next > upgrade.maxLevel()) {
            throw new IllegalStateException("Upgrade is already maxed");
        }
        var levels = new EnumMap<LemegetonUpgrade, Integer>(LemegetonUpgrade.class);
        levels.putAll(this.upgrades);
        levels.put(upgrade, next);
        return new LemegetonState(levels, this.bonusSlots);
    }

    /** Integration point for a future incursion reward; the reward stays with this book. */
    public LemegetonState withBonusSlots(int slots) {
        return new LemegetonState(this.upgrades, slots);
    }
}
