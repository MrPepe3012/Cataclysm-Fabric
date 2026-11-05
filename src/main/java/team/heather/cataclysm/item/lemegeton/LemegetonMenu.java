package team.heather.cataclysm.item.lemegeton;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.component.data.lemegeton.LemegetonUpgrade;

import java.util.Optional;

/** The client's copy of the catalogue: every value here is synced from {@link ServerLemegetonMenu}. */
public class LemegetonMenu extends AbstractContainerMenu {

    protected static final LemegetonUpgrade[] UPGRADES = LemegetonUpgrade.values();
    protected static final int CAPACITY = UPGRADES.length;
    protected static final int REQUIRED_TIER = CAPACITY + 1;
    protected static final int HAS_PAYMENT = REQUIRED_TIER + 1;
    protected static final int DATA_SIZE = HAS_PAYMENT + 1;

    private final ContainerData data;

    public LemegetonMenu(int id, Inventory inventory) {
        this(id, new SimpleContainerData(DATA_SIZE));
    }

    protected LemegetonMenu(int id, ContainerData data) {
        super(CataclysmRegistry.MENUS.LEMEGETON, id);
        this.data = data;
        this.addDataSlots(data);
    }

    public int level(LemegetonUpgrade upgrade) {
        return this.data.get(upgrade.ordinal());
    }

    public int totalLevels() {
        int total = 0;
        for (var upgrade : UPGRADES) {
            total += this.level(upgrade);
        }
        return total;
    }

    public int capacity() {
        return this.data.get(CAPACITY);
    }

    public int requiredTier() {
        return this.data.get(REQUIRED_TIER);
    }

    /** Explains why the upgrade cannot be bought right now; empty means it can. */
    public Optional<Component> denial(LemegetonUpgrade upgrade) {
        if (this.level(upgrade) >= upgrade.maxLevel()) {
            return Optional.of(Component.translatable("message.cataclysm.lemegeton.maxed"));
        }
        if (this.totalLevels() >= this.capacity()) {
            return Optional.of(Component.translatable("message.cataclysm.lemegeton.limit"));
        }
        if (this.data.get(HAS_PAYMENT) == 0) {
            return Optional.of(Component.translatable("menu.cataclysm.lemegeton.payment", this.requiredTier()));
        }
        return Optional.empty();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
