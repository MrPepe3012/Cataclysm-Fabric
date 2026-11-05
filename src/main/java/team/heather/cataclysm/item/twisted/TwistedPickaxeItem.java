package team.heather.cataclysm.item.twisted;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.component.data.TwistedPickaxeMode;

/** A mining tool with two persistent modes, toggled by sneak-use. */
public final class TwistedPickaxeItem extends Item {

    private static final int TOGGLE_COOLDOWN = 20;

    public TwistedPickaxeItem(Properties properties) {
        super(properties.component(CataclysmRegistry.DATA_COMPONENTS.PICKAXE_MODE, TwistedPickaxeMode.FORTUNE));
    }

    @Override
    public @NotNull InteractionResult use(Level level,
                                          Player player,
                                          InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            return super.use(level, player, hand);
        }
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) {
            stack.set(CataclysmRegistry.DATA_COMPONENTS.PICKAXE_MODE, stack.getOrDefault(CataclysmRegistry.DATA_COMPONENTS.PICKAXE_MODE, TwistedPickaxeMode.FORTUNE).toggled());
            player.getCooldowns().addCooldown(stack, TOGGLE_COOLDOWN);
            level.playSound(null, player.blockPosition(), SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 0.7F, 0.6F);
        }
        return InteractionResult.SUCCESS;
    }
}
