package com.cataclysm.registry;

import com.cataclysm.Cataclysm;
import com.cataclysm.registry.effect.CataclysmMobEffect;
import com.cataclysm.registry.effect.DisperMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public final class CataclysmMobEffects {
    public static final Holder<MobEffect> DISPER = register("disper", new DisperMobEffect(MobEffectCategory.HARMFUL, 0xFFFF99));
    public static final Holder<MobEffect> MORTEM = register("mortem", new CataclysmMobEffect(MobEffectCategory.HARMFUL, 0xFFD700));
    public static final Holder<MobEffect> IMMUNITY = register("immunity", new CataclysmMobEffect(MobEffectCategory.BENEFICIAL, 0xB8860B));

    private CataclysmMobEffects() {}

    private static Holder<MobEffect> register(String string, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Cataclysm.resourceLocation(string), effect);
    }
}