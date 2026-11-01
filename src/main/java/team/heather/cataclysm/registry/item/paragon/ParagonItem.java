package team.heather.cataclysm.registry.item.paragon;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ParagonItem extends Item implements ImmunityGrantingItem {

    private final ImmunityTable immunityTable;

    public ParagonItem(Properties properties,
                       ImmunityTable immunityTable) {
        super(applyProperties(properties));
        this.immunityTable = immunityTable;
    }

    @Override
    public ImmunityTable getImmunityTable() {
        return this.immunityTable;
    }

    public static Properties applyProperties(Properties properties) {
        return properties
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                .rarity(Rarity.EPIC);
    }
}
