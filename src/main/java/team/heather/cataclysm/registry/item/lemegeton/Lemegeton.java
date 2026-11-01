package team.heather.cataclysm.registry.item.lemegeton;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.CataclysmItems;

import java.util.function.IntSupplier;

public final class Lemegeton {
    private Lemegeton() {}

    /** The caller normalizes world progression to a week numbered from one. */
    public static void initialize(IntSupplier weekNumber) {
        PayloadTypeRegistry.playC2S().register(OpenLemegetonPayload.TYPE, OpenLemegetonPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenLemegetonPayload.TYPE, (payload, context) ->
                open(context.player(), weekNumber));
        UseBlockCallback.EVENT.register((player, level, hand, hit) ->
                player.getItemInHand(hand).getItem() instanceof LemegetonItem item
                        ? item.use(level, player, hand) : InteractionResult.PASS);
    }

    public static int requiredTier(int weekNumber) {
        return weekNumber >= 4 ? 3 : weekNumber >= 2 ? 2 : 1;
    }

    public static int capacity(int weekNumber, LemegetonState state) {
        return (int) Math.min(Integer.MAX_VALUE, Math.max(1L, weekNumber) * 2 + state.bonusSlots());
    }

    private static void open(ServerPlayer player, IntSupplier weekNumber) {
        ItemStack book = player.getMainHandItem();
        if (!(book.getItem() instanceof LemegetonItem) || player.isSpectator() || !player.isAlive()
                || player.containerMenu != player.inventoryMenu
                || !LemegetonEvents.ALLOW_USE.invoker().allow(player, book)) return;
        int max = capacity(weekNumber.getAsInt(), LemegetonItem.stateOf(book));
        var payment = player.getOffhandItem();
        boolean canPay = payment.is(CataclysmItems.CATACLYSM_UPGRADE)
                && payment.getOrDefault(CataclysmDataComponents.UPGRADE_TIER, 1) == requiredTier(weekNumber.getAsInt());
        if (canPay && LemegetonItem.stateOf(book).totalLevels() >= max) {
            player.displayClientMessage(Component.translatable("message.cataclysm.lemegeton.limit").withStyle(ChatFormatting.RED), true);
            player.playNotifySound(SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundSource.PLAYERS, 1, 1.27F);
            return;
        }
        var result = player.openMenu(new SimpleMenuProvider((id, inventory, owner) -> new LemegetonMenu(
                id, inventory, book,
                () -> capacity(weekNumber.getAsInt(), LemegetonItem.stateOf(book)),
                () -> requiredTier(weekNumber.getAsInt())),
                Component.translatable("menu.cataclysm.lemegeton.title", LemegetonItem.stateOf(book).totalLevels(), max)));
        if (result.isPresent()) {
            player.playNotifySound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.PLAYERS, 1, 1.25F);
            player.playNotifySound(SoundEvents.SHULKER_BOX_OPEN, SoundSource.PLAYERS, 0.6F, 2);
            if (canPay) player.playNotifySound(SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1, 1.75F);
        }
    }

}
