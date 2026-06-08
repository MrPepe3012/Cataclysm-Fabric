package com.cataclysm.registry.item.totem;

import com.cataclysm.registry.item.totem.mortality.MortalityTable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DeathProtection;

import java.util.Optional;

public class TotemItem extends Item {
    private final MortalityTable mortalityTable;

    public TotemItem(Properties properties, MortalityTable mortalityTable) {
        super(properties.stacksTo(1).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING));
        this.mortalityTable = mortalityTable;
    }

    public float getMortalityReduction(int week) {
        return this.mortalityTable.getValue(week);
    }

    public static Optional<Float> findMortalityReduction(Item item, int week) {
        if (item instanceof TotemItem totem) return Optional.of(totem.getMortalityReduction(week));
        if (item.equals(Items.TOTEM_OF_UNDYING)) return Optional.of(MortalityTable.VANILLA.getValue(week));
        return Optional.empty();
    }
}