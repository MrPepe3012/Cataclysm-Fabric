package com.cataclysm.server;

import com.cataclysm.Cataclysm;
import com.cataclysm.chat.CataclysmBroadcast;
import com.cataclysm.command.MortalityCommand;
import com.cataclysm.registry.item.totem.UseTotemCallback;
import com.cataclysm.registry.item.totem.mortality.MortalityAftereffects;
import com.cataclysm.registry.CataclysmMobEffects;
import com.cataclysm.registry.effect.DisperMobEffect;
import com.cataclysm.registry.sound.SoundMashup;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
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
        ServerLifecycleEvents.SERVER_STARTING.register(ServerEventInitializer::handleServerStarting);
        PlayerBlockBreakEvents.BEFORE.register(ServerEventInitializer::handleBeforeBlockBreak);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(ServerEventInitializer::handleAllowDamage);
        UseBlockCallback.EVENT.register(ServerEventInitializer::handleUseBlock);
        UseTotemCallback.EVENT.register(ServerEventInitializer::handleUseTotem);
        CommandRegistrationCallback.EVENT.register(ServerEventInitializer::handleCommandRegistration);
    }

    private static void handleServerStarting(MinecraftServer server) {
        Cataclysm.setServer(server);
    }

    private static boolean handleBeforeBlockBreak(Level world, @NotNull Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        // Disper Effect Logic
        if (player.hasEffect(CataclysmMobEffects.DISPER) && DisperMobEffect.bypass(state.getBlock())) {
            SoundMashup.DISPER.playAtEntity(player);
            return false;
        }

        return true;
    }

    private static boolean handleAllowDamage(LivingEntity entity, DamageSource source, float amount) {
        // Immunity Effect Logic
        if (entity.hasEffect(CataclysmMobEffects.IMMUNITY)) {
            SoundMashup.PARAGON_DAMAGE.playAtEntity(entity);
            return false;
        }

        return true;
    }

    private static InteractionResult handleUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        // Disper Effect Logic
        if (player.hasEffect(CataclysmMobEffects.DISPER)) {
            SoundMashup.DISPER.playAtEntity(player);
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    private static DeathProtection handleUseTotem(LivingEntity entity, ItemStack totemStack, DeathProtection deathProtection) {
        if (!(entity instanceof Player user)) return deathProtection;

        // Mortem Effect Logic
        if (user.hasEffect(CataclysmMobEffects.MORTEM)) {
            Cataclysm.getServer().getPlayerList().getPlayers().forEach(player -> {
                CataclysmBroadcast.send(player, CataclysmBroadcast.TOTEM_FAIL, user.getName().getString());
                SoundMashup.MORTEM.playAtEntity(user);
            });
            return null;
        }

        // Mortality Logic
        return MortalityAftereffects.onResurrect(user, totemStack, deathProtection);
    }

    private static void handleCommandRegistration(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext access, Commands.CommandSelection environment) {
        MortalityCommand.INSTANCE.register(dispatcher, access, environment);
    }
}