package team.heather.cataclysm.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

/** A group of items that share a palette and a set of properties applied automatically. */
public enum ItemFamily {

    PARAGON(ItemPalette.PARAGON, Rarity.EPIC, true, false),
    LEMEGETON(ItemPalette.LEMEGETON, Rarity.COMMON, false, false),
    TWISTED(ItemPalette.TWISTED, Rarity.COMMON, false, true),
    ARCANE(ItemPalette.ARCANE, Rarity.COMMON, false, true),
    CALAMITY(ItemPalette.CALAMITY, Rarity.COMMON, false, true),
    MIDWAY(ItemPalette.MIDWAY, Rarity.COMMON, false, true),
    MIRAGE(ItemPalette.MIRAGE, Rarity.COMMON, false, true),
    PALE(ItemPalette.PALE, Rarity.COMMON, false, true);

    private final ItemPalette palette;
    private final Rarity rarity;
    private final boolean glint;
    private final boolean unbreakable;

    ItemFamily(ItemPalette palette,
               Rarity rarity,
               boolean glint,
               boolean unbreakable) {
        this.palette = palette;
        this.rarity = rarity;
        this.glint = glint;
        this.unbreakable = unbreakable;
    }

    public ItemPalette palette() {
        return palette;
    }

    public Item.Properties apply(Item.Properties properties) {
        properties.rarity(rarity);

        if (this.unbreakable) {
            properties.component(DataComponents.UNBREAKABLE, Unit.INSTANCE).fireResistant();
        }

        return this.glint
                ? properties.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                : properties;
    }
}
