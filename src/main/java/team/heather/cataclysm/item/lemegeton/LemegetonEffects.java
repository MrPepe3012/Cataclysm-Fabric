package team.heather.cataclysm.item.lemegeton;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.component.data.lemegeton.LemegetonState;

import java.util.function.IntSupplier;

public final class LemegetonEffects {

    private static final int SECOND_TIER_WEEK = 2;
    private static final int THIRD_TIER_WEEK = 4;
    private static final int LEVELS_PER_WEEK = 2;

    private LemegetonEffects() {}

    /** The caller normalizes world progression to a week numbered from one. */
    public static void initialize(IntSupplier weekNumber) {
        PayloadTypeRegistry.playC2S().register(OpenLemegetonPayload.TYPE, OpenLemegetonPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenLemegetonPayload.TYPE, (payload, context) ->
            open(context.player(), weekNumber));
        UseBlockCallback.EVENT.register((player, level, hand, hit) ->
            player.getItemInHand(hand).getItem() instanceof LemegetonItem item
                ? item.use(level, player, hand)
                : InteractionResult.PASS);
    }

    public static int requiredTier(int weekNumber) {
        return weekNumber >= THIRD_TIER_WEEK ? 3 : weekNumber >= SECOND_TIER_WEEK ? 2 : 1;
    }

    public static int capacity(int weekNumber, LemegetonState state) {
        return (int) Math.min(Integer.MAX_VALUE, Math.max(1L, weekNumber) * LEVELS_PER_WEEK + state.bonusSlots());
    }

    /** Searches the whole inventory, off-hand included. An empty stack means the player cannot pay. */
    public static ItemStack findPayment(ServerPlayer player, int requiredTier) {
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            var stack = inventory.getItem(slot);
            if (stack.is(CataclysmRegistry.ITEMS.CATACLYSM_UPGRADE)
                && stack.getOrDefault(CataclysmRegistry.DATA_COMPONENTS.UPGRADE_TIER, 1) == requiredTier) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static void open(ServerPlayer player, IntSupplier weekNumber) {
        var book = player.getMainHandItem();
        if (!canOpen(player, book)) {
            return;
        }
        var provider = new SimpleMenuProvider((id, inventory, owner) -> new ServerLemegetonMenu(
            id, player, book,
            () -> capacityOf(book, weekNumber),
            () -> requiredTier(weekNumber.getAsInt())),
            Component.translatable("menu.cataclysm.lemegeton.title"));
        if (player.openMenu(provider).isPresent()) {
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

    private static int capacityOf(ItemStack book, IntSupplier weekNumber) {
        return capacity(weekNumber.getAsInt(), LemegetonItem.stateOf(book));
    }
}
