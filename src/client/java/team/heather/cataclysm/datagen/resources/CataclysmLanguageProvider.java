package team.heather.cataclysm.datagen.resources;

import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.util.CataclysmBroadcast;
import team.heather.cataclysm.command.MortalityCommand;
import team.heather.cataclysm.component.data.lemegeton.LemegetonUpgrade;
import team.heather.cataclysm.util.TinyCaps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class CataclysmLanguageProvider extends FabricLanguageProvider {

    private TranslationBuilder builder;

    public CataclysmLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder) {

        this.builder = builder;
        this.generateCommands();
        this.generateBroadcasts();
        this.generateItems();
        this.generateBlocks();
        this.generateLemegeton();
    }

    private void generateBlocks() {
        this.builder.add(CataclysmRegistry.BLOCKS.ARCANE_CORE, TinyCaps.format("Arcane Core"));
        this.builder.add(CataclysmRegistry.BLOCKS.GOLEM_HEAD, TinyCaps.format("Golem Head"));
        this.builder.add(CataclysmRegistry.BLOCKS.GOLEM_WALL_HEAD, TinyCaps.format("Golem Head"));
    }

    private void generateLemegeton() {
        this.addUpgrade(LemegetonUpgrade.REGENERATION, "Regeneration", "Heals you little by little.");
        this.addUpgrade(LemegetonUpgrade.JUMP_BOOST, "Jump Boost", "Lets you jump higher.");
        this.addUpgrade(LemegetonUpgrade.SPEED, "Speed", "Makes you run faster.");
        this.addUpgrade(LemegetonUpgrade.STRENGTH, "Strength", "Makes your hits stronger.");
        this.addUpgrade(LemegetonUpgrade.DOLPHIN_GRACE, "Dolphin's Grace", "Swim fast and breathe underwater.");
        this.addUpgrade(LemegetonUpgrade.HEALTH_BLESSING, "Health Blessing", "Heals you and adds extra hearts.");
        this.addUpgrade(LemegetonUpgrade.RESISTANCE, "Resistance", "You take less damage.");
        this.addUpgrade(LemegetonUpgrade.HERO_OF_THE_VILLAGE, "Hero of the Village", "Villagers give you better prices.");
        this.addUpgrade(LemegetonUpgrade.HASTE, "Haste", "Mine and attack faster.");
        this.addUpgrade(LemegetonUpgrade.INVISIBILITY, "Invisibility", "Makes your body invisible.");
        this.addUpgrade(LemegetonUpgrade.FIRE_RESISTANCE, "Fire Resistance", "Fire and lava can't hurt you.");
        this.addUpgrade(LemegetonUpgrade.CONDUIT_POWER, "Conduit Power", "Breathe and mine fast underwater.");
        this.addUpgrade(LemegetonUpgrade.SLOW_FALLING, "Slow Falling", "Fall slowly, with no fall damage.");
        this.addUpgrade(LemegetonUpgrade.CLEANSING, "Cleansing", "Removes bad effects and blocks new ones.");

        this.builder.add("menu.cataclysm.lemegeton.title", "Lemegeton");
        this.builder.add("menu.cataclysm.lemegeton.unlock", "Unlock");
        this.builder.add("menu.cataclysm.lemegeton.upgrade", "Upgrade");
        this.builder.add("menu.cataclysm.lemegeton.complete", "Complete");
        this.builder.add("menu.cataclysm.lemegeton.ready", "Costs 1 Cataclysm Upgrade.");
        this.builder.add("menu.cataclysm.lemegeton.payment", "You need a Tier %s Cataclysm Upgrade.");
        this.builder.add("message.cataclysm.lemegeton.limit", "No more upgrades this week.");
        this.builder.add("message.cataclysm.lemegeton.maxed", "This upgrade is at its max level.");
        this.builder.add("message.cataclysm.lemegeton.unlocked", "%s unlocked %s.");
        this.builder.add("message.cataclysm.lemegeton.upgraded", "%s upgraded %s to level %s.");
    }

    private void addUpgrade(LemegetonUpgrade upgrade,
                            String name,
                            String description) {
        this.builder.add(upgrade.translationKey(), TinyCaps.format(name));
        this.builder.add(upgrade.descriptionKey(), description);
    }

    private void generateCommands() {
        this.addMortalityFeedback("get", "%s's mortality: %s%%");
        this.addMortalityFeedback("set", "%s's mortality changed from %s%% to %s%%");
    }

    private void generateBroadcasts() {
        this.builder.add(CataclysmBroadcast.TOTEM_USE, "<%s> used a totem. (%s%%)");
        this.builder.add(CataclysmBroadcast.TOTEM_FAIL, "<%s> tried to use a totem with <Mortem>.");
    }

    private void generateItems() {
        this.addItem(CataclysmRegistry.ITEMS.PARAGON_BLESSING, "Paragon's Blessing");

        this.addItem(CataclysmRegistry.ITEMS.LEMEGETON, "Lemegeton");

        this.addItem(CataclysmRegistry.ITEMS.PARAGON_QUARTZ, "Paragon's Quartz");
        this.addItem(CataclysmRegistry.ITEMS.TECTONITE, "Tectonite");
        this.addItem(CataclysmRegistry.ITEMS.ONYX, "Onyx");
        this.addItem(CataclysmRegistry.ITEMS.VOIDSTONE, "Void Stone");
        this.addItem(CataclysmRegistry.ITEMS.CATACLYSM_UPGRADE, "Cataclysm Upgrade");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_SWORD, "Twisted Sword");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_PICKAXE, "Twisted Pickaxe");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_AXE, "Twisted Axe");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_SHOVEL, "Twisted Shovel");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_HOE, "Twisted Hoe");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_FLESH, "Twisted Flesh");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_BONE, "Twisted Bone");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_POWDER, "Twisted Powder");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_STRING, "Twisted String");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_PEARL, "Twisted Pearl");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_ROD, "Twisted Rod");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_INGOT, "Twisted Ingot");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_MACE, "Arcane Mace");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_BOW, "Arcane Bow");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_SHIELD, "Arcane Shield");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_TRIDENT, "Arcane Trident");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_TOTEM, "Arcane Totem");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_ROD, "Arcane Rod");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_CORE, "Arcane Core");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_NUGGET, "Arcane Nugget");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_INGOT, "Arcane Ingot");
        this.addItem(CataclysmRegistry.ITEMS.ARCANE_SCUTE, "Arcane Scute");
        this.addItem(CataclysmRegistry.ITEMS.VOIDHEART, "Void Heart");
        this.addItem(CataclysmRegistry.ITEMS.TWISTED_RELIC, "Twisted Relic");
        this.addItem(CataclysmRegistry.ITEMS.UR_TEAR, "Ur-Tear");
        this.addItem(CataclysmRegistry.ITEMS.TOXIC_MEMBRANE, "Toxic Membrane");
        this.addItem(CataclysmRegistry.ITEMS.GOLDEN_CREAM, "Golden Cream");
        this.addItem(CataclysmRegistry.ITEMS.NIGHTMARE_BONE, "Nightmare Bone");
        this.addItem(CataclysmRegistry.ITEMS.CATACLYST_BONE, "Cataclyst Bone");
        this.addItem(CataclysmRegistry.ITEMS.GUARDIAN_HEART, "Guardian Heart");
        this.addItem(CataclysmRegistry.ITEMS.PALE_BALL, "Pale Ball");
        this.addItem(CataclysmRegistry.ITEMS.LLAMA_FUR, "Llama Fur");
        this.addItem(CataclysmRegistry.ITEMS.MIDWAY_RELIC, "Midway Relic");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_KEY, "Calamity Key");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_NUGGET, "Calamity Nugget");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_INGOT, "Calamity Ingot");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_HELMET, "Calamity Helmet");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_CHESTPLATE, "Calamity Chestplate");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_LEGGINGS, "Calamity Leggings");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_BOOTS, "Calamity Boots");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_TOTEM, "Calamity Totem");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_APPLE, "Calamity Apple");
        this.addItem(CataclysmRegistry.ITEMS.ENCHANTED_CALAMITY_APPLE, "Enchanted Calamity Apple");
        this.addItem(CataclysmRegistry.ITEMS.CALAMITY_CARROT, "Calamity Carrot");
        this.addItem(CataclysmRegistry.ITEMS.ELDERS_EYE, "Elder's Eye");
        this.addItem(CataclysmRegistry.ITEMS.GOLEM_HEAD, "Golem Head");
        this.addItem(CataclysmRegistry.ITEMS.DROWNED_CROWN, "Drowned Crown");
        this.addItem(CataclysmRegistry.ITEMS.WANDERING_HEART, "Wandering Heart");
        this.addItem(CataclysmRegistry.ITEMS.WANDERING_SOUL, "Wandering Soul");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_SCUTE, "Mirage Scute");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_HELMET, "Mirage Helmet");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_PEARL, "Mirage Pearl");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_FLESH, "Mirage Flesh");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_POWDER, "Mirage Powder");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_BONE, "Mirage Bone");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_TEAR, "Mirage Tear");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_INGOT, "Mirage Ingot");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_EYEBALL, "Mirage Eyeball");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_QUARTZ, "Mirage Quartz");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_ESSENCE, "Mirage Essence");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_AMETHYST, "Mirage Amethyst");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_WHALE_WING, "Whale Wing");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_BLESSING, "Mirage's Blessing");
        this.addItem(CataclysmRegistry.ITEMS.PARAGON_PEARL, "Paragon Pearl");
        this.addItem(CataclysmRegistry.ITEMS.ENDER_BAG, "Ender Bag");
        this.addItem(CataclysmRegistry.ITEMS.OBSCURE_ONYX, "Obscure Onyx");
        this.addItem(CataclysmRegistry.ITEMS.MACE_UPGRADE, "Mace Upgrade");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_UPGRADE, "Mirage Upgrade");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_ELYTRA, "Mirage Elytra");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_MACE, "Mirage Mace");
        this.addItem(CataclysmRegistry.ITEMS.MIRAGE_APPLE, "Mirage Apple");
        this.addItem(CataclysmRegistry.ITEMS.PARAGON_TOTEM, "Paragon Totem");
        this.addItem(CataclysmRegistry.ITEMS.PALE_SWORD, "Pale Sword");
        this.addItem(CataclysmRegistry.ITEMS.PALE_BOW, "Pale Bow");

        this.addItem(CataclysmRegistry.ITEMS.PALE_HELMET, "Pale Helmet");
        this.addItem(CataclysmRegistry.ITEMS.PALE_CHESTPLATE, "Pale Chestplate");
        this.addItem(CataclysmRegistry.ITEMS.PALE_LEGGINGS, "Pale Leggings");
        this.addItem(CataclysmRegistry.ITEMS.PALE_BOOTS, "Pale Boots");

        this.addItem(CataclysmRegistry.ITEMS.CHALICE_OF_THE_OVERWORLD, "Overworld's Chalice");
        this.addItem(CataclysmRegistry.ITEMS.CHALICE_OF_THE_NETHER, "Nether's Chalice");
        this.addItem(CataclysmRegistry.ITEMS.CHALICE_OF_THE_END, "End's Chalice");
    }

    private void addItem(Item item, String name) {
        this.builder.add(item, TinyCaps.format(name));
    }

    private void addMortalityFeedback(String subcommand,
                                      String translation) {
        this.builder.add("commands.cataclysm.%s.%s".formatted(MortalityCommand.ALIAS, subcommand), translation);
    }
}
