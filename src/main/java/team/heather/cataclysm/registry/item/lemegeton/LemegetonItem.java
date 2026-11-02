package team.heather.cataclysm.registry.item.lemegeton;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;

public final class LemegetonItem extends Item {
    public static final int EFFECT_DURATION_TICKS = 30 * 20;
    public static final int COOLDOWN_TICKS = 5 * 60 * 20;

    public LemegetonItem(Properties properties) {
        super(properties.stacksTo(1).component(CataclysmDataComponents.LEMEGETON, LemegetonState.EMPTY));
    }

    public static LemegetonState stateOf(ItemStack stack) {
        return stack.getOrDefault(CataclysmDataComponents.LEMEGETON, LemegetonState.EMPTY);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stateOf(stack).hasUpgrades();
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }
        var stack = player.getItemInHand(hand);
        if (player.isSpectator() || !LemegetonEvents.ALLOW_USE.invoker().allow(serverPlayer, stack)) {
            return InteractionResult.FAIL;
        }
        var state = stateOf(stack);
        long time = serverPlayer.server.overworld().getGameTime();
        long remaining = state.remainingCooldown(time);
        if (remaining > 0) {
            serverPlayer.displayClientMessage(Component.translatable("message.cataclysm.lemegeton.cooldown",
                    (remaining + 19) / 20).withStyle(ChatFormatting.GRAY), true);
            return InteractionResult.FAIL;
        }
        for (var upgrade : LemegetonUpgrade.values()) {
            upgrade.apply(serverPlayer, state.level(upgrade), EFFECT_DURATION_TICKS);
        }
        stack.set(CataclysmDataComponents.LEMEGETON, state.withCooldown(time + COOLDOWN_TICKS));
        serverPlayer.playNotifySound(SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 1, 0.57F);
        serverPlayer.playNotifySound(SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 1, 1.27F);
        serverPlayer.playNotifySound(SoundEvents.ELDER_GUARDIAN_DEATH, SoundSource.PLAYERS, 1, 0.67F);
        return InteractionResult.SUCCESS;
    }
}
