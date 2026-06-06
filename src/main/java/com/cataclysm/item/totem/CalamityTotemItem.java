package com.cataclysm.item.totem;

public class CalamityTotemItem extends TotemItem {
    protected CalamityTotemItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getMortalityReduction(int week) {
        return switch (week) {
            case 4 -> 0.5F;
            case 5 -> 2.0F;
            case 6 -> 10.0F;
            default -> 0.0F;
        };
    }
}
