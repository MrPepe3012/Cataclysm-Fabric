package team.heather.cataclysm.item.feature.lemegeton;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.data.LemegetonComponent;
import team.heather.cataclysm.item.data.UpgradeTierComponent;
import team.heather.cataclysm.menu.CataclysmMenus;
import team.heather.cataclysm.menu.lemegeton.ServerLemegetonMenu;
import team.heather.cataclysm.component.level.phase.Phase;

import java.util.function.Supplier;

public final class LemegetonProcessor {

    private static final Phase SECOND_TIER_PHASE = Phase.PHASE_2;
    private static final Phase THIRD_TIER_PHASE = Phase.PHASE_4;
    private static final int LEVELS_PER_PHASE = 2;

    private LemegetonProcessor() {}

    public static void initialize() {
        UseBlockCallback.EVENT.register((player, level, hand, hit) ->
            player.getItemInHand(hand).getItem() instanceof LemegetonItem item
                ? item.use(level, player, hand)
                : InteractionResult.PASS);
    }

    public static int requiredTier(Phase phase) {
        return phase.isAtLeast(THIRD_TIER_PHASE) ? 3 : phase.isAtLeast(SECOND_TIER_PHASE) ? 2 : 1;
    }

    public static int capacity(Phase phase, LemegetonComponent state) {
        return (int) Math.min(Integer.MAX_VALUE, (phase.ordinal() + 1L) * LEVELS_PER_PHASE + state.bonusSlots());
    }

    /** Searches the whole inventory, off-hand included. An empty stack means the player cannot pay. */
    public static ItemStack findPayment(ServerPlayer player, int requiredTier) {
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            var stack = inventory.getItem(slot);
            if (stack.is(CataclysmItems.CATACLYSM_UPGRADE)
                && stack.getOrDefault(CataclysmDataComponents.UPGRADE_TIER, UpgradeTierComponent.FIRST).tier() == requiredTier) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static void open(ServerPlayer player, Supplier<Phase> phase) {
        var book = player.getMainHandItem();
        if (!canOpen(player, book)) {
            return;
        }
        boolean opened = CataclysmMenus.open(player, "menu.cataclysm.lemegeton.title", id -> new ServerLemegetonMenu(
            id, player, book,
            () -> capacityOf(book, phase),
            () -> requiredTier(phase.get())));
        if (opened) {
            player.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1, 1);
        }
    }

    private static boolean canOpen(ServerPlayer player, ItemStack book) {
        return book.getItem() instanceof LemegetonItem
            && !player.isSpectator()
            && player.isAlive()
            && player.containerMenu == player.inventoryMenu
            && AllowLemegetonUseCallback.EVENT.invoker().allowUse(player, book);
    }

    private static int capacityOf(ItemStack book, Supplier<Phase> phase) {
        return capacity(phase.get(), LemegetonItem.stateOf(book));
    }
}
