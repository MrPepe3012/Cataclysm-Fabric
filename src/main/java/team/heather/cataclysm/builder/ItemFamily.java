package team.heather.cataclysm.builder;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

/** A group of items that share a palette and a set of properties applied automatically. */
public enum ItemFamily {
    PARAGON(Palette.PARAGON, Rarity.EPIC, true, false),
    LEMEGETON(Palette.LEMEGETON, Rarity.COMMON, false, false),
    TWISTED(Palette.TWISTED, Rarity.COMMON, false, true),
    ARCANE(Palette.ARCANE, Rarity.COMMON, false, true),
    CALAMITY(Palette.CALAMITY, Rarity.COMMON, false, true),
    MIDWAY(Palette.MIDWAY, Rarity.COMMON, false, true),
    MIRAGE(Palette.MIRAGE, Rarity.COMMON, false, true),
    PALE(Palette.PALE, Rarity.COMMON, false, true);

    private final Palette palette;
    private final Rarity rarity;
    private final boolean glint;
    private final boolean unbreakable;

    ItemFamily(Palette palette, Rarity rarity, boolean glint, boolean unbreakable) {
        this.palette = palette;
        this.rarity = rarity;
        this.glint = glint;
        this.unbreakable = unbreakable;
    }

    public Palette palette() {
        return palette;
    }

    public Item.Properties apply(Item.Properties properties) {
        properties.rarity(rarity);
        if (this.unbreakable) {
            properties.component(DataComponents.UNBREAKABLE, Unit.INSTANCE).fireResistant();
        }
        return glint ? properties.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true) : properties;
    }
}
