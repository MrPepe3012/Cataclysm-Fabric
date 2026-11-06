package team.heather.cataclysm.component.level.ragnarok;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import team.heather.cataclysm.component.CataclysmCardinalComponents;

public final class RagnarokComponent implements AutoSyncedComponent {

    private final Level level;

    private boolean active;

    public RagnarokComponent(Level level) {
        this.level = level;
    }

    public void setActive(boolean active) {
        this.active = active;
        CataclysmCardinalComponents.RAGNAROK.sync(this.level);
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        this.active = tag.getBooleanOr("active", false);
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putBoolean("active", this.active);
    }
}
