package team.heather.cataclysm.item.totem;

import team.heather.cataclysm.component.cardinal.mortality.MortalityTable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DeathProtection;

public class TotemItem extends Item implements ResurrectionProvider {

    private final MortalityTable mortalityTable;

    public TotemItem(Properties properties,
                     MortalityTable mortalityTable) {
        super(properties.stacksTo(1).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING));
        this.mortalityTable = mortalityTable;
    }

    @Override
    public float getMortalityReduction(int week) {
        return this.mortalityTable.getValue(week);
    }
}
