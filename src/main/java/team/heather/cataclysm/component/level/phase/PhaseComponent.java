package team.heather.cataclysm.component.level.phase;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import team.heather.cataclysm.component.CataclysmCardinalComponents;

public final class PhaseComponent implements AutoSyncedComponent {

    private final Level level;

    private Phase phase = Phase.PHASE_1;

    public PhaseComponent(Level level) {
        this.level = level;
    }

    public void set(Phase phase) {
        this.phase = phase;
        CataclysmCardinalComponents.PHASE.sync(this.level);
    }

    public Phase get() {
        return this.phase;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        this.phase = Phase.CODEC.byName(tag.getStringOr("phase", ""), Phase.PHASE_1);
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putString("phase", this.phase.getSerializedName());
    }
}
