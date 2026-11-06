package team.heather.cataclysm.item.feature.totem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DeathProtection;
import team.heather.cataclysm.component.mortality.MortalityTable;
import team.heather.cataclysm.component.level.phase.Phase;

import java.util.Optional;

@FunctionalInterface
public interface ResurrectionProvider {

    ResurrectionProvider VANILLA = MortalityTable.VANILLA::getValue;

    static Optional<ResurrectionProvider> of(Item item) {
        if (item instanceof ResurrectionProvider totem) {
            return Optional.of(totem);
        }
        if (item == Items.TOTEM_OF_UNDYING) {
            return Optional.of(VANILLA);
        }
        return Optional.empty();
    }

    float getMortalityReduction(Phase phase);

    default boolean canResurrect(LivingEntity entity) {
        return true;
    }

    default DeathProtection resurrect(LivingEntity entity,
                                      DeathProtection protection) {
        return protection;
    }
}
