package team.heather.cataclysm.registry.item.mirage;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.PassiveItemEffects;
import team.heather.cataclysm.registry.item.arcane.ArcaneMaceItem;
import team.heather.cataclysm.registry.sound.SoundMashup;

public final class MirageMaceItem extends ArcaneMaceItem {
    public MirageMaceItem(Properties properties) {
        super(properties.component(CataclysmDataComponents.MACE_MODE, 0));
    }

    @Override
    protected int windChargeCooldown() {
        return 15;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;
        int mode = stack.getOrDefault(CataclysmDataComponents.MACE_MODE, 0);
        if (mode == 0) {
            var result = super.use(level, player, hand);
            if (player.isShiftKeyDown() && player instanceof ServerPlayer) {
                stack.set(CataclysmDataComponents.MACE_MODE, 1);
                player.getCooldowns().addCooldown(stack, 5);
                SoundMashup.MIRAGE_HOOK_MODE.playAtEntity(player);
            }
            return result;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) {
                stack.set(CataclysmDataComponents.MACE_MODE, 0);
                player.getCooldowns().addCooldown(stack, 5);
                SoundMashup.MIRAGE_NORMAL_MODE.playAtEntity(player);
            } else {
                var look = player.getLookAngle();
                player.setDeltaMovement(look.x * 2.15, look.y * 2.44, look.z * 2.15);
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
                player.getCooldowns().addCooldown(stack, 80);
                PassiveItemEffects.startHookTrail(serverPlayer);
                SoundMashup.MIRAGE_HOOK.playAtEntity(player);
                serverPlayer.serverLevel().sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 3, 0, 0, 0, 0);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
