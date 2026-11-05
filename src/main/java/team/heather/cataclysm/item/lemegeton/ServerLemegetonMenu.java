package team.heather.cataclysm.item.lemegeton;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.component.data.lemegeton.LemegetonUpgrade;

import java.util.function.IntSupplier;

/** Owns every rule of the catalogue. The client can only ask for an upgrade by its index. */
public final class ServerLemegetonMenu extends LemegetonMenu {

    private final ServerPlayer owner;
    private final ItemStack book;
    private final int bookSlot;

    public ServerLemegetonMenu(int id, ServerPlayer owner, ItemStack book, IntSupplier capacity, IntSupplier requiredTier) {
        super(id, new BookData(owner, book, capacity, requiredTier));
        this.owner = owner;
        this.book = book;
        this.bookSlot = owner.getInventory().getSelectedSlot();
    }

    @Override
    public boolean stillValid(Player player) {
        return player == this.owner && player.isAlive() && !player.isSpectator()
            && player.getInventory().getSelectedSlot() == this.bookSlot
            && player.getInventory().getItem(this.bookSlot) == this.book
            && this.book.getItem() instanceof LemegetonItem
            && AllowLemegetonUseCallback.EVENT.invoker().allowUse(this.owner, this.book);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (player != this.owner || id < 0 || id >= UPGRADES.length) {
            return false;
        }
        this.purchase(UPGRADES[id]);
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.playSound(SoundEvents.BOOK_PUT, 1);
    }

    private void purchase(LemegetonUpgrade upgrade) {
        var denial = this.denial(upgrade);
        if (denial.isPresent()) {
            this.reject(denial.get());
            return;
        }
        var state = LemegetonItem.stateOf(this.book).upgrade(upgrade);
        this.book.set(CataclysmRegistry.DATA_COMPONENTS.LEMEGETON, state);
        LemegetonEffects.findPayment(this.owner, this.requiredTier()).shrink(1);
        this.owner.getInventory().setChanged();
        this.owner.inventoryMenu.broadcastChanges();
        this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 0.85F);
        this.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.85F);
        this.announce(upgrade, state.level(upgrade));
    }

    private void reject(Component reason) {
        this.playSound(SoundEvents.VAULT_INSERT_ITEM_FAIL, 1);
        this.owner.displayClientMessage(reason.copy().withStyle(ChatFormatting.RED), true);
    }

    private void announce(LemegetonUpgrade upgrade, int level) {
        String key = level == 1 ? "message.cataclysm.lemegeton.unlocked" : "message.cataclysm.lemegeton.upgraded";
        this.owner.server.getPlayerList().broadcastSystemMessage(Component.translatable(
            key, this.owner.getDisplayName(), Component.translatable(upgrade.translationKey()), level), false);
    }

    private void playSound(SoundEvent sound, float pitch) {
        this.owner.playNotifySound(sound, SoundSource.PLAYERS, 1, pitch);
    }

    /** Reads the book live, so the client always sees what a purchase would be checked against. */
    private record BookData(ServerPlayer owner, ItemStack book, IntSupplier capacity, IntSupplier requiredTier) implements ContainerData {

        @Override
        public int get(int index) {
            if (index == CAPACITY) {
                return Math.min(this.capacity.getAsInt(), Short.MAX_VALUE);
            }
            if (index == REQUIRED_TIER) {
                return this.requiredTier.getAsInt();
            }
            if (index == HAS_PAYMENT) {
                return LemegetonEffects.findPayment(this.owner, this.requiredTier.getAsInt()).isEmpty() ? 0 : 1;
            }
            return LemegetonItem.stateOf(this.book).level(UPGRADES[index]);
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() {
            return DATA_SIZE;
        }
    }
}
