package com.cataclysm.server;

import com.cataclysm.effect.CataclysmMobEffects;
import com.cataclysm.effect.DisperMobEffect;
import com.cataclysm.item.totem.UseTotemCallback;
import com.cataclysm.item.totem.mortality.MortalityAftereffects;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ServerEventInitializer {
    private ServerEventInitializer() {}

    public static void initialize() {
        PlayerBlockBreakEvents.BEFORE.register(ServerEventInitializer::handleBeforeBlockBreak);
        UseBlockCallback.EVENT.register(ServerEventInitializer::handleUseBlock);
        UseTotemCallback.EVENT.register(ServerEventInitializer::handleUseTotem);
    }

    private static boolean handleBeforeBlockBreak(Level world, @NotNull Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return DisperMobEffect.findEffect(player).map(disper -> disper.onBreak(player, state)).orElse(true);
    }

    private static InteractionResult handleUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        return DisperMobEffect.findEffect(player)
                .<InteractionResult>map(disper -> disper.onPlace(player) ? InteractionResult.PASS : InteractionResult.FAIL)
                .orElse(InteractionResult.PASS);
    }

    private static DeathProtection handleUseTotem(LivingEntity entity, ItemStack totemStack, DeathProtection deathProtection) {
        if (!(entity instanceof Player player)) return deathProtection;

        if (player.hasEffect(CataclysmMobEffects.MORTEM)) {
            //TODO Add mortem feedback
            return null;
        }

        deathProtection = MortalityAftereffects.onResurrect(player, totemStack, deathProtection);

        return deathProtection;
    }
}
