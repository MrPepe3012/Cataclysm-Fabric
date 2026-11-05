package team.heather.cataclysm.item.lemegeton;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.component.data.lemegeton.LemegetonState;
import team.heather.cataclysm.component.data.lemegeton.LemegetonUpgrade;

public final class LemegetonItem extends Item {

    public static final int EFFECT_DURATION_TICKS = 30 * 20;
    public static final int COOLDOWN_TICKS = 5 * 60 * 20;

    public LemegetonItem(Properties properties) {
        super(properties.stacksTo(1).component(CataclysmRegistry.DATA_COMPONENTS.LEMEGETON, LemegetonState.EMPTY));
    }

    public static LemegetonState stateOf(ItemStack stack) {
        return stack.getOrDefault(CataclysmRegistry.DATA_COMPONENTS.LEMEGETON, LemegetonState.EMPTY);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stateOf(stack).hasUpgrades();
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }
        if (player.isSpectator() || !AllowLemegetonUseCallback.EVENT.invoker().allowUse(serverPlayer, stack)) {
            return InteractionResult.FAIL;
        }
        var state = stateOf(stack);
        for (var upgrade : LemegetonUpgrade.values()) {
            upgrade.apply(serverPlayer, state.level(upgrade), EFFECT_DURATION_TICKS);
        }
        player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
        serverPlayer.playNotifySound(SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 1, 0.57F);
        serverPlayer.playNotifySound(SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 1, 1.27F);
        serverPlayer.playNotifySound(SoundEvents.ELDER_GUARDIAN_DEATH, SoundSource.PLAYERS, 1, 0.67F);
        return InteractionResult.SUCCESS;
    }
}
