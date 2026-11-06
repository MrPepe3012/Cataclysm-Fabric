package team.heather.cataclysm.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

/** Both sides declare the same values in the same order: the server reads its source live and the client receives it. */
public abstract class SyncedValueMenu extends AbstractContainerMenu {

    protected SyncedValueMenu(MenuType<?> type, int id) {
        super(type, id);
    }

    protected final IntSupplier synced(IntSupplier source) {
        var value = new SyncedValue(source);
        this.addDataSlot(value);
        return value;
    }

    protected final BooleanSupplier syncedFlag(BooleanSupplier source) {
        var value = this.synced(() -> source.getAsBoolean() ? 1 : 0);
        return () -> value.getAsInt() != 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    /** The network sends these as shorts. Only the client receives a value, which replaces its source. */
    private static final class SyncedValue extends DataSlot implements IntSupplier {

        private IntSupplier source;

        private SyncedValue(IntSupplier source) {
            this.source = source;
        }

        @Override
        public int get() {
            return Math.min(this.source.getAsInt(), Short.MAX_VALUE);
        }

        @Override
        public void set(int value) {
            this.source = () -> value;
        }

        @Override
        public int getAsInt() {
            return this.get();
        }
    }
}
