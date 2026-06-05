package com.cataclysm.mortality;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.Component;

public class MortalityComponent implements Component {
    private static final String KEY_MORTALITY = "mortality";
    private static final String KEY_SEVERITY = "severity";
    private static final float DEFAULT_MORTALITY = 1.0F;
    private static final int DEFAULT_SEVERITY = 0;

    private float mortality = DEFAULT_MORTALITY;
    private int severity = DEFAULT_SEVERITY;
    
    public void setMortality(float mortality) {
        this.mortality = Math.clamp(0.0F, mortality, 1.0F);
    }

    public float getMortality() {
        return this.mortality;
    }

    public float getSeverity() {
        return this.severity;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        this.mortality = tag.getFloatOr(KEY_MORTALITY, DEFAULT_MORTALITY);
        this.severity = tag.getIntOr(KEY_SEVERITY, DEFAULT_SEVERITY);
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putFloat(KEY_MORTALITY, this.mortality);
        tag.putInt(KEY_SEVERITY, this.severity);
    }
}
