package com.cataclysm.registry.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Set;

public class DisperMobEffect extends MobEffect {
    private static final Set<Block> BREAK_BYPASS = Set.of(Blocks.SPAWNER, Blocks.CREAKING_HEART);

    public DisperMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static boolean bypass(Block block) {
        return BREAK_BYPASS.contains(block);
    }
}