package com.cataclysm.player.mortality;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.Component;

public class MortalityComponent implements Component {
    private static final String KEY_MORTALITY = "mortality";
    private static final float DEFAULT_MORTALITY = 1.0F;

    private float mortality = DEFAULT_MORTALITY;

    public void set(float mortality) {
        this.mortality = Math.clamp(0.0F, mortality, 1.0F);
    }

    public float get() {
        return this.mortality;
    }
    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        this.mortality = tag.getFloatOr(KEY_MORTALITY, DEFAULT_MORTALITY);
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putFloat(KEY_MORTALITY, this.mortality);
    }
}