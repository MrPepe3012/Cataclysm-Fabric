package team.heather.cataclysm.core.item.totem;

import team.heather.cataclysm.core.item.totem.mortality.MortalityTable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DeathProtection;

import java.util.Optional;

@FunctionalInterface
public interface ResurrectingItem {

    ResurrectingItem VANILLA = MortalityTable.VANILLA::getValue;

    static Optional<ResurrectingItem> of(Item item) {
        if (item instanceof ResurrectingItem totem) return Optional.of(totem);
        if (item == Items.TOTEM_OF_UNDYING) return Optional.of(VANILLA);
        return Optional.empty();
    }

    float getMortalityReduction(int week);

    default boolean canResurrect(LivingEntity entity) {
        return true;
    }

    default DeathProtection resurrect(LivingEntity entity,
                                      DeathProtection protection) {
        return protection;
    }
}
