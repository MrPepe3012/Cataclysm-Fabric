package com.cataclysm.item.totem;

public class ArcaneTotemItem extends TotemItem {
    protected ArcaneTotemItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getMortalityReduction(int week) {
        return switch (week) {
            case 3 -> 0.5F;
            case 4 -> 1.0F;
            case 5 -> 5.0F;
            case 6 -> 50.0F;
            default -> 0.0F;
        };
    }
}
