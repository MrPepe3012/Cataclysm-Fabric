package team.heather.cataclysm.registry.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.material.CataclysmArmorMaterial;

import java.util.List;
import java.util.ArrayList;

public interface CataclysmItems extends CataclysmRegistry {

    Item PARAGON_BLESSING = Foundry.item("paragons_blessing", item -> item.palette(Palette.PARAGON).glint().build());
    Item PARAGON_PEARL = Foundry.item("paragon_pearl", item -> item.glint().palette(Palette.PARAGON).build());
    Item PARAGON_TOTEM = Foundry.item("paragon_totem", item -> item.base(ItemBase.TOTEM).palette(Palette.PARAGON).glint().build());
    Item PARAGON_QUARTZ = Foundry.item("paragons_quartz", Foundry::build);

    Item LEMEGETON = Foundry.item("lemegeton", item -> item.base(ItemBase.LEMEGETON).palette(Palette.LEMEGETON).build());
    Item CATACLYSM_UPGRADE = Foundry.item("cataclysm_upgrade", CataclysmUpgradeItem::create);
    Item TECTONITE = Foundry.item("tectonite", Foundry::build);
    Item ONYX = Foundry.item("onyx", Foundry::build);
    Item VOID_STONE = Foundry.item("void_stone", Foundry::build);

    // --- Twisted

    Item TWISTED_SWORD = twistedTool("twisted_sword", ItemBase.SWORD, 12, -2.4);
    Item TWISTED_PICKAXE = twistedTool("twisted_pickaxe", ItemBase.TWISTED_PICKAXE, 8, -3.0);
    Item TWISTED_AXE = twistedTool("twisted_axe", ItemBase.AXE, 16, -3.0);
    Item TWISTED_SHOVEL = twistedTool("twisted_shovel", ItemBase.SHOVEL, 7.5, null);
    Item TWISTED_HOE = twistedTool("twisted_hoe", ItemBase.HOE, 2, null);
    Item TWISTED_FLESH = material("twisted_flesh");
    Item TWISTED_BONE = material("twisted_bone");
    Item TWISTED_POWDER = material("twisted_powder");
    Item TWISTED_STRING = material("twisted_string");
    Item TWISTED_PEARL = material("twisted_pearl");
    Item TWISTED_ROD = material("twisted_rod");
    Item TWISTED_INGOT = material("twisted_ingot");

    // --- Arcane

    Item ARCANE_MACE = Foundry.item("arcane_mace", item -> item.base(ItemBase.MACE).palette(Palette.ARCANE).description(true).attack(8, 0.6).enchant(Enchantments.DENSITY, 5).enchant(Enchantments.WIND_BURST, 3).unbreakable().build());
    Item ARCANE_BOW = Foundry.item("arcane_bow", item -> item.base(ItemBase.BOW).palette(Palette.ARCANE).description(true).enchant(Enchantments.POWER, 10).enchant(Enchantments.INFINITY, 1).unbreakable().build());
    Item ARCANE_SHIELD = Foundry.item("arcane_shield", item -> item.base(ItemBase.SHIELD).palette(Palette.ARCANE).glint().unbreakable().build());
    Item ARCANE_TRIDENT = Foundry.item("arcane_trident", item -> item.base(ItemBase.TRIDENT).palette(Palette.ARCANE).description(true).attack(12, 1.1).enchant(Enchantments.RIPTIDE, 5).unbreakable().build());
    Item ARCANE_TOTEM = Foundry.item("arcane_totem", item -> item.base(ItemBase.TOTEM).palette(Palette.ARCANE).glint().build());
    Item ARCANE_ROD = Foundry.item("arcane_rod", item -> item.glint().build());
    Item ARCANE_CORE = Foundry.item("arcane_core", item -> item.base(ItemBase.ARCANE_CORE).build());
    Item ARCANE_NUGGET = Foundry.item("arcane_nugget", item -> item.glint().build());
    Item ARCANE_INGOT = Foundry.item("arcane_ingot", item -> item.glint().build());
    Item ARCANE_SCUTE = Foundry.item("arcane_scute", item -> item.glint().build());

    // --- Relics and special materials

    Item VOID_HEART = Foundry.item("void_heart", item -> item.palette(Palette.VOID_HEART)
        .descriptionArguments(Component.literal("v").withStyle(ChatFormatting.OBFUSCATED), Component.literal("p").withStyle(ChatFormatting.OBFUSCATED))
        .glint().build());
    Item TWISTED_RELIC = Foundry.item("twisted_relic", item -> item.palette(Palette.TWISTED).glint().build());
    Item UR_TEAR = material("ur_tear");
    Item TOXIC_MEMBRANE = material("toxic_membrane");
    Item GOLDEN_CREAM = Foundry.item("golden_cream", Foundry::build);
    Item NIGHTMARE_BONE = Foundry.item("nightmare_bone", Foundry::build);
    Item CATACLYST_BONE = material("cataclyst_bone");
    Item GUARDIAN_HEART = material("guardian_heart");
    Item PALE_BALL = material("pale_ball");
    Item LLAMA_FUR = material("llama_fur");
    Item MIDWAY_RELIC = Foundry.item("midway_relic", item -> item.palette(Palette.CALAMITY).glint().build());

    // --- Calamity

    Item CALAMITY_KEY = Foundry.item("calamity_key", item -> item.palette(Palette.CALAMITY).build());
    Item CALAMITY_NUGGET = Foundry.item("calamity_nugget", Foundry::build);
    Item CALAMITY_INGOT = Foundry.item("calamity_ingot", Foundry::build);
    Item CALAMITY_HELMET = calamityArmor("calamity_helmet", ArmorType.HELMET);
    Item CALAMITY_CHESTPLATE = calamityArmor("calamity_chestplate", ArmorType.CHESTPLATE);
    Item CALAMITY_LEGGINGS = calamityArmor("calamity_leggings", ArmorType.LEGGINGS);
    Item CALAMITY_BOOTS = calamityArmor("calamity_boots", ArmorType.BOOTS);
    Item CALAMITY_TOTEM = Foundry.item("calamity_totem", item -> item.base(ItemBase.TOTEM).palette(Palette.CALAMITY).build());
    Item CALAMITY_APPLE = Foundry.item("calamity_apple", item -> item.base(ItemBase.GOLDEN_FOOD).palette(Palette.CALAMITY).build());
    Item ENCHANTED_CALAMITY_APPLE = Foundry.item("enchanted_calamity_apple", item -> item.base(ItemBase.ENCHANTED_FOOD).palette(Palette.CALAMITY).build());
    Item CALAMITY_CARROT = Foundry.item("calamity_carrot", item -> item.base(ItemBase.CARROT_FOOD).palette(Palette.CALAMITY).build());

    // --- Creature remnants

    Item ELDERS_EYE = material("elders_eye");
    Item GOLEM_HEAD = Foundry.item("golem_head", item -> item.base(ItemBase.GOLEM_HEAD).build());
    Item DROWNED_CROWN = material("drowned_crown");
    Item WANDERING_HEART = material("wandering_heart");
    Item WANDERING_SOUL = material("wandering_soul");

    // --- Mirage

    Item MIRAGE_SCUTE = material("mirage_scute");
    Item MIRAGE_HELMET = Foundry.item("mirage_helmet", item -> item.palette(Palette.MIRAGE).armor(CataclysmArmorMaterial.MIRAGE, ArmorType.HELMET).trim(TrimMaterials.IRON, TrimPatterns.DUNE).unbreakable().build());
    Item MIRAGE_PEARL = material("mirage_pearl");
    Item MIRAGE_FLESH = material("mirage_flesh");
    Item MIRAGE_POWDER = material("mirage_powder");
    Item MIRAGE_BONE = material("mirage_bone");
    Item MIRAGE_TEAR = material("mirage_tear");
    Item MIRAGE_INGOT = material("mirage_ingot");
    Item MIRAGE_EYEBALL = material("mirage_eyeball");
    Item MIRAGE_QUARTZ = material("mirage_quartz");
    Item MIRAGE_ESSENCE = material("mirage_essence");
    Item MIRAGE_AMETHYST = material("mirage_amethyst");
    Item WHALE_WING = material("whale_wing");
    Item MIRAGE_BLESSING = Foundry.item("mirages_blessing", item -> item.palette(Palette.MIRAGE).glint().build());
    Item ENDER_BAG = Foundry.item("ender_bag", item -> item.palette(Palette.ENDER).build());
    Item OBSCURE_ONYX = Foundry.item("obscure_onyx", item -> item.build());
    Item MACE_UPGRADE = Foundry.item("mace_upgrade", item -> item.palette(Palette.MIRAGE).build());
    Item MIRAGE_UPGRADE = Foundry.item("mirage_upgrade", item -> item.palette(Palette.MIRAGE).build());
    Item BROKEN_ELYTRA = Foundry.item("broken_elytra", item -> item.build());
    Item MIRAGE_ELYTRA = Foundry.item("mirage_elytra", item -> item.base(ItemBase.GLIDER).palette(Palette.MIRAGE).unbreakable().build());
    Item MIRAGE_MACE = Foundry.item("mirage_mace_normal", item -> item.base(ItemBase.MACE).palette(Palette.MIRAGE).attack(10, 0.4).enchant(Enchantments.DENSITY, 7).enchant(Enchantments.WIND_BURST, 3).unbreakable().build());
    Item MIRAGE_APPLE = Foundry.item("mirage_apple", item -> item.palette(Palette.MIRAGE).build());

    // --- Paragon Totem and Pale

    Item PALE_TOKEN = Foundry.item("pale_token", item -> item.palette(Palette.PALE).glint().build());
    Item PALE_SWORD = Foundry.item("pale_sword", item -> item.base(ItemBase.SWORD).palette(Palette.PALE).description(true).attack(20, -2.4).enchant(Enchantments.BREACH, 7).enchant(Enchantments.SHARPNESS, 5).enchant(Enchantments.BANE_OF_ARTHROPODS, 5).enchant(Enchantments.SMITE, 5).enchant(Enchantments.LOOTING, 3).enchant(Enchantments.SWEEPING_EDGE, 3).unbreakable().build());
    Item PALE_BOW = Foundry.item("pale_bow", item -> item.base(ItemBase.BOW).palette(Palette.PALE).description(true).enchant(Enchantments.POWER, 20).enchant(Enchantments.BREACH, 7).enchant(Enchantments.INFINITY, 1).enchant(Enchantments.PUNCH, 2).enchant(Enchantments.FLAME, 2).unbreakable().build());
    Item PALE_HELMET = paleArmor("pale_helmet", ArmorType.HELMET);
    Item PALE_CHESTPLATE = paleArmor("pale_chestplate", ArmorType.CHESTPLATE);
    Item PALE_LEGGINGS = paleArmor("pale_leggings", ArmorType.LEGGINGS);
    Item PALE_BOOTS = paleArmor("pale_boots", ArmorType.BOOTS);

    // --- Administration, Pantheon and exclusive items

    Item OVERWORLDS_CHALICE = Foundry.item("overworlds_chalice", item -> item.palette(Palette.OVERWORLD).build());
    Item NETHERS_CHALICE = Foundry.item("nethers_chalice", item -> item.palette(Palette.NETHER).glint().build());
    Item ENDS_CHALICE = Foundry.item("ends_chalice", item -> item.palette(Palette.END).glint().build());
    Item GRYMPESITO_TOTEM = Foundry.item("grympesito_totem", item -> item.base(ItemBase.TOTEM).palette(Palette.PARAGON).glint().build());

    // --- Shared shapes

    private static Item material(String path) {
        return Foundry.item(path, Foundry::build);
    }

    /** Creates a variant of the same registered item with a tier from 1 through 3. */
    static ItemStack upgrade(int tier) {
        if (tier < CataclysmDataComponents.MIN_UPGRADE_TIER || tier > CataclysmDataComponents.MAX_UPGRADE_TIER) {
            throw new IllegalArgumentException("Upgrade tier must be between 1 and 3: " + tier);
        }
        var stack = new ItemStack(CATACLYSM_UPGRADE);
        stack.set(CataclysmDataComponents.UPGRADE_TIER, tier);
        return stack;
    }

    /** {@code speed} is the attack speed modifier; null keeps the player's base speed, as the original did. */
    private static Item twistedTool(String path, ItemBase base, double damage, Double speed) {
        return Foundry.item(path, item -> {
            item.base(base).palette(Palette.TWISTED).unbreakable();
            return (speed == null ? item.attack(damage) : item.attack(damage, speed)).build();
        });
    }

    private static Item calamityArmor(String path, ArmorType type) {
        return Foundry.item(path, item -> item.armor(CataclysmArmorMaterial.CALAMITY, type)
            .palette(Palette.CALAMITY)
            .trim(TrimMaterials.IRON, TrimPatterns.SPIRE).hide(DataComponents.TRIM).unbreakable().build());
    }

    private static Item paleArmor(String path, ArmorType type) {
        return Foundry.item(path, item -> item.armor(CataclysmArmorMaterial.PALE, type)
            .palette(Palette.PALE).description(true)
            .trim(TrimMaterials.IRON, TrimPatterns.TIDE).hide(DataComponents.TRIM).unbreakable().build());
    }

    static List<Item> getContent() {
        return CataclysmRegistry.contentOf(CataclysmItems.class, Item.class);
    }

    static List<ItemStack> getCreativeContent(HolderLookup.Provider registries) {
        var items = new ArrayList<>(getContent());
        items.remove(CATACLYSM_UPGRADE);
        items.add(items.indexOf(LEMEGETON) + 1, CATACLYSM_UPGRADE);
        return items.stream().flatMap(item -> Foundry.stacksOf(item, registries).stream()).toList();
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
