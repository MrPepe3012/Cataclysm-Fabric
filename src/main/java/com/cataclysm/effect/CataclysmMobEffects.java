package com.cataclysm.effect;

import com.cataclysm.Cataclysm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public final class CataclysmMobEffects {
    public static final Holder<MobEffect> DISPER = register("disper", new DisperMobEffect(MobEffectCategory.HARMFUL, 16776960));

    private CataclysmMobEffects() {}

    private static Holder<MobEffect> register(String string, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Cataclysm.createResourceLocation(string), mobEffect);
    }
}