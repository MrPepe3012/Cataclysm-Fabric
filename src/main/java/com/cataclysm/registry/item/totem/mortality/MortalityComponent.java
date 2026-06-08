package com.cataclysm.registry.item.totem.mortality;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.Component;

public class MortalityComponent implements Component {
    private static final String KEY = "mortality";
    private static final float DEFAULT = 1.0F;

    private float mortality = DEFAULT;

    public void set(float mortality) {
        this.mortality = Math.clamp(0.0F, mortality, 1.0F);
    }

    public float get() {
        return Math.round(this.mortality * 100) / 100.0F;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        this.mortality = tag.getFloatOr(KEY, DEFAULT);
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putFloat(KEY, this.mortality);
    }
}