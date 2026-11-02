package team.heather.cataclysm.registry.item.mirage;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.arcane.ArcaneMaceItem;
import team.heather.cataclysm.registry.item.effect.PassiveItemEffects;
import team.heather.cataclysm.registry.sound.SoundMashup;

public final class MirageMaceItem extends ArcaneMaceItem {

    private static final int WIND_MODE = 0;
    private static final int HOOK_MODE = 1;
    private static final int SWITCH_COOLDOWN = 5;
    private static final int HOOK_COOLDOWN = 80;

    public MirageMaceItem(Properties properties) {
        super(properties.component(CataclysmDataComponents.MACE_MODE, WIND_MODE));
    }

    @Override
    protected int windChargeCooldown() {
        return 15;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }
        if (stack.getOrDefault(CataclysmDataComponents.MACE_MODE, WIND_MODE) == WIND_MODE) {
            return this.useWindMode(level, player, hand, stack);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) {
                this.switchMode(player, stack, WIND_MODE);
                SoundMashup.MIRAGE_NORMAL_MODE.playAtEntity(player);
            } else {
                this.launch(serverPlayer, stack);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult useWindMode(Level level, Player player, InteractionHand hand, ItemStack stack) {
        InteractionResult result = super.use(level, player, hand);
        if (player.isShiftKeyDown() && player instanceof ServerPlayer) {
            this.switchMode(player, stack, HOOK_MODE);
            SoundMashup.MIRAGE_HOOK_MODE.playAtEntity(player);
        }
        return result;
    }

    private void switchMode(Player player, ItemStack stack, int mode) {
        stack.set(CataclysmDataComponents.MACE_MODE, mode);
        player.getCooldowns().addCooldown(stack, SWITCH_COOLDOWN);
    }

    private void launch(ServerPlayer player, ItemStack stack) {
        var look = player.getLookAngle();
        player.setDeltaMovement(look.x * 2.15, look.y * 2.44, look.z * 2.15);
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
        player.getCooldowns().addCooldown(stack, HOOK_COOLDOWN);
        PassiveItemEffects.startHookTrail(player);
        SoundMashup.MIRAGE_HOOK.playAtEntity(player);
        player.serverLevel().sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 3, 0, 0, 0, 0);
    }
}
