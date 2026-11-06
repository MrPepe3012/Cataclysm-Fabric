package team.heather.cataclysm.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

/** A group of items that share a rarity and a set of properties applied automatically. */
public enum ItemFamily {

    PARAGON(Rarity.EPIC, true, false),
    LEMEGETON(Rarity.EPIC, false, false),
    TWISTED(Rarity.UNCOMMON, false, true),
    ARCANE(Rarity.RARE, false, true),
    CALAMITY(Rarity.RARE, false, true),
    MIDWAY(Rarity.RARE, false, true),
    MIRAGE(Rarity.EPIC, false, true),
    PALE(Rarity.EPIC, false, true);

    private final Rarity rarity;
    private final boolean glint;
    private final boolean unbreakable;

    ItemFamily(Rarity rarity,
               boolean glint,
               boolean unbreakable) {
        this.rarity = rarity;
        this.glint = glint;
        this.unbreakable = unbreakable;
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
