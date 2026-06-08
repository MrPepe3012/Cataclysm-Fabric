package com.cataclysm.registry;

import com.cataclysm.Cataclysm;
import com.cataclysm.registry.item.paragon.ParagonBlessingItem;
import com.cataclysm.registry.item.totem.TotemItem;
import com.cataclysm.registry.item.totem.mortality.MortalityTable;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class CataclysmItems {
    private static final List<Item> ITEMS = new ArrayList<>();

    public static final Item PARAGON_BLESSING = register("paragon_blessing", ParagonBlessingItem::new, new Item.Properties().rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    public static final Item ARCANE_TOTEM  = registerTotem("arcane_totem",  Rarity.RARE, MortalityTable.ARCANE);
    public static final Item CALAMITY_TOTEM = registerTotem("calamity_totem", Rarity.RARE, MortalityTable.CALAMITY);
    public static final Item PARAGON_TOTEM  = registerTotem("paragon_totem",  Rarity.EPIC, MortalityTable.PARAGON);

    private CataclysmItems() {}

    public static List<Item> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    private static Item registerTotem(String path, Rarity rarity, MortalityTable table) {
        return register(path, properties -> new TotemItem(properties, table), new Item.Properties().rarity(rarity));
    }

    private static Item register(String path, Function<Item.Properties, Item> function, Item.Properties properties) {
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Cataclysm.MOD_ID, path));
        Item item = function.apply(properties.setId(resourceKey));

        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        Item registered = Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
        ITEMS.add(registered);
        return registered;
    }

    static {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Cataclysm.resourceLocation("items"), FabricItemGroup.builder()
                .title(Component.literal("Cataclysm"))
                .icon(() -> new ItemStack(CataclysmItems.PARAGON_BLESSING))
                .displayItems((params, output) -> CataclysmItems.getItems().forEach(output::accept))
                .build());
    }
}