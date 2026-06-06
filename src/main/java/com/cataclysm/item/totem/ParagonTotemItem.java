package com.cataclysm.item.totem;

public class ParagonTotemItem extends TotemItem {
    protected ParagonTotemItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getMortalityReduction(int week) {
        return 0.0F;
    }
}
