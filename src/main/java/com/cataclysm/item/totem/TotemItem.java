package com.cataclysm.item.totem;

import com.cataclysm.item.totem.mortality.MortalityTable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class TotemItem extends Item {
    private final MortalityTable mortalityTable;

    public TotemItem(Properties properties, MortalityTable mortalityTable) {
        super(properties);
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