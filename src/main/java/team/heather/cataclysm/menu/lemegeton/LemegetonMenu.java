package team.heather.cataclysm.menu.lemegeton;

import net.minecraft.network.chat.Component;
import team.heather.cataclysm.item.data.LemegetonComponent;
import team.heather.cataclysm.menu.CataclysmMenus;
import team.heather.cataclysm.menu.SyncedValueMenu;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

/** The client's copy of the catalogue: every value here is synced from {@link ServerLemegetonMenu}. */
public class LemegetonMenu extends SyncedValueMenu {

    protected static final List<LemegetonComponent.Upgrade> UPGRADES = List.of(LemegetonComponent.Upgrade.values());

    private final Map<LemegetonComponent.Upgrade, IntSupplier> levels = new EnumMap<>(LemegetonComponent.Upgrade.class);
    private final IntSupplier capacity;
    private final IntSupplier requiredTier;
    private final BooleanSupplier hasPayment;

    public LemegetonMenu(int id) {
        this(id, upgrade -> 0, () -> 0, () -> 0, () -> false);
    }

    protected LemegetonMenu(int id,
                            ToIntFunction<LemegetonComponent.Upgrade> level,
                            IntSupplier capacity,
                            IntSupplier requiredTier,
                            BooleanSupplier hasPayment) {
        super(CataclysmMenus.LEMEGETON, id);
        for (var upgrade : UPGRADES) {
            this.levels.put(upgrade, this.synced(() -> level.applyAsInt(upgrade)));
        }
        this.capacity = this.synced(capacity);
        this.requiredTier = this.synced(requiredTier);
        this.hasPayment = this.syncedFlag(hasPayment);
    }

    public int level(LemegetonComponent.Upgrade upgrade) {
        return this.levels.get(upgrade).getAsInt();
    }

    public int totalLevels() {
        int total = 0;
        for (var upgrade : UPGRADES) {
            total += this.level(upgrade);
        }
        return total;
    }

    public int capacity() {
        return this.capacity.getAsInt();
    }

    public int requiredTier() {
        return this.requiredTier.getAsInt();
    }

    /** Explains why the upgrade cannot be bought right now; empty means it can. */
    public Optional<Component> denial(LemegetonComponent.Upgrade upgrade) {
        if (this.level(upgrade) >= upgrade.maxLevel()) {
            return Optional.of(Component.translatable("message.cataclysm.lemegeton.maxed"));
        }
        if (this.totalLevels() >= this.capacity()) {
            return Optional.of(Component.translatable("message.cataclysm.lemegeton.limit"));
        }
        if (!this.hasPayment.getAsBoolean()) {
            return Optional.of(Component.translatable("menu.cataclysm.lemegeton.payment", this.requiredTier()));
        }
        return Optional.empty();
    }
}
