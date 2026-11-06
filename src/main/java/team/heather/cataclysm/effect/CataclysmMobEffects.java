package team.heather.cataclysm.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.effect.beneficial.CleansingMobEffect;
import team.heather.cataclysm.effect.beneficial.ImmortalityMobEffect;
import team.heather.cataclysm.effect.harmful.DisperMobEffect;
import team.heather.cataclysm.effect.harmful.MortemMobEffect;
import team.heather.cataclysm.effect.harmful.PaleCorrosionMobEffect;

public interface CataclysmMobEffects {

    Holder<MobEffect> IMMORTALITY = register("immortality", new ImmortalityMobEffect(MobEffectCategory.BENEFICIAL, 0xFEFFD1));
    Holder<MobEffect> CLEANSING = register("cleansing", new CleansingMobEffect(MobEffectCategory.BENEFICIAL, 0xF4F1E6));

    Holder<MobEffect> DISPER = register("disper", new DisperMobEffect(MobEffectCategory.HARMFUL, 0xCE904B));
    Holder<MobEffect> MORTEM = register("mortem", new MortemMobEffect(MobEffectCategory.HARMFUL, 0xC79A3E));
    Holder<MobEffect> PALE_CORROSION = register("pale_corrosion", new PaleCorrosionMobEffect(MobEffectCategory.HARMFUL, 0xD6D3C4));

    static void initialize() {}

    private static Holder<MobEffect> register(String path, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Cataclysm.resourceLocation(path), effect);
    }
}
