package com.cataclysm.item.totem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TotemItem extends Item {
    private static final TotemItem DEFAULT = new TotemItem(new Item.Properties());

    protected TotemItem(Properties properties) {
        super(properties);
    }

    public float getMortalityReduction(int week) {
        return switch (week) {
            case 4 -> 2.0F;
            case 5 -> 10.0F;
            case 6 -> 100.0F;
            default -> 1.0F;
        };
    }

    public static @Nullable Float getMortalityReduction(ItemStack stack, int week) {
        Item item = stack.getItem();
        if (item instanceof TotemItem totem) {
            return totem.getMortalityReduction(week);
        }
        if (item.equals(Items.TOTEM_OF_UNDYING)) {
            return DEFAULT.getMortalityReduction(week);
        }
        return null;
    }
}
