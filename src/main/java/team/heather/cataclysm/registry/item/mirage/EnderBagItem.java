package team.heather.cataclysm.registry.item.mirage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class EnderBagItem extends Item {

    public EnderBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level,
                                          Player player,
                                          InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider((id, inventory, owner) ->
                    ChestMenu.threeRows(id, inventory, serverPlayer.getEnderChestInventory()),
                    Component.translatable("container.enderchest")));
            player.getCooldowns().addCooldown(stack, 20);
            level.playSound(null, player.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 0.8F, 1.25F);
        }
        return InteractionResult.SUCCESS;
    }
}
