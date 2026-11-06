package team.heather.cataclysm.component.mortality;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import team.heather.cataclysm.component.CataclysmCardinalComponents;

public final class MortalityComponent implements AutoSyncedComponent {

    private static final String KEY = "mortality";
    private static final float DEFAULT = 1.0F;
    private static final float PRECISION = 10000.0F;

    private final Player player;

    private float mortality = DEFAULT;

    public MortalityComponent(Player player) {
        this.player = player;
    }

    public void set(float mortality) {
        this.mortality = normalize(mortality);
        CataclysmCardinalComponents.MORTALITY.sync(this.player);
    }

    public float get() {
        return this.mortality;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        this.mortality = normalize(tag.getFloatOr(KEY, DEFAULT));
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putFloat(KEY, this.mortality);
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer recipient) {
        return recipient == this.player;
    }

    private static float normalize(float mortality) {
        return Math.round(Math.clamp(mortality, 0.0F, 1.0F) * PRECISION) / PRECISION;
    }
}
