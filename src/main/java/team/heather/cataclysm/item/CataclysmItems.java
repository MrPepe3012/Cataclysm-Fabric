package team.heather.cataclysm.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.component.mortality.MortalityTable;
import team.heather.cataclysm.block.CataclysmBlocks;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.data.UpgradeTierComponent;
import team.heather.cataclysm.item.feature.arcane.ArcaneMaceItem;
import team.heather.cataclysm.item.feature.arcane.ArcaneShieldItem;
import team.heather.cataclysm.item.feature.calamity.CalamityFoodItem;
import team.heather.cataclysm.item.feature.lemegeton.LemegetonItem;
import team.heather.cataclysm.item.feature.mirage.EnderBagItem;
import team.heather.cataclysm.item.feature.mirage.MirageElytraItem;
import team.heather.cataclysm.item.feature.mirage.MirageMaceItem;
import team.heather.cataclysm.item.feature.paragon.ParagonBlessingItem;
import team.heather.cataclysm.item.feature.paragon.ParagonTotemItem;
import team.heather.cataclysm.item.feature.totem.TotemItem;
import team.heather.cataclysm.item.feature.twisted.TwistedPickaxeItem;

import java.util.ArrayList;
import java.util.List;

public interface CataclysmItems {

    Item PARAGON_BLESSING = ItemFactory.register("paragon_blessing", properties -> new ParagonBlessingItem(properties, false), ItemFamily.PARAGON);
    Item PARAGON_PEARL = ItemFactory.register("paragon_pearl", EnderpearlItem::new, ItemFamily.PARAGON, item -> item.props(properties -> properties.stacksTo(16)));
    Item PARAGON_TOTEM = ItemFactory.register("paragon_totem", ParagonTotemItem::new, ItemFamily.PARAGON);
    Item PARAGON_QUARTZ = ItemFactory.register("paragon_quartz", Rarity.RARE);

    Item LEMEGETON = ItemFactory.register("lemegeton", LemegetonItem::new, ItemFamily.LEMEGETON);
    Item CATACLYSM_UPGRADE = ItemFactory.register("cataclysm_upgrade", ItemFamily.LEMEGETON, item -> item.variants(CataclysmDataComponents.UPGRADE_TIER, UpgradeTierComponent.values()));
    Item VOIDSTONE = ItemFactory.register("voidstone", Rarity.UNCOMMON);
    Item TECTONITE = ItemFactory.register("tectonite", Rarity.UNCOMMON);
    Item ONYX = ItemFactory.register("onyx", Rarity.UNCOMMON);

    Item TWISTED_RELIC = ItemFactory.register("twisted_relic", ItemFamily.TWISTED, item -> item.glint().rarity(Rarity.RARE));
    Item TWISTED_SWORD = ItemFactory.register("twisted_sword", ItemFamily.TWISTED, item -> item.sword(12, -2.4));
    Item TWISTED_PICKAXE = ItemFactory.register("twisted_pickaxe", TwistedPickaxeItem::new, ItemFamily.TWISTED, item -> item.pickaxe(8, -3.0));
    Item TWISTED_AXE = ItemFactory.register("twisted_axe", ItemFamily.TWISTED, item -> item.axe(16, -3.0));
    Item TWISTED_SHOVEL = ItemFactory.register("twisted_shovel", ItemFamily.TWISTED, item -> item.shovel(7.5, 0));
    Item TWISTED_HOE = ItemFactory.register("twisted_hoe", ItemFamily.TWISTED, item -> item.hoe(2, 0));
    Item TWISTED_FLESH = ItemFactory.register("twisted_flesh");
    Item TWISTED_BONE = ItemFactory.register("twisted_bone");
    Item TWISTED_POWDER = ItemFactory.register("twisted_powder");
    Item TWISTED_STRING = ItemFactory.register("twisted_string");
    Item TWISTED_PEARL = ItemFactory.register("twisted_pearl");
    Item TWISTED_ROD = ItemFactory.register("twisted_rod");
    Item TWISTED_INGOT = ItemFactory.register("twisted_ingot", Rarity.UNCOMMON);
    Item VOIDHEART = ItemFactory.register("voidheart", Rarity.RARE);

    Item ARCANE_MACE = ItemFactory.register("arcane_mace", ArcaneMaceItem::new, ItemFamily.ARCANE, item -> item.damage(8).speed(0.6));
    Item ARCANE_BOW = ItemFactory.register("arcane_bow", BowItem::new, ItemFamily.ARCANE, item -> item.props(properties -> properties.stacksTo(1).enchantable(1)));
    Item ARCANE_SHIELD = ItemFactory.register("arcane_shield", ArcaneShieldItem::new, ItemFamily.ARCANE);
    Item ARCANE_TRIDENT = ItemFactory.register("arcane_trident", ItemFamily.ARCANE, item -> item.trident(12, 1.1));
    Item ARCANE_TOTEM = ItemFactory.register("arcane_totem", properties -> new TotemItem(properties, MortalityTable.ARCANE), ItemFamily.ARCANE);
    Item ARCANE_ROD = ItemFactory.register("arcane_rod", ItemFamily.ARCANE, Rarity.UNCOMMON);
    Item ARCANE_CORE = ItemFactory.register("arcane_core", properties -> new BlockItem(CataclysmBlocks.ARCANE_CORE, properties), ItemFamily.ARCANE);
    Item ARCANE_NUGGET = ItemFactory.register("arcane_nugget", ItemFamily.ARCANE, Rarity.UNCOMMON);
    Item ARCANE_INGOT = ItemFactory.register("arcane_ingot", ItemFamily.ARCANE, Rarity.UNCOMMON);
    Item ARCANE_SCUTE = ItemFactory.register("arcane_scute", ItemFamily.ARCANE, Rarity.UNCOMMON);

    Item CALAMITY_HELMET = ItemFactory.register("calamity_helmet", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.HELMET));
    Item CALAMITY_CHESTPLATE = ItemFactory.register("calamity_chestplate", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.CHESTPLATE));
    Item CALAMITY_LEGGINGS = ItemFactory.register("calamity_leggings", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.LEGGINGS));
    Item CALAMITY_BOOTS = ItemFactory.register("calamity_boots", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.BOOTS));
    Item CALAMITY_TOTEM = ItemFactory.register("calamity_totem", properties -> new TotemItem(properties, MortalityTable.CALAMITY), ItemFamily.CALAMITY);
    Item ENCHANTED_CALAMITY_APPLE = ItemFactory.register("enchanted_calamity_apple", properties -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.ENCHANTED_APPLE), ItemFamily.CALAMITY, item -> item.glint().rarity(Rarity.EPIC));
    Item CALAMITY_APPLE = ItemFactory.register("calamity_apple", properties -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.APPLE), ItemFamily.CALAMITY);
    Item CALAMITY_CARROT = ItemFactory.register("calamity_carrot", properties -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.CARROT), ItemFamily.CALAMITY);
    Item CALAMITY_KEY = ItemFactory.register("calamity_key", ItemFamily.CALAMITY, Rarity.UNCOMMON);
    Item CALAMITY_NUGGET = ItemFactory.register("calamity_nugget", ItemFamily.CALAMITY, Rarity.UNCOMMON);
    Item CALAMITY_INGOT = ItemFactory.register("calamity_ingot", ItemFamily.CALAMITY, Rarity.UNCOMMON);

    Item MIDWAY_RELIC = ItemFactory.register("midway_relic", ItemFamily.MIDWAY, ItemFactory::glint);
    Item UR_TEAR = ItemFactory.register("ur_tear", Rarity.UNCOMMON);
    Item TOXIC_MEMBRANE = ItemFactory.register("toxic_membrane", Rarity.UNCOMMON);
    Item NIGHTMARE_BONE = ItemFactory.register("nightmare_bone", Rarity.UNCOMMON);
    Item CATACLYST_BONE = ItemFactory.register("cataclyst_bone", Rarity.UNCOMMON);
    Item GUARDIAN_HEART = ItemFactory.register("guardian_heart", Rarity.UNCOMMON);
    Item GOLDEN_CREAM = ItemFactory.register("golden_cream", Rarity.UNCOMMON);
    Item PALE_BALL = ItemFactory.register("pale_ball", Rarity.UNCOMMON);
    Item LLAMA_FUR = ItemFactory.register("llama_fur", Rarity.UNCOMMON);

    Item MIRAGE_HELMET = ItemFactory.register("mirage_helmet", ItemFamily.MIRAGE, item -> item.armor(CataclysmArmorMaterial.MIRAGE, ArmorType.HELMET));
    Item MIRAGE_ELYTRA = ItemFactory.register("mirage_elytra", MirageElytraItem::new, ItemFamily.MIRAGE);
    Item MIRAGE_MACE = ItemFactory.register("mirage_mace", MirageMaceItem::new, ItemFamily.MIRAGE, item -> item.damage(10).speed(0.4));
    Item MIRAGE_BLESSING = ItemFactory.register("mirage_blessing", properties -> new ParagonBlessingItem(properties, true), ItemFamily.MIRAGE);
    Item ENDER_BAG = ItemFactory.register("ender_bag", EnderBagItem::new, item -> item.rarity(Rarity.RARE));
    Item MIRAGE_APPLE = ItemFactory.register("mirage_apple", ItemFamily.MIRAGE);
    Item MACE_UPGRADE = ItemFactory.register("mace_upgrade", ItemFamily.MIRAGE);
    Item MIRAGE_UPGRADE = ItemFactory.register("mirage_upgrade", ItemFamily.MIRAGE);
    Item OBSCURE_ONYX = ItemFactory.register("obscure_onyx", Rarity.RARE);
    Item MIRAGE_INGOT = ItemFactory.register("mirage_ingot", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_SCUTE = ItemFactory.register("mirage_scute", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_PEARL = ItemFactory.register("mirage_pearl", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_FLESH = ItemFactory.register("mirage_flesh", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_POWDER = ItemFactory.register("mirage_powder", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_BONE = ItemFactory.register("mirage_bone", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_TEAR = ItemFactory.register("mirage_tear", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_EYEBALL = ItemFactory.register("mirage_eyeball", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_QUARTZ = ItemFactory.register("mirage_quartz", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_ESSENCE = ItemFactory.register("mirage_essence", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_AMETHYST = ItemFactory.register("mirage_amethyst", ItemFamily.MIRAGE, Rarity.RARE);
    Item MIRAGE_WHALE_WING = ItemFactory.register("mirage_whale_wing", ItemFamily.MIRAGE, Rarity.RARE);
    Item WANDERING_HEART = ItemFactory.register("wandering_heart", Rarity.RARE);
    Item WANDERING_SOUL = ItemFactory.register("wandering_soul", Rarity.RARE);
    Item ELDERS_EYE = ItemFactory.register("elders_eye", Rarity.RARE);
    Item GOLEM_HEAD = ItemFactory.register("golem_head", CataclysmBlocks.GOLEM_HEAD::toItem, item -> item.rarity(Rarity.RARE).props(properties -> properties.equippableUnswappable(EquipmentSlot.HEAD)));
    Item DROWNED_CROWN = ItemFactory.register("drowned_crown", Rarity.RARE);

    Item PALE_HELMET = ItemFactory.register("pale_helmet", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.HELMET));
    Item PALE_CHESTPLATE = ItemFactory.register("pale_chestplate", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.CHESTPLATE));
    Item PALE_LEGGINGS = ItemFactory.register("pale_leggings", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.LEGGINGS));
    Item PALE_BOOTS = ItemFactory.register("pale_boots", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.BOOTS));
    Item PALE_SWORD = ItemFactory.register("pale_sword", ItemFamily.PALE, item -> item.sword(20, -2.4));
    Item PALE_BOW = ItemFactory.register("pale_bow", BowItem::new, ItemFamily.PALE, item -> item.props(properties -> properties.stacksTo(1).enchantable(1)));

    Item CHALICE_OF_THE_OVERWORLD = ItemFactory.register("chalice_of_the_overworld", item -> item.rarity(Rarity.EPIC).props(properties -> properties.stacksTo(1)));
    Item CHALICE_OF_THE_NETHER = ItemFactory.register("chalice_of_the_nether", item -> item.rarity(Rarity.EPIC).props(properties -> properties.stacksTo(1)));
    Item CHALICE_OF_THE_END = ItemFactory.register("chalice_of_the_end", item -> item.rarity(Rarity.EPIC).props(properties -> properties.stacksTo(1)));

    static void initialize() {
        Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Cataclysm.resourceLocation("items"),
            FabricItemGroup.builder()
                .title(Component.literal("Cataclysm"))
                .icon(() -> new ItemStack(CataclysmItems.PARAGON_PEARL))
                .displayItems((params, output) -> getCreativeContent(params.holders()).forEach(output::accept))
                .build()
        );
    }

    static List<Item> getContent() {
        return BuiltInRegistries.ITEM.stream()
            .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(Cataclysm.MOD_ID))
            .toList();
    }

    static List<ItemStack> getCreativeContent(HolderLookup.Provider registries) {
        var items = new ArrayList<>(getContent());
        items.remove(CATACLYSM_UPGRADE);
        items.add(items.indexOf(LEMEGETON) + 1, CATACLYSM_UPGRADE);
        return items.stream().flatMap(item -> ItemFactory.stacksOf(item, registries).stream()).toList();
    }
}
