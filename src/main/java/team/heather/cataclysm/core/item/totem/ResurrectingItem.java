package team.heather.cataclysm.core.item.totem;

import team.heather.cataclysm.core.item.totem.mortality.MortalityTable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DeathProtection;

import java.util.Optional;

/**
 * An item that resurrects its holder. Every difference between these items lives in an
 * override, so callers never ask which one they are holding.
 */
@FunctionalInterface
public interface ResurrectingItem {

    /** Vanilla's Totem of Undying, which cannot implement this interface itself. */
    ResurrectingItem VANILLA = MortalityTable.VANILLA::getValue;

    /** Mortality the holder loses when this totem resurrects them. */
    float getMortalityReduction(int week);

    /**
     * Whether this totem works for {@code entity} right now. This is where Mortem rejects
     * totems once cataclysmic-effects provides it.
     */
    default boolean canResurrect(LivingEntity entity) {
        return true;
    }

    /** Effects applied on resurrection, starting from those in the item's death protection. */
    default DeathProtection resurrect(LivingEntity entity,
                                      DeathProtection protection) {
        return protection;
    }

    static Optional<ResurrectingItem> of(Item item) {
        if (item instanceof ResurrectingItem totem) return Optional.of(totem);
        if (item == Items.TOTEM_OF_UNDYING) return Optional.of(VANILLA);
        return Optional.empty();
    }
}
