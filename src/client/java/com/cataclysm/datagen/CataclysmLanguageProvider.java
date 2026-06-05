package com.cataclysm.datagen;

import com.cataclysm.effect.CataclysmMobEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.effect.MobEffect;

import java.util.concurrent.CompletableFuture;

public class CataclysmLanguageProvider extends FabricLanguageProvider {

    public CataclysmLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder) {
        this.addEffect(builder, CataclysmMobEffects.DISPER, "Disper");
    }

    // Utility methods
    private void addEffect(TranslationBuilder builder, Holder<MobEffect> holder, String name) {
        builder.add(holder.value(), name);
    }
}