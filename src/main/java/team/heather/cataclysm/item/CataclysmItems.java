package team.heather.cataclysm.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorType;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.item.armor.CataclysmArmorMaterial;
import team.heather.cataclysm.component.cardinal.mortality.MortalityTable;
import team.heather.cataclysm.item.arcane.ArcaneMaceItem;
import team.heather.cataclysm.item.arcane.ArcaneShieldItem;
import team.heather.cataclysm.item.paragon.ParagonBlessingItem;
import team.heather.cataclysm.item.calamity.CalamityFoodItem;
import team.heather.cataclysm.item.mirage.EnderBagItem;
import team.heather.cataclysm.item.lemegeton.LemegetonItem;
import team.heather.cataclysm.item.mirage.MirageElytraItem;
import team.heather.cataclysm.item.mirage.MirageMaceItem;
import team.heather.cataclysm.item.paragon.ParagonTotemItem;
import team.heather.cataclysm.item.totem.TotemItem;
import team.heather.cataclysm.item.twisted.TwistedPickaxeItem;
import team.heather.cataclysm.registry.CataclysmRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public interface CataclysmItems extends CataclysmRegistry<Item> {

    Item PARAGON_BLESSING = ItemFactory.register("paragon_blessing", properties -> new ParagonBlessingItem(properties, false), ItemFamily.PARAGON);
    Item PARAGON_PEARL = ItemFactory.register("paragon_pearl", EnderpearlItem::new, ItemFamily.PARAGON, item -> item.props(properties -> properties.stacksTo(16)));
    Item PARAGON_TOTEM = ItemFactory.register("paragon_totem", ParagonTotemItem::new, ItemFamily.PARAGON);
    Item PARAGON_QUARTZ = ItemFactory.register("paragon_quartz");

    Item LEMEGETON = ItemFactory.register("lemegeton", LemegetonItem::new, ItemFamily.LEMEGETON);
    Item CATACLYSM_UPGRADE = ItemFactory.register("cataclysm_upgrade", ItemFamily.LEMEGETON, item -> item
        .props(properties -> properties.component(CataclysmRegistry.DATA_COMPONENTS.UPGRADE_TIER, CataclysmRegistry.DATA_COMPONENTS.MIN_UPGRADE_TIER))
        .variants(CataclysmRegistry.DATA_COMPONENTS.UPGRADE_TIER, IntStream
            .rangeClosed(CataclysmRegistry.DATA_COMPONENTS.MIN_UPGRADE_TIER, CataclysmRegistry.DATA_COMPONENTS.MAX_UPGRADE_TIER)
            .boxed()
            .toList()));
    Item VOIDSTONE = ItemFactory.register("voidstone");
    Item TECTONITE = ItemFactory.register("tectonite");
    Item ONYX = ItemFactory.register("onyx");

    Item TWISTED_RELIC = ItemFactory.register("twisted_relic", ItemFamily.TWISTED, ItemFactory::glint);
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
    Item TWISTED_INGOT = ItemFactory.register("twisted_ingot");
    Item VOIDHEART = ItemFactory.register("voidheart");

    Item ARCANE_MACE = ItemFactory.register("arcane_mace", ArcaneMaceItem::new, ItemFamily.ARCANE, item -> item.damage(8).speed(0.6));
    Item ARCANE_BOW = ItemFactory.register("arcane_bow", BowItem::new, ItemFamily.ARCANE, item -> item.props(properties -> properties.stacksTo(1).enchantable(1)));
    Item ARCANE_SHIELD = ItemFactory.register("arcane_shield", ArcaneShieldItem::new, ItemFamily.ARCANE);
    Item ARCANE_TRIDENT = ItemFactory.register("arcane_trident", TridentItem::new, ItemFamily.ARCANE, item -> item
        .props(properties -> properties.stacksTo(1).enchantable(1)
            .component(DataComponents.TOOL, TridentItem.createToolProperties())
            .component(DataComponents.WEAPON, new Weapon(1)))
        .damage(12)
        .speed(1.1));
    Item ARCANE_TOTEM = ItemFactory.register("arcane_totem", properties -> new TotemItem(properties, MortalityTable.ARCANE), ItemFamily.ARCANE);
    Item ARCANE_ROD = ItemFactory.register("arcane_rod", ItemFamily.ARCANE);
    Item ARCANE_CORE = ItemFactory.register("arcane_core", properties -> new BlockItem(CataclysmRegistry.BLOCKS.ARCANE_CORE, properties), ItemFamily.ARCANE);
    Item ARCANE_NUGGET = ItemFactory.register("arcane_nugget", ItemFamily.ARCANE);
    Item ARCANE_INGOT = ItemFactory.register("arcane_ingot", ItemFamily.ARCANE);
    Item ARCANE_SCUTE = ItemFactory.register("arcane_scute", ItemFamily.ARCANE);

    Item CALAMITY_HELMET = ItemFactory.register("calamity_helmet", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.HELMET));
    Item CALAMITY_CHESTPLATE = ItemFactory.register("calamity_chestplate", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.CHESTPLATE));
    Item CALAMITY_LEGGINGS = ItemFactory.register("calamity_leggings", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.LEGGINGS));
    Item CALAMITY_BOOTS = ItemFactory.register("calamity_boots", ItemFamily.CALAMITY, item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.BOOTS));
    Item CALAMITY_TOTEM = ItemFactory.register("calamity_totem", properties -> new TotemItem(properties, MortalityTable.CALAMITY), ItemFamily.CALAMITY);
    Item CALAMITY_APPLE = ItemFactory.register("calamity_apple", properties -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.APPLE), ItemFamily.CALAMITY);
    Item ENCHANTED_CALAMITY_APPLE = ItemFactory.register("enchanted_calamity_apple", properties -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.ENCHANTED_APPLE), ItemFamily.CALAMITY, item -> item.glint());
    Item CALAMITY_CARROT = ItemFactory.register("calamity_carrot", properties -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.CARROT), ItemFamily.CALAMITY);
    Item CALAMITY_KEY = ItemFactory.register("calamity_key", ItemFamily.CALAMITY);
    Item CALAMITY_NUGGET = ItemFactory.register("calamity_nugget", ItemFamily.CALAMITY);
    Item CALAMITY_INGOT = ItemFactory.register("calamity_ingot", ItemFamily.CALAMITY);

    Item MIDWAY_RELIC = ItemFactory.register("midway_relic", ItemFamily.MIDWAY, item -> item.glint());
    Item UR_TEAR = ItemFactory.register("ur_tear");
    Item TOXIC_MEMBRANE = ItemFactory.register("toxic_membrane");
    Item NIGHTMARE_BONE = ItemFactory.register("nightmare_bone");
    Item CATACLYST_BONE = ItemFactory.register("cataclyst_bone");
    Item GUARDIAN_HEART = ItemFactory.register("guardian_heart");
    Item GOLDEN_CREAM = ItemFactory.register("golden_cream");
    Item PALE_BALL = ItemFactory.register("pale_ball");
    Item LLAMA_FUR = ItemFactory.register("llama_fur");

    Item ENDER_BAG = ItemFactory.register("ender_bag", EnderBagItem::new, item -> item.palette(ItemPalette.ENDER));
    Item MIRAGE_ELYTRA = ItemFactory.register("mirage_elytra", MirageElytraItem::new, ItemFamily.MIRAGE);
    Item MIRAGE_MACE = ItemFactory.register("mirage_mace", MirageMaceItem::new, ItemFamily.MIRAGE, item -> item.damage(10).speed(0.4));
    Item MIRAGE_BLESSING = ItemFactory.register("mirage_blessing", properties -> new ParagonBlessingItem(properties, true), ItemFamily.MIRAGE);
    Item MIRAGE_HELMET = ItemFactory.register("mirage_helmet", ItemFamily.MIRAGE, item -> item.armor(CataclysmArmorMaterial.MIRAGE, ArmorType.HELMET));
    Item MIRAGE_APPLE = ItemFactory.register("mirage_apple", ItemFamily.MIRAGE);
    Item MACE_UPGRADE = ItemFactory.register("mace_upgrade", ItemFamily.MIRAGE);
    Item MIRAGE_UPGRADE = ItemFactory.register("mirage_upgrade", ItemFamily.MIRAGE);
    Item OBSCURE_ONYX = ItemFactory.register("obscure_onyx");
    Item MIRAGE_INGOT = ItemFactory.register("mirage_ingot", ItemFamily.MIRAGE);
    Item MIRAGE_SCUTE = ItemFactory.register("mirage_scute", ItemFamily.MIRAGE);
    Item MIRAGE_PEARL = ItemFactory.register("mirage_pearl", ItemFamily.MIRAGE);
    Item MIRAGE_FLESH = ItemFactory.register("mirage_flesh", ItemFamily.MIRAGE);
    Item MIRAGE_POWDER = ItemFactory.register("mirage_powder", ItemFamily.MIRAGE);
    Item MIRAGE_BONE = ItemFactory.register("mirage_bone", ItemFamily.MIRAGE);
    Item MIRAGE_TEAR = ItemFactory.register("mirage_tear", ItemFamily.MIRAGE);
    Item MIRAGE_EYEBALL = ItemFactory.register("mirage_eyeball", ItemFamily.MIRAGE);
    Item MIRAGE_QUARTZ = ItemFactory.register("mirage_quartz", ItemFamily.MIRAGE);
    Item MIRAGE_ESSENCE = ItemFactory.register("mirage_essence", ItemFamily.MIRAGE);
    Item MIRAGE_AMETHYST = ItemFactory.register("mirage_amethyst", ItemFamily.MIRAGE);
    Item MIRAGE_WHALE_WING = ItemFactory.register("mirage_whale_wing", ItemFamily.MIRAGE);
    Item WANDERING_HEART = ItemFactory.register("wandering_heart");
    Item WANDERING_SOUL = ItemFactory.register("wandering_soul");
    Item ELDERS_EYE = ItemFactory.register("elders_eye");
    Item GOLEM_HEAD = ItemFactory.register("golem_head", properties -> new StandingAndWallBlockItem(CataclysmRegistry.BLOCKS.GOLEM_HEAD, CataclysmRegistry.BLOCKS.GOLEM_WALL_HEAD, Direction.DOWN, properties), item -> item.props(properties -> properties.equippableUnswappable(EquipmentSlot.HEAD)));
    Item DROWNED_CROWN = ItemFactory.register("drowned_crown");

    Item PALE_HELMET = ItemFactory.register("pale_helmet", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.HELMET));
    Item PALE_CHESTPLATE = ItemFactory.register("pale_chestplate", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.CHESTPLATE));
    Item PALE_LEGGINGS = ItemFactory.register("pale_leggings", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.LEGGINGS));
    Item PALE_BOOTS = ItemFactory.register("pale_boots", ItemFamily.PALE, item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.BOOTS));
    Item PALE_SWORD = ItemFactory.register("pale_sword", ItemFamily.PALE, item -> item.sword(20, -2.4));
    Item PALE_BOW = ItemFactory.register("pale_bow", BowItem::new, ItemFamily.PALE, item -> item.props(properties -> properties.stacksTo(1).enchantable(1)));

    Item CHALICE_OF_THE_OVERWORLD = ItemFactory.register("chalice_of_the_overworld", item -> item.palette(ItemPalette.CHALICE));
    Item CHALICE_OF_THE_NETHER = ItemFactory.register("chalice_of_the_nether", item -> item.palette(ItemPalette.CHALICE));
    Item CHALICE_OF_THE_END = ItemFactory.register("chalice_of_the_end", item -> item.palette(ItemPalette.CHALICE));

    default List<ItemStack> getCreativeContent(HolderLookup.Provider registries) {
        var items = new ArrayList<>(this.contentOf());
        items.remove(CATACLYSM_UPGRADE);
        items.add(items.indexOf(LEMEGETON) + 1, CATACLYSM_UPGRADE);
        return items.stream().flatMap(item -> ItemFactory.stacksOf(item, registries).stream()).toList();
    }

    @Override
    default void onBootstrap() {
        Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Cataclysm.createNamespacedKey("items"),
            FabricItemGroup.builder()
                .title(Component.literal("Cataclysm"))
                .icon(() -> new ItemStack(CataclysmItems.PARAGON_PEARL))
                .displayItems((params, output) -> this.getCreativeContent(params.holders()).forEach(output::accept))
                .build()
        );
    }
}
