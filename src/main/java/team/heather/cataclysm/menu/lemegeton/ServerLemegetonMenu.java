package team.heather.cataclysm.menu.lemegeton;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.data.LemegetonComponent;
import team.heather.cataclysm.item.feature.lemegeton.AllowLemegetonUseCallback;
import team.heather.cataclysm.item.feature.lemegeton.LemegetonProcessor;
import team.heather.cataclysm.item.feature.lemegeton.LemegetonItem;

import java.util.function.IntSupplier;

/** Owns every rule of the catalogue. The client can only ask for an upgrade by its index. */
public final class ServerLemegetonMenu extends LemegetonMenu {

    private static final float DEFAULT_PITCH = 1;
    private static final float PURCHASE_PITCH = 0.85F;

    private final ServerPlayer owner;
    private final ItemStack book;
    private final int bookSlot;

    public ServerLemegetonMenu(int id,
                               ServerPlayer owner,
                               ItemStack book,
                               IntSupplier capacity,
                               IntSupplier requiredTier) {
        super(id, upgrade -> LemegetonItem.stateOf(book).level(upgrade), capacity, requiredTier,
            () -> !LemegetonProcessor.findPayment(owner, requiredTier.getAsInt()).isEmpty());
        this.owner = owner;
        this.book = book;
        this.bookSlot = owner.getInventory().getSelectedSlot();
    }

    @Override
    public boolean stillValid(Player player) {
        return player == this.owner && this.isHoldingBook() && this.canUseBook();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (player != this.owner || id < 0 || id >= UPGRADES.size()) {
            return false;
        }
        this.purchase(UPGRADES.get(id));
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.playSound(SoundEvents.BOOK_PUT, DEFAULT_PITCH);
    }

    private boolean isHoldingBook() {
        var inventory = this.owner.getInventory();
        return inventory.getSelectedSlot() == this.bookSlot
            && inventory.getItem(this.bookSlot) == this.book
            && this.book.getItem() instanceof LemegetonItem;
    }

    private boolean canUseBook() {
        return this.owner.isAlive()
            && !this.owner.isSpectator()
            && AllowLemegetonUseCallback.EVENT.invoker().allowUse(this.owner, this.book);
    }

    private void purchase(LemegetonComponent.Upgrade upgrade) {
        var denial = this.denial(upgrade);
        if (denial.isPresent()) {
            this.reject(denial.get());
            return;
        }
        var state = LemegetonItem.stateOf(this.book).upgrade(upgrade);
        this.book.set(CataclysmDataComponents.LEMEGETON, state);
        LemegetonProcessor.findPayment(this.owner, this.requiredTier()).shrink(1);
        this.owner.getInventory().setChanged();
        this.owner.inventoryMenu.broadcastChanges();
        this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, PURCHASE_PITCH);
        this.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, PURCHASE_PITCH);
        this.announce(upgrade, state.level(upgrade));
    }

    private void reject(Component reason) {
        this.playSound(SoundEvents.VAULT_INSERT_ITEM_FAIL, DEFAULT_PITCH);
        this.owner.displayClientMessage(reason.copy().withStyle(ChatFormatting.RED), true);
    }

    private void announce(LemegetonComponent.Upgrade upgrade, int level) {
        String key = level == 1 ? "message.cataclysm.lemegeton.unlocked" : "message.cataclysm.lemegeton.upgraded";
        this.owner.server.getPlayerList().broadcastSystemMessage(Component.translatable(
            key, this.owner.getDisplayName(), Component.translatable(upgrade.translationKey()), level), false);
    }

    private void playSound(SoundEvent sound, float pitch) {
        this.owner.playNotifySound(sound, SoundSource.PLAYERS, 1, pitch);
    }
}
