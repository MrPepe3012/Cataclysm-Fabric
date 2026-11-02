package team.heather.cataclysm.datagen;

import team.heather.cataclysm.util.CataclysmBroadcast;
import team.heather.cataclysm.command.MortalityCommand;
import team.heather.cataclysm.registry.item.CataclysmItems;
import team.heather.cataclysm.registry.block.CataclysmBlocks;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonUpgrade;
import team.heather.cataclysm.util.TinyCaps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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
        this.builder.add(CataclysmBlocks.ARCANE_CORE, TinyCaps.format("Arcane Core"));
        this.builder.add(CataclysmBlocks.GOLEM_HEAD, TinyCaps.format("Golem Head"));
        this.builder.add(CataclysmBlocks.GOLEM_WALL_HEAD, TinyCaps.format("Golem Head"));
    }

    private void generateLemegeton() {
        this.builder.add(LemegetonUpgrade.REGENERATION.translationKey(), TinyCaps.format("Regeneration"));
        this.builder.add(LemegetonUpgrade.JUMP_BOOST.translationKey(), TinyCaps.format("Jump Boost"));
        this.builder.add(LemegetonUpgrade.SPEED.translationKey(), TinyCaps.format("Speed"));
        this.builder.add(LemegetonUpgrade.STRENGTH.translationKey(), TinyCaps.format("Strength"));
        this.builder.add(LemegetonUpgrade.DOLPHIN_GRACE.translationKey(), TinyCaps.format("Dolphin's Grace"));
        this.builder.add(LemegetonUpgrade.HEALTH_BLESSING.translationKey(), TinyCaps.format("Health Blessing"));
        this.builder.add(LemegetonUpgrade.RESISTANCE.translationKey(), TinyCaps.format("Resistance"));
        this.builder.add(LemegetonUpgrade.HERO_OF_THE_VILLAGE.translationKey(), TinyCaps.format("Hero of the Village"));
        this.builder.add(LemegetonUpgrade.HASTE.translationKey(), TinyCaps.format("Haste"));
        this.builder.add(LemegetonUpgrade.INVISIBILITY.translationKey(), TinyCaps.format("Invisibility"));
        this.builder.add(LemegetonUpgrade.FIRE_RESISTANCE.translationKey(), TinyCaps.format("Fire Resistance"));
        this.builder.add(LemegetonUpgrade.CONDUIT_POWER.translationKey(), TinyCaps.format("Conduit Power"));
        this.builder.add(LemegetonUpgrade.SLOW_FALLING.translationKey(), TinyCaps.format("Slow Falling"));
        this.builder.add(LemegetonUpgrade.CLEANSING.translationKey(), TinyCaps.format("Cleansing"));

        this.builder.add("menu.cataclysm.lemegeton.title", "Lemegeton Upgrades: %s/%s");
        this.builder.add("menu.cataclysm.lemegeton.required_tier", "Requires a tier %s Cataclysm Upgrade.");
        this.builder.add("menu.cataclysm.lemegeton.offhand", "Hold it in your off-hand to buy a level.");
        this.builder.add("message.cataclysm.lemegeton.cooldown", "Lemegeton is ready in %s seconds.");
        this.builder.add("message.cataclysm.lemegeton.limit", "This Lemegeton has reached its upgrade limit for this week.");
        this.builder.add("message.cataclysm.lemegeton.maxed", "This upgrade is already at its maximum level.");
        this.builder.add("message.cataclysm.lemegeton.unlocked", "%s unlocked %s.");
        this.builder.add("message.cataclysm.lemegeton.upgraded", "%s upgraded %s to level %s.");
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
        this.addItem(CataclysmItems.PARAGON_BLESSING, "Paragon's Blessing", "The Paragon's testament");
        this.addItem(CataclysmItems.PARAGON_QUARTZ, "Paragon's Quartz");
        this.addItem(CataclysmItems.LEMEGETON, "Lemegeton", "Deus sacer cataclysmatum");
        this.addItem(CataclysmItems.TECTONITE, "Tectonite");
        this.addItem(CataclysmItems.ONYX, "Onyx");
        this.addItem(CataclysmItems.VOIDSTONE, "Void Stone");
        this.addItem(CataclysmItems.CATACLYSM_UPGRADE, "Cataclysm Upgrade", "Unlock an upgrade by opening the Lemegeton menu while holding this in your off-hand.");
        this.addTwistedTool(CataclysmItems.TWISTED_SWORD, "Twisted Sword");
        this.addTwistedTool(CataclysmItems.TWISTED_PICKAXE, "Twisted Pickaxe");
        this.addTwistedTool(CataclysmItems.TWISTED_AXE, "Twisted Axe");
        this.addTwistedTool(CataclysmItems.TWISTED_SHOVEL, "Twisted Shovel");
        this.addTwistedTool(CataclysmItems.TWISTED_HOE, "Twisted Hoe");
        this.addItem(CataclysmItems.TWISTED_FLESH, "Twisted Flesh");
        this.addItem(CataclysmItems.TWISTED_BONE, "Twisted Bone");
        this.addItem(CataclysmItems.TWISTED_POWDER, "Twisted Powder");
        this.addItem(CataclysmItems.TWISTED_STRING, "Twisted String");
        this.addItem(CataclysmItems.TWISTED_PEARL, "Twisted Pearl");
        this.addItem(CataclysmItems.TWISTED_ROD, "Twisted Rod");
        this.addItem(CataclysmItems.TWISTED_INGOT, "Twisted Ingot");
        this.addArcaneEquipment(CataclysmItems.ARCANE_MACE, "Arcane Mace");
        this.addArcaneEquipment(CataclysmItems.ARCANE_BOW, "Arcane Bow");
        this.addArcaneEquipment(CataclysmItems.ARCANE_SHIELD, "Arcane Shield");
        this.addArcaneEquipment(CataclysmItems.ARCANE_TRIDENT, "Arcane Trident");
        this.addArcaneEquipment(CataclysmItems.ARCANE_TOTEM, "Arcane Totem");
        this.addItem(CataclysmItems.ARCANE_ROD, "Arcane Rod");
        this.addItem(CataclysmItems.ARCANE_CORE, "Arcane Core");
        this.addItem(CataclysmItems.ARCANE_NUGGET, "Arcane Nugget");
        this.addItem(CataclysmItems.ARCANE_INGOT, "Arcane Ingot");
        this.addItem(CataclysmItems.ARCANE_SCUTE, "Arcane Scute");
        this.addItem(CataclysmItems.VOIDHEART, "Void Heart");
        this.addItem(CataclysmItems.TWISTED_RELIC, "Twisted Relic", "Treasure from the depths of the abyss.");
        this.addItem(CataclysmItems.UR_TEAR, "Ur-Tear", "A remnant of an Ur-Ghast's lament.");
        this.addItem(CataclysmItems.TOXIC_MEMBRANE, "Toxic Membrane", "A remnant of something beautiful.");
        this.addItem(CataclysmItems.GOLDEN_CREAM, "Golden Cream", "A remnant of something loathsome.");
        this.addItem(CataclysmItems.NIGHTMARE_BONE, "Nightmare Bone", "A remnant of something terrible.");
        this.addItem(CataclysmItems.CATACLYST_BONE, "Cataclyst Bone", "A remnant of a cataclysm.");
        this.addItem(CataclysmItems.GUARDIAN_HEART, "Guardian Heart", "A remnant of a temple.");
        this.addItem(CataclysmItems.PALE_BALL, "Pale Ball", "A remnant of a forest.");
        this.addItem(CataclysmItems.LLAMA_FUR, "Llama Fur", "A remnant of distant times.");
        this.addItem(CataclysmItems.MIDWAY_RELIC, "Midway Relic", "An ancient relic forged in times of adversity");
        this.addItem(CataclysmItems.CALAMITY_KEY, "Calamity Key", "From the depths of the Calamity Chambers.");
        this.addItem(CataclysmItems.CALAMITY_NUGGET, "Calamity Nugget");
        this.addItem(CataclysmItems.CALAMITY_INGOT, "Calamity Ingot");
        this.addItem(CataclysmItems.CALAMITY_HELMET, "Calamity Helmet", "A helmet hardened by the fury of a Ragnarök");
        this.addItem(CataclysmItems.CALAMITY_CHESTPLATE, "Calamity Chestplate", "A chestplate hardened by the fury of a Ragnarök");
        this.addItem(CataclysmItems.CALAMITY_LEGGINGS, "Calamity Leggings", "Leggings hardened by the fury of a Ragnarök");
        this.addItem(CataclysmItems.CALAMITY_BOOTS, "Calamity Boots", "Boots hardened by the fury of a Ragnarök");
        this.addItem(CataclysmItems.CALAMITY_TOTEM, "Calamity Totem", "Forged from the terror of mortals");
        this.addItem(CataclysmItems.CALAMITY_APPLE, "Calamity Apple", "Infused with the strength of a thousand storms");
        this.addItem(CataclysmItems.ENCHANTED_CALAMITY_APPLE, "Enchanted Calamity Apple", "Infused with the strength of a thousand storms");
        this.addItem(CataclysmItems.CALAMITY_CARROT, "Calamity Carrot", "Empowered by the speed of a cyclone");
        this.addItem(CataclysmItems.ELDERS_EYE, "Elder's Eye", "A remnant of an elder guardian.");
        this.addItem(CataclysmItems.GOLEM_HEAD, "Golem Head", "A remnant of a legend.");
        this.addItem(CataclysmItems.DROWNED_CROWN, "Drowned Crown", "A remnant of a sunken kingdom.");
        this.addItem(CataclysmItems.WANDERING_HEART, "Wandering Heart", "A remnant of an eternal traveler.");
        this.addItem(CataclysmItems.WANDERING_SOUL, "Wandering Soul", "A remnant of a wandering spirit.");
        this.addItem(CataclysmItems.MIRAGE_SCUTE, "Mirage Scute");
        this.addItem(CataclysmItems.MIRAGE_HELMET, "Mirage Helmet", "Forged with the illusion of a mirage");
        this.addItem(CataclysmItems.MIRAGE_PEARL, "Mirage Pearl");
        this.addItem(CataclysmItems.MIRAGE_FLESH, "Mirage Flesh");
        this.addItem(CataclysmItems.MIRAGE_POWDER, "Mirage Powder");
        this.addItem(CataclysmItems.MIRAGE_BONE, "Mirage Bone");
        this.addItem(CataclysmItems.MIRAGE_TEAR, "Mirage Tear");
        this.addItem(CataclysmItems.MIRAGE_INGOT, "Mirage Ingot");
        this.addItem(CataclysmItems.MIRAGE_EYEBALL, "Mirage Eyeball");
        this.addItem(CataclysmItems.MIRAGE_QUARTZ, "Mirage Quartz");
        this.addItem(CataclysmItems.MIRAGE_ESSENCE, "Mirage Essence");
        this.addItem(CataclysmItems.MIRAGE_AMETHYST, "Mirage Amethyst");
        this.addItem(CataclysmItems.MIRAGE_WHALE_WING, "Whale Wing");
        this.addItem(CataclysmItems.MIRAGE_BLESSING, "Mirage's Blessing", "An echo of eternity");
        this.addItem(CataclysmItems.PARAGON_PEARL, "Paragon Pearl", "A fragment of the Paragon's grace");
        this.addItem(CataclysmItems.ENDER_BAG, "Ender Bag");
        this.addItem(CataclysmItems.OBSCURE_ONYX, "Obscure Onyx", "Forged with the illusion of the abyss");
        this.addItem(CataclysmItems.MACE_UPGRADE, "Mace Upgrade");
        this.addItem(CataclysmItems.MIRAGE_UPGRADE, "Mirage Upgrade");
        this.addItem(CataclysmItems.BROKEN_ELYTRA, "Broken Elytra");
        this.addItem(CataclysmItems.MIRAGE_ELYTRA, "Mirage Elytra", "Woven with the illusion of a mirage.");
        this.addItem(CataclysmItems.MIRAGE_MACE, "Mirage Mace", "Forged with the illusion of a mirage");
        this.addItem(CataclysmItems.MIRAGE_APPLE, "Mirage Apple", "Fruit of an illusion");
        this.addItem(CataclysmItems.PARAGON_TOTEM, "Paragon Totem", "Superstes Numisma");
        this.addItem(CataclysmItems.PALE_SWORD, "Pale Sword", "Ex umbra silvae confectum");
        this.addItem(CataclysmItems.PALE_BOW, "Pale Bow", "Ex umbris silvae confectum");
        this.addPaleArmor(CataclysmItems.PALE_HELMET, "Pale Helmet");
        this.addPaleArmor(CataclysmItems.PALE_CHESTPLATE, "Pale Chestplate");
        this.addPaleArmor(CataclysmItems.PALE_LEGGINGS, "Pale Leggings");
        this.addPaleArmor(CataclysmItems.PALE_BOOTS, "Pale Boots");
        this.addItem(CataclysmItems.CHALICE_OF_THE_OVERWORLD, "Overworld's Chalice");
        this.addItem(CataclysmItems.CHALICE_OF_THE_NETHER, "Nether's Chalice");
        this.addItem(CataclysmItems.CHALICE_OF_THE_END, "End's Chalice");
    }

    /** An empty description translation produces no lore lines in the tooltip. */
    private void addItem(Item item,
                         String name) {
        this.addItem(item, name, "");
    }

    /** Generates both keys even when an item has no description. */
    private void addItem(Item item,
                         String name,
                         String description) {
        this.builder.add(item, TinyCaps.format(name));
        this.builder.add(BuiltInRegistries.ITEM.getKey(item).toLanguageKey("item") + ".description", description);
    }

    private void addTwistedTool(Item item,
                                String name) {
        this.addItem(item, name, "Forged from remnants of the abyss");
    }

    private void addArcaneEquipment(Item item,
                                    String name) {
        this.addItem(item, name, "Resonates with souls from arcane times");
    }

    private void addPaleArmor(Item item,
                              String name) {
        this.addItem(item, name, "Ex essentia silvae nullius confectum");
    }

    private void addMortalityFeedback(String subcommand,
                                      String translation) {
        this.builder.add("commands.cataclysm.%s.%s".formatted(MortalityCommand.ALIAS, subcommand), translation);
    }
}
