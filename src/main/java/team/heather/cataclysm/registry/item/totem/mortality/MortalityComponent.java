package team.heather.cataclysm.registry.item.totem.mortality;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.Component;

public final class MortalityComponent implements Component {

    private static final String KEY = "mortality";
    private static final float DEFAULT = 1.0F;

    private float mortality = DEFAULT;

    @Override
    public void readFromNbt(CompoundTag tag,
                            HolderLookup.Provider lookup) {
        set(tag.getFloatOr(KEY, DEFAULT));
    }

    @Override
    public void writeToNbt(CompoundTag tag,
                           HolderLookup.Provider lookup) {
        tag.putFloat(KEY, this.mortality);
    }

    public void set(float mortality) {
        this.mortality = Math.round(Math.clamp(mortality, 0.0F, 1.0F) * 10000) / 10000.0F;
    }

    public float get() {
        return this.mortality;
    }
}
