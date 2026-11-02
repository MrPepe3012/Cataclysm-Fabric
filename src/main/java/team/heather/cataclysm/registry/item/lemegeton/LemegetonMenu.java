package team.heather.cataclysm.registry.item.lemegeton;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.CataclysmItems;

import java.util.List;
import java.util.function.IntSupplier;

/** A catalogue, not a storage container: no icon or player stack can be moved through this menu. */
public final class LemegetonMenu extends ChestMenu {
    private final ServerPlayer owner;
    private final ItemStack book;
    private final int bookSlot;
    private final IntSupplier capacity;
    private final IntSupplier requiredTier;

    public LemegetonMenu(int id, Inventory inventory, ItemStack book, IntSupplier capacity, IntSupplier requiredTier) {
        super(MenuType.GENERIC_9x6, id, inventory, new SimpleContainer(54), 6);
        this.owner = (ServerPlayer) inventory.player;
        this.book = book;
        this.bookSlot = inventory.getSelectedSlot();
        this.capacity = capacity;
        this.requiredTier = requiredTier;
        this.populate();
    }

    private void populate() {
        var container = this.getContainer();
        for (int slot = 0; slot < 54; slot++) {
            if (slot < 9 || slot >= 45 || slot % 9 == 0 || slot % 9 == 8
                    || List.of(10, 16, 19, 25, 28, 34, 37, 43).contains(slot)) {
                var pane = new ItemStack(Items.WHITE_STAINED_GLASS_PANE);
                pane.set(DataComponents.ITEM_NAME, Component.empty());
                container.setItem(slot, pane);
            }
        }
        var state = LemegetonItem.stateOf(this.book);
        for (var upgrade : LemegetonUpgrade.values()) {
            container.setItem(upgrade.slot(), upgrade.menuIcon(state));
        }
        var cost = new ItemStack(CataclysmItems.CATACLYSM_UPGRADE);
        cost.set(CataclysmDataComponents.UPGRADE_TIER, this.requiredTier.getAsInt());
        cost.set(DataComponents.LORE, new ItemLore(List.of(
                Component.translatable("menu.cataclysm.lemegeton.required_tier", this.requiredTier.getAsInt()).withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false)),
                Component.translatable("menu.cataclysm.lemegeton.offhand").withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false)))));
        container.setItem(49, cost);
    }

    @Override
    public boolean stillValid(Player player) {
        return player == this.owner && player.isAlive() && !player.isSpectator()
                && player.getInventory().getSelectedSlot() == this.bookSlot
                && player.getInventory().getItem(this.bookSlot) == this.book
                && this.book.getItem() instanceof LemegetonItem
                && LemegetonEvents.ALLOW_USE.invoker().allow(this.owner, this.book);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }


    @Override
    public void clicked(int slot, int button, ClickType clickType, Player player) {
        if (player != this.owner || this.owner.containerMenu != this || !this.stillValid(player)) {
            return;
        }
        if (clickType != ClickType.PICKUP || button != 0) {
            return;
        }
        for (var upgrade : LemegetonUpgrade.values()) {
            if (upgrade.slot() == slot) {
                this.purchase(upgrade);
                return;
            }
        }
    }

    private void purchase(LemegetonUpgrade upgrade) {
        var state = LemegetonItem.stateOf(this.book);
        var payment = this.owner.getOffhandItem();
        if (!payment.is(CataclysmItems.CATACLYSM_UPGRADE)
                || payment.getOrDefault(CataclysmDataComponents.UPGRADE_TIER, 1) != this.requiredTier.getAsInt()) {
            this.reject("menu.cataclysm.lemegeton.required_tier", this.requiredTier.getAsInt());
            return;
        }
        if (state.totalLevels() >= this.capacity.getAsInt()) {
            this.reject("message.cataclysm.lemegeton.limit");
            return;
        }
        if (state.level(upgrade) >= upgrade.maxLevel()) {
            this.reject("message.cataclysm.lemegeton.maxed");
            return;
        }
        var updated = state.upgrade(upgrade);
        this.book.set(CataclysmDataComponents.LEMEGETON, updated);
        payment.shrink(1);
        this.owner.getInventory().setChanged();
        this.owner.playNotifySound(SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1, 0.85F);
        this.owner.playNotifySound(SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1, 0.85F);
        this.owner.server.getPlayerList().broadcastSystemMessage(Component.translatable(
                updated.level(upgrade) == 1 ? "message.cataclysm.lemegeton.unlocked" : "message.cataclysm.lemegeton.upgraded",
                this.owner.getDisplayName(), Component.translatable(upgrade.translationKey()), updated.level(upgrade)), false);
        this.owner.closeContainer();
    }

    private void reject(String key, Object... arguments) {
        this.owner.playNotifySound(SoundEvents.SHIELD_BREAK.value(), SoundSource.PLAYERS, 1, 1.05F);
        this.owner.displayClientMessage(Component.translatable(key, arguments).withStyle(ChatFormatting.RED), true);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.owner.playNotifySound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.PLAYERS, 1, 1.65F);
        this.owner.playNotifySound(SoundEvents.SHULKER_BOX_CLOSE, SoundSource.PLAYERS, 0.6F, 2);
    }
}
