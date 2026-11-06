package team.heather.cataclysm.item.feature.twisted;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.data.TwistedPickaxeComponent;

/** A mining tool with two persistent modes, toggled by sneak-use. */
public final class TwistedPickaxeItem extends Item {

    private static final int TOGGLE_COOLDOWN = 20;

    public TwistedPickaxeItem(Properties properties) {
        super(properties.component(CataclysmDataComponents.TWISTED_PICKAXE, TwistedPickaxeComponent.SILK_TOUCH));
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
            stack.set(CataclysmDataComponents.TWISTED_PICKAXE, stack.getOrDefault(CataclysmDataComponents.TWISTED_PICKAXE, TwistedPickaxeComponent.SILK_TOUCH).toggled());
            player.getCooldowns().addCooldown(stack, TOGGLE_COOLDOWN);
            level.playSound(null, player.blockPosition(), SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 0.7F, 0.6F);
        }
        return InteractionResult.SUCCESS;
    }
}
