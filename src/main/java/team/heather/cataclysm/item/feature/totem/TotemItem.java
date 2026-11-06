package team.heather.cataclysm.item.feature.totem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DeathProtection;
import team.heather.cataclysm.component.mortality.MortalityTable;
import team.heather.cataclysm.component.level.phase.Phase;

public class TotemItem extends Item implements ResurrectionProvider {

    private final MortalityTable table;

    public TotemItem(Properties properties, MortalityTable table) {
        super(properties
            .stacksTo(1)
            .component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING));
        this.table = table;
    }

    @Override
    public float getMortalityReduction(Phase phase) {
        return this.table.getValue(phase);
    }
}
