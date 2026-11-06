package team.heather.cataclysm.datagen.resources;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.item.data.LemegetonComponent;
import team.heather.cataclysm.util.CataclysmBroadcast;
import team.heather.cataclysm.util.TinyCaps;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CataclysmLanguageProvider extends FabricLanguageProvider {

    private static final Set<String> LOWERCASE_WORDS = Set.of("of", "the");
    /** Words an identifier cannot spell, as it only holds lowercase letters and underscores. */
    private static final Map<String, String> SPELLED_WORDS = Map.of("elders", "Elder's");

    private TranslationBuilder builder;

    public CataclysmLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder) {

        this.builder = builder;
        this.generateBroadcasts();
        this.addNames(BuiltInRegistries.ITEM, this.builder::add);
        this.addNames(BuiltInRegistries.BLOCK, this.builder::add);
        this.generateLemegeton();
        this.generateMobEffects();
    }

    private void generateMobEffects() {
        this.builder.add(CataclysmMobEffects.IMMORTALITY.value(), "Immortality");
        this.builder.add(CataclysmMobEffects.CLEANSING.value(), "Cleansing");
        this.builder.add(CataclysmMobEffects.DISPER.value(), "Disper");
        this.builder.add(CataclysmMobEffects.MORTEM.value(), "Mortem");
        this.builder.add(CataclysmMobEffects.PALE_CORROSION.value(), "Pale Corrosion");
    }

    private void generateLemegeton() {
        this.addUpgrade(LemegetonComponent.Upgrade.REGENERATION, "Regeneration", "Heals you little by little.");
        this.addUpgrade(LemegetonComponent.Upgrade.JUMP_BOOST, "Jump Boost", "Lets you jump higher.");
        this.addUpgrade(LemegetonComponent.Upgrade.SPEED, "Speed", "Makes you run faster.");
        this.addUpgrade(LemegetonComponent.Upgrade.STRENGTH, "Strength", "Makes your hits stronger.");
        this.addUpgrade(LemegetonComponent.Upgrade.DOLPHIN_GRACE, "Dolphin's Grace", "Swim fast and breathe underwater.");
        this.addUpgrade(LemegetonComponent.Upgrade.HEALTH_BLESSING, "Health Blessing", "Heals you and adds extra hearts.");
        this.addUpgrade(LemegetonComponent.Upgrade.RESISTANCE, "Resistance", "You take less damage.");
        this.addUpgrade(LemegetonComponent.Upgrade.HERO_OF_THE_VILLAGE, "Hero of the Village", "Villagers give you better prices.");
        this.addUpgrade(LemegetonComponent.Upgrade.HASTE, "Haste", "Mine and attack faster.");
        this.addUpgrade(LemegetonComponent.Upgrade.INVISIBILITY, "Invisibility", "Makes your body invisible.");
        this.addUpgrade(LemegetonComponent.Upgrade.FIRE_RESISTANCE, "Fire Resistance", "Fire and lava can't hurt you.");
        this.addUpgrade(LemegetonComponent.Upgrade.CONDUIT_POWER, "Conduit Power", "Breathe and mine fast underwater.");
        this.addUpgrade(LemegetonComponent.Upgrade.SLOW_FALLING, "Slow Falling", "Fall slowly, with no fall damage.");
        this.addUpgrade(LemegetonComponent.Upgrade.CLEANSING, "Cleansing", "Removes bad effects and blocks new ones.");

        this.builder.add("menu.cataclysm.lemegeton.title", "Lemegeton");
        this.builder.add("menu.cataclysm.lemegeton.unlock", "Unlock");
        this.builder.add("menu.cataclysm.lemegeton.upgrade", "Upgrade");
        this.builder.add("menu.cataclysm.lemegeton.complete", "Complete");
        this.builder.add("menu.cataclysm.lemegeton.ready", "Costs 1 Cataclysm Upgrade.");
        this.builder.add("menu.cataclysm.lemegeton.payment", "You need a Tier %s Cataclysm Upgrade.");
        this.builder.add("message.cataclysm.lemegeton.limit", "No more upgrades this phase.");
        this.builder.add("message.cataclysm.lemegeton.maxed", "This upgrade is at its max level.");
        this.builder.add("message.cataclysm.lemegeton.unlocked", "%s unlocked %s.");
        this.builder.add("message.cataclysm.lemegeton.upgraded", "%s upgraded %s to level %s.");
    }

    private void addUpgrade(LemegetonComponent.Upgrade upgrade,
                            String name,
                            String description) {
        this.builder.add(upgrade.translationKey(), TinyCaps.format(name));
        this.builder.add(upgrade.descriptionKey(), description);
    }

    private void generateBroadcasts() {
        this.builder.add(CataclysmBroadcast.TOTEM_USE, "<%s> used a totem. (%s%%)");
        this.builder.add(CataclysmBroadcast.TOTEM_FAIL, "<%s> tried to use a totem with <Mortem>.");
    }

    private <T> void addNames(Registry<T> registry, BiConsumer<T, String> add) {
        registry.stream()
            .filter(entry -> Cataclysm.MOD_ID.equals(registry.getKey(entry).getNamespace()))
            .forEach(entry -> add.accept(entry, TinyCaps.format(displayName(registry.getKey(entry).getPath()))));
    }

    /** Turns an identifier path into a title: {@code chalice_of_the_end} becomes {@code Chalice of the End}. */
    private static String displayName(String path) {
        var words = path.split("_");
        for (int index = 0; index < words.length; index++) {
            words[index] = displayWord(words[index], index == 0);
        }
        return String.join(" ", words);
    }

    private static String displayWord(String word, boolean first) {
        if (!first && LOWERCASE_WORDS.contains(word)) {
            return word;
        }
        return SPELLED_WORDS.getOrDefault(word, Character.toUpperCase(word.charAt(0)) + word.substring(1));
    }
}
