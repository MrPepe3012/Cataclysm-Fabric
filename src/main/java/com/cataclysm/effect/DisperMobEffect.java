package com.cataclysm.effect;

import com.cataclysm.sound.SoundHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Set;

public class DisperMobEffect extends MobEffect {
    private static final Set<Block> BREAK_BYPASS = Set.of(Blocks.SPAWNER, Blocks.CREAKING_HEART);

    DisperMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean onBreak(Player player, BlockState state) {
        Block block = state.getBlock();
        if (this.isBreakBypassed(block)) {
            return true;
        }
        return this.cancelAction(player);
    }

    public boolean onPlace(Player player) {
        return this.cancelAction(player);
    }

    private boolean cancelAction(Player player) {
        SoundHelper.playAtPlayer(player, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 1.75F);
        return false;
    }

    private boolean isBreakBypassed(Block block) {
        return BREAK_BYPASS.contains(block);
    }

    // Utility methods
    public static Optional<DisperMobEffect> findEffect(Player player) {
        return player.getActiveEffects().stream()
                .filter(effect -> effect.getEffect().value() instanceof DisperMobEffect)
                .findFirst()
                .map(effect -> (DisperMobEffect) effect.getEffect().value());
    }
}