package team.heather.cataclysm.registry.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;

/** A mining tool with two persistent visual modes, toggled by sneak-use. */
public final class TwistedPickaxeItem extends Item {
    public TwistedPickaxeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) return super.use(level, player, hand);
        if (!level.isClientSide()) {
            var stack = player.getItemInHand(hand);
            int mode = stack.getOrDefault(CataclysmDataComponents.PICKAXE_MODE, 0);
            stack.set(CataclysmDataComponents.PICKAXE_MODE, mode == 0 ? 1 : 0);
        }
        return InteractionResult.SUCCESS;
    }
}
