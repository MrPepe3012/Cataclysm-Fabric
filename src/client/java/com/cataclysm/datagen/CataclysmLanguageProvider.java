package com.cataclysm.datagen;

import com.cataclysm.chat.CataclysmBroadcast;
import com.cataclysm.command.MortalityCommand;
import com.cataclysm.registry.CataclysmMobEffects;
import com.cataclysm.registry.CataclysmItems;
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

        // Effects
        this.addEffect(builder, CataclysmMobEffects.DISPER, "Disper");
        this.addEffect(builder, CataclysmMobEffects.MORTEM, "Mortem");
        this.addEffect(builder, CataclysmMobEffects.IMMUNITY, "Immunity");

        // Command
        this.addCommandFeedback(builder, MortalityCommand.ALIAS, "get", "%s's mortality: %s%%");
        this.addCommandFeedback(builder, MortalityCommand.ALIAS, "set", "%s's mortality changed from %s%% to %s%%");

        // Broadcast
        builder.add(CataclysmBroadcast.TOTEM_USE, "<%s> used a totem. (%s%%)");
        builder.add(CataclysmBroadcast.TOTEM_FAIL, "<%s> tried to used a totem with <Mortem>.");

        // Items
        builder.add(CataclysmItems.ARCANE_TOTEM, "Arcane Totem");
        builder.add(CataclysmItems.CALAMITY_TOTEM, "Calamity Totem");
        builder.add(CataclysmItems.PARAGON_TOTEM, "Paragon's Totem");
        builder.add(CataclysmItems.PARAGON_BLESSING, "Paragon's Blessing");
    }

    // Utility methods
    private void addEffect(TranslationBuilder builder, Holder<MobEffect> holder, String translation) {
        builder.add(holder.value(), translation);
    }

    private void addCommandFeedback(TranslationBuilder builder, String alias, String path, String translation) {
        builder.add("commands.cataclysm.%s.%s".formatted(alias, path), translation);
    }
}