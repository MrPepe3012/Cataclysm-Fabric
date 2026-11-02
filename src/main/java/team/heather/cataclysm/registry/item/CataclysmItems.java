package team.heather.cataclysm.registry.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.builder.ItemBase;
import team.heather.cataclysm.builder.ItemFactory;
import team.heather.cataclysm.builder.ItemFamily;
import team.heather.cataclysm.builder.Palette;
import team.heather.cataclysm.registry.item.lemegeton.CataclysmUpgradeItem;
import team.heather.cataclysm.material.CataclysmArmorMaterial;

import java.util.List;
import java.util.ArrayList;

public interface CataclysmItems extends CataclysmRegistry {

    Item PARAGON_BLESSING = ItemFactory.register("paragon_blessing", item -> item.family(ItemFamily.PARAGON).base(ItemBase.PARAGON_BLESSING));
    Item PARAGON_PEARL = ItemFactory.register("paragon_pearl", item -> item.family(ItemFamily.PARAGON).base(ItemBase.PARAGON_PEARL));
    Item PARAGON_TOTEM = ItemFactory.register("paragon_totem", item -> item.family(ItemFamily.PARAGON).base(ItemBase.PARAGON_TOTEM));
    Item PARAGON_QUARTZ = ItemFactory.register("paragon_quartz");

    Item LEMEGETON = ItemFactory.register("lemegeton", item -> item.family(ItemFamily.LEMEGETON).base(ItemBase.LEMEGETON));
    Item CATACLYSM_UPGRADE = ItemFactory.register("cataclysm_upgrade", CataclysmUpgradeItem::create);
    Item VOIDSTONE = ItemFactory.register("voidstone");
    Item TECTONITE = ItemFactory.register("tectonite");
    Item ONYX = ItemFactory.register("onyx");

    Item TWISTED_RELIC = ItemFactory.register("twisted_relic", item -> item.family(ItemFamily.TWISTED).glint());
    Item TWISTED_SWORD = ItemFactory.register("twisted_sword", item -> item.family(ItemFamily.TWISTED).base(ItemBase.SWORD.damage(12).speed(-2.4)));
    Item TWISTED_PICKAXE = ItemFactory.register("twisted_pickaxe", item -> item.family(ItemFamily.TWISTED).base(ItemBase.TWISTED_PICKAXE.damage(8).speed(-3.0)));
    Item TWISTED_AXE = ItemFactory.register("twisted_axe", item -> item.family(ItemFamily.TWISTED).base(ItemBase.AXE.damage(16).speed(-3.0)));
    Item TWISTED_SHOVEL = ItemFactory.register("twisted_shovel", item -> item.family(ItemFamily.TWISTED).base(ItemBase.SHOVEL.damage(7.5)));
    Item TWISTED_HOE = ItemFactory.register("twisted_hoe", item -> item.family(ItemFamily.TWISTED).base(ItemBase.HOE.damage(2)));
    Item TWISTED_FLESH = ItemFactory.register("twisted_flesh");
    Item TWISTED_BONE = ItemFactory.register("twisted_bone");
    Item TWISTED_POWDER = ItemFactory.register("twisted_powder");
    Item TWISTED_STRING = ItemFactory.register("twisted_string");
    Item TWISTED_PEARL = ItemFactory.register("twisted_pearl");
    Item TWISTED_ROD = ItemFactory.register("twisted_rod");
    Item TWISTED_INGOT = ItemFactory.register("twisted_ingot");
    Item VOIDHEART = ItemFactory.register("voidheart");

    Item ARCANE_MACE = ItemFactory.register("arcane_mace", item -> item.family(ItemFamily.ARCANE).base(ItemBase.ARCANE_MACE.damage(8).speed(0.6)));
    Item ARCANE_BOW = ItemFactory.register("arcane_bow", item -> item.family(ItemFamily.ARCANE).base(ItemBase.BOW));
    Item ARCANE_SHIELD = ItemFactory.register("arcane_shield", item -> item.family(ItemFamily.ARCANE).base(ItemBase.SHIELD));
    Item ARCANE_TRIDENT = ItemFactory.register("arcane_trident", item -> item.family(ItemFamily.ARCANE).base(ItemBase.TRIDENT.damage(12).speed(1.1)));
    Item ARCANE_TOTEM = ItemFactory.register("arcane_totem", item -> item.family(ItemFamily.ARCANE).base(ItemBase.ARCANE_TOTEM));
    Item ARCANE_ROD = ItemFactory.register("arcane_rod", item -> item.family(ItemFamily.ARCANE));
    Item ARCANE_CORE = ItemFactory.register("arcane_core", item -> item.family(ItemFamily.ARCANE).base(ItemBase.ARCANE_CORE));
    Item ARCANE_NUGGET = ItemFactory.register("arcane_nugget", item -> item.family(ItemFamily.ARCANE));
    Item ARCANE_INGOT = ItemFactory.register("arcane_ingot", item -> item.family(ItemFamily.ARCANE));
    Item ARCANE_SCUTE = ItemFactory.register("arcane_scute", item -> item.family(ItemFamily.ARCANE));

    Item CALAMITY_HELMET = ItemFactory.register("calamity_helmet", item -> item.family(ItemFamily.CALAMITY).armor(CataclysmArmorMaterial.CALAMITY, ArmorType.HELMET));
    Item CALAMITY_CHESTPLATE = ItemFactory.register("calamity_chestplate", item -> item.family(ItemFamily.CALAMITY).armor(CataclysmArmorMaterial.CALAMITY, ArmorType.CHESTPLATE));
    Item CALAMITY_LEGGINGS = ItemFactory.register("calamity_leggings", item -> item.family(ItemFamily.CALAMITY).armor(CataclysmArmorMaterial.CALAMITY, ArmorType.LEGGINGS));
    Item CALAMITY_BOOTS = ItemFactory.register("calamity_boots", item -> item.family(ItemFamily.CALAMITY).armor(CataclysmArmorMaterial.CALAMITY, ArmorType.BOOTS));
    Item CALAMITY_TOTEM = ItemFactory.register("calamity_totem", item -> item.family(ItemFamily.CALAMITY).base(ItemBase.CALAMITY_TOTEM));
    Item CALAMITY_APPLE = ItemFactory.register("calamity_apple", item -> item.family(ItemFamily.CALAMITY).base(ItemBase.CALAMITY_APPLE));
    Item ENCHANTED_CALAMITY_APPLE = ItemFactory.register("enchanted_calamity_apple", item -> item.family(ItemFamily.CALAMITY).base(ItemBase.ENCHANTED_CALAMITY_APPLE));
    Item CALAMITY_CARROT = ItemFactory.register("calamity_carrot", item -> item.family(ItemFamily.CALAMITY).base(ItemBase.CALAMITY_CARROT));
    Item CALAMITY_KEY = ItemFactory.register("calamity_key", item -> item.family(ItemFamily.CALAMITY));
    Item CALAMITY_NUGGET = ItemFactory.register("calamity_nugget", item -> item.family(ItemFamily.CALAMITY));
    Item CALAMITY_INGOT = ItemFactory.register("calamity_ingot", item -> item.family(ItemFamily.CALAMITY));

    Item MIDWAY_RELIC = ItemFactory.register("midway_relic", item -> item.family(ItemFamily.MIDWAY).glint());
    Item UR_TEAR = ItemFactory.register("ur_tear");
    Item TOXIC_MEMBRANE = ItemFactory.register("toxic_membrane");
    Item NIGHTMARE_BONE = ItemFactory.register("nightmare_bone");
    Item CATACLYST_BONE = ItemFactory.register("cataclyst_bone");
    Item GUARDIAN_HEART = ItemFactory.register("guardian_heart");
    Item GOLDEN_CREAM = ItemFactory.register("golden_cream");
    Item PALE_BALL = ItemFactory.register("pale_ball");
    Item LLAMA_FUR = ItemFactory.register("llama_fur");

    // --- Creature remnants

    Item ELDERS_EYE = ItemFactory.register("elders_eye");
    Item GOLEM_HEAD = ItemFactory.register("golem_head", item -> item.base(ItemBase.GOLEM_HEAD));
    Item DROWNED_CROWN = ItemFactory.register("drowned_crown");
    Item WANDERING_HEART = ItemFactory.register("wandering_heart");
    Item WANDERING_SOUL = ItemFactory.register("wandering_soul");

    // --- Mirage

    Item ENDER_BAG = ItemFactory.register("ender_bag", item -> item.palette(Palette.ENDER).base(ItemBase.ENDER_BAG));
    Item OBSCURE_ONYX = ItemFactory.register("obscure_onyx");
    Item MACE_UPGRADE = ItemFactory.register("mace_upgrade", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_UPGRADE = ItemFactory.register("mirage_upgrade", item -> item.family(ItemFamily.MIRAGE));
    Item BROKEN_ELYTRA = ItemFactory.register("broken_elytra");
    Item MIRAGE_ELYTRA = ItemFactory.register("mirage_elytra", item -> item.family(ItemFamily.MIRAGE).base(ItemBase.GLIDER));
    Item MIRAGE_MACE = ItemFactory.register("mirage_mace", item -> item.family(ItemFamily.MIRAGE).base(ItemBase.MIRAGE_MACE.damage(10).speed(0.4)));
    Item MIRAGE_APPLE = ItemFactory.register("mirage_apple", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_BLESSING = ItemFactory.register("mirage_blessing", item -> item.family(ItemFamily.MIRAGE).base(ItemBase.MIRAGE_BLESSING));
    Item MIRAGE_HELMET = ItemFactory.register("mirage_helmet", item -> item.family(ItemFamily.MIRAGE).armor(CataclysmArmorMaterial.MIRAGE, ArmorType.HELMET).trim(TrimMaterials.IRON, TrimPatterns.DUNE));
    Item MIRAGE_SCUTE = ItemFactory.register("mirage_scute", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_PEARL = ItemFactory.register("mirage_pearl", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_FLESH = ItemFactory.register("mirage_flesh", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_POWDER = ItemFactory.register("mirage_powder", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_BONE = ItemFactory.register("mirage_bone", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_TEAR = ItemFactory.register("mirage_tear", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_INGOT = ItemFactory.register("mirage_ingot", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_EYEBALL = ItemFactory.register("mirage_eyeball", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_QUARTZ = ItemFactory.register("mirage_quartz", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_ESSENCE = ItemFactory.register("mirage_essence", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_AMETHYST = ItemFactory.register("mirage_amethyst", item -> item.family(ItemFamily.MIRAGE));
    Item MIRAGE_WHALE_WING = ItemFactory.register("mirage_whale_wing", item -> item.family(ItemFamily.MIRAGE));

    // --- Paragon Totem and Pale

    Item PALE_HELMET = ItemFactory.register("pale_helmet", item -> item.family(ItemFamily.PALE).armor(CataclysmArmorMaterial.PALE, ArmorType.HELMET).description(true));
    Item PALE_CHESTPLATE = ItemFactory.register("pale_chestplate", item -> item.family(ItemFamily.PALE).armor(CataclysmArmorMaterial.PALE, ArmorType.CHESTPLATE).description(true));
    Item PALE_LEGGINGS = ItemFactory.register("pale_leggings", item -> item.family(ItemFamily.PALE).armor(CataclysmArmorMaterial.PALE, ArmorType.LEGGINGS).description(true));
    Item PALE_BOOTS = ItemFactory.register("pale_boots", item -> item.family(ItemFamily.PALE).armor(CataclysmArmorMaterial.PALE, ArmorType.BOOTS).description(true));
    Item PALE_SWORD = ItemFactory.register("pale_sword", item -> item.family(ItemFamily.PALE).base(ItemBase.SWORD.damage(20).speed(-2.4)).description(true));
    Item PALE_BOW = ItemFactory.register("pale_bow", item -> item.family(ItemFamily.PALE).base(ItemBase.BOW).description(true));

    // --- Administration, Pantheon and exclusive items

    Item CHALICE_OF_THE_OVERWORLD = ItemFactory.register("chalice_of_the_overworld", item -> item.palette(Palette.CHALICE));
    Item CHALICE_OF_THE_NETHER = ItemFactory.register("chalice_of_the_nether", item -> item.palette(Palette.CHALICE));
    Item CHALICE_OF_THE_END = ItemFactory.register("chalice_of_the_end", item -> item.palette(Palette.CHALICE));

    // --- Shared shapes

    /** Creates a variant of the same registered item with a tier from 1 through 3. */
    static ItemStack upgrade(int tier) {
        if (tier < CataclysmDataComponents.MIN_UPGRADE_TIER || tier > CataclysmDataComponents.MAX_UPGRADE_TIER) {
            throw new IllegalArgumentException("Upgrade tier must be between 1 and 3: " + tier);
        }
        var stack = new ItemStack(CATACLYSM_UPGRADE);
        stack.set(CataclysmDataComponents.UPGRADE_TIER, tier);
        return stack;
    }

    static List<Item> getContent() {
        return CataclysmRegistry.contentOf(CataclysmItems.class, Item.class);
    }

    static List<ItemStack> getCreativeContent(HolderLookup.Provider registries) {
        var items = new ArrayList<>(getContent());
        items.remove(CATACLYSM_UPGRADE);
        items.add(items.indexOf(LEMEGETON) + 1, CATACLYSM_UPGRADE);
        return items.stream().flatMap(item -> ItemFactory.stacksOf(item, registries).stream()).toList();
    }

    static void registerCreativeModeTab() {
        Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Cataclysm.createNamespacedKey("items"),
            FabricItemGroup.builder()
                .title(Component.literal("Cataclysm"))
                .icon(() -> new ItemStack(CataclysmItems.PARAGON_PEARL))
                .displayItems((params, output) ->
                    CataclysmItems.getCreativeContent(params.holders()).forEach(output::accept))
                .build()
        );
    }
}
