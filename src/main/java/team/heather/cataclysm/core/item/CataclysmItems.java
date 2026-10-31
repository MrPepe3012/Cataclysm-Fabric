package team.heather.cataclysm.core.item;

import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.core.CataclysmRegistry;
import team.heather.cataclysm.core.item.material.CataclysmArmorMaterial;
import team.heather.cataclysm.core.item.material.CataclysmToolMaterial;
import team.heather.cataclysm.core.item.paragon.ImmunityTable;
import team.heather.cataclysm.core.item.totem.ParagonTotem;
import team.heather.cataclysm.core.item.totem.mortality.MortalityTable;
import team.heather.cataclysm.core.item.pale.PaleWeaponItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.List;

public interface CataclysmItems extends CataclysmRegistry {

    Item PARAGON_QUARTZ = Foundry.item("paragon_quartz", Foundry::build);
    Item PARAGON_BLESSING = Foundry.item("paragon_blessing", item -> item.paragon(ImmunityTable.PARAGON_BLESSING).stack(64).build());
    Item PARAGON_PEARL = Foundry.item("paragon_pearl", item -> item.paragon(ImmunityTable.PARAGON_PEARL).stack(16).build());
    Item PARAGON_TOTEM = Foundry.item("paragon_totem", item -> item.as(ParagonTotem::new).build());

    Item ARCANE_TOTEM = Foundry.item("arcane_totem", item -> item.totem(MortalityTable.ARCANE).build());

    Item CALAMITY_TOTEM = Foundry.item("calamity_totem", item -> item.totem(MortalityTable.CALAMITY).build());

    Item MIRAGE_BLESSING = Foundry.item("mirage_blessing", item -> item.paragon(ImmunityTable.MIRAGE_BLESSING).stack(64).build());

    Item PALE_HELMET = Foundry.item("pale_helmet", item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.HELMET).unbreakable().fireResistant().build());
    Item PALE_CHESTPLATE = Foundry.item("pale_chestplate", item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.CHESTPLATE).unbreakable().fireResistant().build());
    Item PALE_LEGGINGS = Foundry.item("pale_leggings", item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.LEGGINGS).unbreakable().fireResistant().build());
    Item PALE_BOOTS = Foundry.item("pale_boots", item -> item.armor(CataclysmArmorMaterial.PALE, ArmorType.BOOTS).unbreakable().fireResistant().build());
    Item PALE_SWORD = Foundry.item("pale_sword", item -> item.as(PaleWeaponItem::new).sword(CataclysmToolMaterial.PALE, 3.0F, -2.4F).unbreakable().build());
    Item PALE_BOW = Foundry.item("pale_bow", item -> item.as(BowItem::new).durable(500).unbreakable().build());

    Item CALAMITY_HELMET = Foundry.item("calamity_helmet", item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.HELMET).unbreakable().fireResistant().build());
    Item CALAMITY_CHESTPLATE = Foundry.item("calamity_chestplate", item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.CHESTPLATE).unbreakable().fireResistant().build());
    Item CALAMITY_LEGGINGS = Foundry.item("calamity_leggings", item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.LEGGINGS).unbreakable().fireResistant().build());
    Item CALAMITY_BOOTS = Foundry.item("calamity_boots", item -> item.armor(CataclysmArmorMaterial.CALAMITY, ArmorType.BOOTS).unbreakable().fireResistant().build());

    static List<Item> getContent() {
        return CataclysmRegistry.contentOf(CataclysmItems.class, Item.class);
    }

    static void registerCreativeModeTab() {
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                Cataclysm.createNamespacedKey("items"),
                FabricItemGroup.builder()
                        .title(Component.literal("Cataclysm"))
                        .icon(() -> new ItemStack(CataclysmItems.PARAGON_PEARL))
                        .displayItems((params, output) ->
                                CataclysmItems.getContent().forEach(output::accept))
                        .build()
        );
    }
}
