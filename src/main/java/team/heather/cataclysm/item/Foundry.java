package team.heather.cataclysm.item;

import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.item.paragon.ImmunityTable;
import team.heather.cataclysm.item.paragon.ParagonItem;
import team.heather.cataclysm.item.totem.TotemItem;
import team.heather.cataclysm.item.totem.mortality.MortalityTable;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.util.Unit;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class Foundry {

    private final String path;

    private ItemFactory factory = Item::new;
    private Item.Properties props = new Item.Properties();

    private Foundry(String path) {
        this.path = Objects.requireNonNull(path);
    }

    public static Item item(String path, Function<Foundry, Item> configure) {
        return configure.apply(new Foundry(path));
    }

    // --- Factory

    public Foundry as(ItemFactory factory) {
        this.factory = Objects.requireNonNull(factory);
        return this;
    }

    public Foundry props(UnaryOperator<Item.Properties> configure) {
        this.props = Objects.requireNonNull(configure.apply(this.props));
        return this;
    }

    // --- Stats

    public Foundry stack(int size) {
        return props(p -> p.stacksTo(size));
    }

    public Foundry durable(int durability) {
        return props(p -> p.durability(durability));
    }

    public Foundry unbreakable() {
        return props(p -> p.component(DataComponents.UNBREAKABLE, Unit.INSTANCE));
    }

    public Foundry fireResistant() {
        return props(Item.Properties::fireResistant);
    }

    public Foundry rarity(Rarity rarity) {
        return props(p -> p.rarity(rarity));
    }

    // --- Kinds

    public Foundry sword(ToolMaterial material, float damage, float speed) {
        return props(p -> p.sword(material, damage, speed));
    }

    public Foundry armor(ArmorMaterial material, ArmorType type) {
        return props(p -> p.humanoidArmor(material, type));
    }

    public Foundry paragon(ImmunityTable immunityTable) {
        return as(p -> new ParagonItem(p, immunityTable));
    }

    public Foundry totem(MortalityTable mortalityTable) {
        return as(p -> new TotemItem(p, mortalityTable))
                .rarity(Rarity.RARE);
    }

    // --- Build

    public Item build() {
        var key = ResourceKey.create(Registries.ITEM, Cataclysm.createNamespacedKey(this.path));
        var item = this.factory.create(this.props.setId(key));

        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    @FunctionalInterface
    public interface ItemFactory {

        Item create(Item.Properties properties);
    }
}
