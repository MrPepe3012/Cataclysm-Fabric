package team.heather.cataclysm.registry.item.twisted;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

/** A mining tool with two persistent visual modes, toggled by sneak-use. */
public final class TwistedPickaxeItem extends Item {
    public TwistedPickaxeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) return super.use(level, player, hand);
        if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) return InteractionResult.FAIL;
        if (!level.isClientSide()) {
            var stack = player.getItemInHand(hand);
            int mode = stack.getOrDefault(CataclysmDataComponents.PICKAXE_MODE, 0);
            stack.set(CataclysmDataComponents.PICKAXE_MODE, mode == 0 ? 1 : 0);
            player.getCooldowns().addCooldown(stack, 20);
            level.playSound(null, player.blockPosition(), SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 0.7F, 0.6F);
        }
        return InteractionResult.SUCCESS;
    }
}
