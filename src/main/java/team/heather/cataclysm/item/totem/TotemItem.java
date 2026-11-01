package team.heather.cataclysm.item.totem;

import team.heather.cataclysm.item.totem.mortality.MortalityTable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DeathProtection;

public class TotemItem extends Item implements ResurrectingItem {

    private final MortalityTable mortalityTable;

    public TotemItem(Properties properties,
                     MortalityTable mortalityTable) {
        super(applyProperties(properties));
        this.mortalityTable = mortalityTable;
    }

    @Override
    public float getMortalityReduction(int week) {
        return this.mortalityTable.getValue(week);
    }

    public static Properties applyProperties(Properties properties) {
        return properties.stacksTo(1).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING);
    }
}
