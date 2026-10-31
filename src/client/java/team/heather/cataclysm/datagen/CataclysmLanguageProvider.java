package team.heather.cataclysm.datagen;

import team.heather.cataclysm.chat.CataclysmBroadcast;
import team.heather.cataclysm.command.MortalityCommand;
import team.heather.cataclysm.core.item.CataclysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class CataclysmLanguageProvider extends FabricLanguageProvider {
    public CataclysmLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder) {

        // Command
        this.addCommandFeedback(builder, MortalityCommand.ALIAS, "get", "%s's mortality: %s%%");
        this.addCommandFeedback(builder, MortalityCommand.ALIAS, "set", "%s's mortality changed from %s%% to %s%%");

        // Broadcast
        builder.add(CataclysmBroadcast.TOTEM_USE, "<%s> used a totem. (%s%%)");
        builder.add(CataclysmBroadcast.TOTEM_FAIL, "<%s> tried to use a totem with <Mortem>.");

        // Items
        builder.add(CataclysmItems.ARCANE_TOTEM, "Arcane Totem");
        builder.add(CataclysmItems.CALAMITY_TOTEM, "Calamity Totem");
        builder.add(CataclysmItems.PARAGON_TOTEM, "Paragon's Totem");
        builder.add(CataclysmItems.PALE_HELMET, "Pale Helmet");
        builder.add(CataclysmItems.PALE_CHESTPLATE, "Pale Chestplate");
        builder.add(CataclysmItems.PALE_LEGGINGS, "Pale Leggings");
        builder.add(CataclysmItems.PALE_BOOTS, "Pale Boots");
        builder.add(CataclysmItems.PALE_SWORD, "Pale Sword");
        builder.add(CataclysmItems.PALE_BOW, "Pale Bow");
        builder.add(CataclysmItems.CALAMITY_HELMET, "Calamity Helmet");
        builder.add(CataclysmItems.CALAMITY_CHESTPLATE, "Calamity Chestplate");
        builder.add(CataclysmItems.CALAMITY_LEGGINGS, "Calamity Leggings");
        builder.add(CataclysmItems.CALAMITY_BOOTS, "Calamity Boots");
    }

    // Utility methods
    private void addCommandFeedback(TranslationBuilder builder, String alias, String subcommand, String translation) {
        builder.add("commands.cataclysm.%s.%s".formatted(alias, subcommand), translation);
    }
}