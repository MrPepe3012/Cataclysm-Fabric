package com.cataclysm.item;

import com.cataclysm.Cataclysm;
import com.cataclysm.item.totem.TotemItem;
import com.cataclysm.item.totem.mortality.MortalityTable;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public final class CataclysmItems {
    public static final Item ARCANE_TOTEM = registerTotem("arcane_totem", Rarity.RARE, MortalityTable.ARCANE);
    public static final Item CALAMITY_TOTEM = registerTotem("calamity_totem", Rarity.RARE, MortalityTable.CALAMITY);
    public static final Item PARAGON_TOTEM = registerTotem("paragon_totem", Rarity.EPIC, MortalityTable.PARAGON);

    private CataclysmItems() {}

    private static Item registerTotem(String path, Rarity rarity, MortalityTable reduction) {
        return register(path, new TotemItem(new Item.Properties().rarity(rarity), reduction));
    }

    private static Item register(String path, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Cataclysm.createResourceLocation(path), item);
    }
}