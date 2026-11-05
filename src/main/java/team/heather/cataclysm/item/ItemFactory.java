package team.heather.cataclysm.item;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jetbrains.annotations.Nullable;
import team.heather.cataclysm.Cataclysm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class ItemFactory {

    private static final Map<Item, List<BiConsumer<ItemStack, HolderLookup.Provider>>> FINISHERS = new HashMap<>();
    private static final Map<Item, List<UnaryOperator<ItemStack>>> VARIANTS = new HashMap<>();

    // Every tool is unbreakable, so the required durability has no effect
    private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1, 9.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);

    private final String path;
    private final List<BiConsumer<ItemStack, HolderLookup.Provider>> stackFinishers = new ArrayList<>();
    private final List<UnaryOperator<ItemStack>> creativeVariants = new ArrayList<>();
    private final ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.builder();

    private Item.Properties props = new Item.Properties();
    private Function<Item.Properties, Item> constructor = Item::new;
    private @Nullable ItemPalette palette;
    private boolean hasAttributes;

    private ItemFactory(String path) {
        this.path = path;
    }

    public static Item register(String path) {
        return new ItemFactory(path).build();
    }

    public static Item register(String path, ItemFamily family) {
        return new ItemFactory(path).family(family).build();
    }

    public static Item register(String path, UnaryOperator<ItemFactory> configure) {
        return configure.apply(new ItemFactory(path)).build();
    }

    public static Item register(String path, ItemFamily family, UnaryOperator<ItemFactory> configure) {
        return configure.apply(new ItemFactory(path).family(family)).build();
    }

    public static Item register(String path, Function<Item.Properties, Item> constructor, ItemFamily family) {
        return register(path, constructor, family, item -> item);
    }

    public static Item register(String path, Function<Item.Properties, Item> constructor, UnaryOperator<ItemFactory> configure) {
        var factory = new ItemFactory(path);
        factory.constructor = constructor;
        return configure.apply(factory).build();
    }

    public static Item register(String path, Function<Item.Properties, Item> constructor, ItemFamily family, UnaryOperator<ItemFactory> configure) {
        var factory = new ItemFactory(path);
        factory.constructor = constructor;
        return configure.apply(factory.family(family)).build();
    }

    public static ItemStack stackOf(Item item, HolderLookup.Provider registries) {
        var stack = new ItemStack(item);
        FINISHERS.getOrDefault(item, List.of()).forEach(finisher -> finisher.accept(stack, registries));
        return stack;
    }

    public static List<ItemStack> stacksOf(Item item, HolderLookup.Provider registries) {
        var declared = VARIANTS.get(item);
        if (declared == null) {
            return List.of(stackOf(item, registries));
        }
        return declared.stream().map(variant -> variant.apply(stackOf(item, registries))).toList();
    }

    public ItemFactory damage(double damage) {
        return attribute(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID, damage);
    }

    public ItemFactory speed(double speed) {
        return attribute(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID, speed);
    }

    private ItemFactory family(ItemFamily family) {
        this.palette = family.palette();
        this.props = family.apply(this.props);
        return this;
    }

    public ItemFactory props(UnaryOperator<Item.Properties> configure) {
        this.props = configure.apply(this.props);
        return this;
    }

    public ItemFactory armor(ArmorMaterial material, ArmorType type) {
        return props(properties -> properties.humanoidArmor(material, type));
    }

    public ItemFactory sword(double damage, double speed) {
        return props(properties -> properties.sword(TOOL_MATERIAL, toolDamage(damage), (float) speed));
    }

    public ItemFactory pickaxe(double damage, double speed) {
        return props(properties -> properties.pickaxe(TOOL_MATERIAL, toolDamage(damage), (float) speed));
    }

    public ItemFactory axe(double damage, double speed) {
        this.constructor = properties -> new AxeItem(TOOL_MATERIAL, toolDamage(damage), (float) speed, properties);
        return this;
    }

    public ItemFactory shovel(double damage, double speed) {
        this.constructor = properties -> new ShovelItem(TOOL_MATERIAL, toolDamage(damage), (float) speed, properties);
        return this;
    }

    public ItemFactory hoe(double damage, double speed) {
        this.constructor = properties -> new HoeItem(TOOL_MATERIAL, toolDamage(damage), (float) speed, properties);
        return this;
    }

    public <T> ItemFactory variants(DataComponentType<T> component, List<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("A template must declare at least one variant");
        }
        this.creativeVariants.clear();
        for (T value : List.copyOf(values)) {
            this.creativeVariants.add(stack -> {
                stack.set(component, value);
                return stack;
            });
        }
        return this;
    }

    public ItemFactory palette(ItemPalette palette) {
        this.palette = palette;
        return this;
    }

    public ItemFactory glint() {
        return props(properties -> properties.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    }

    public ItemFactory trim(ResourceKey<TrimMaterial> material, ResourceKey<TrimPattern> pattern) {
        this.stackFinishers.add((stack, registries) -> stack.set(DataComponents.TRIM, new ArmorTrim(
                registries.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(material),
                registries.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(pattern))));
        return this;
    }

    public Item build() {
        var key = ResourceKey.create(Registries.ITEM, Cataclysm.createNamespacedKey(this.path));
        applyDisplay(key.location());
        if (this.hasAttributes) {
            this.props.component(DataComponents.ATTRIBUTE_MODIFIERS, this.attributes.build());
        }
        this.props.setId(key);

        var item = Registry.register(BuiltInRegistries.ITEM, key, this.constructor.apply(this.props));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }
        if (!this.stackFinishers.isEmpty()) {
            FINISHERS.put(item, List.copyOf(this.stackFinishers));
        }
        if (!this.creativeVariants.isEmpty()) {
            VARIANTS.put(item, List.copyOf(this.creativeVariants));
        }
        return item;
    }

    private ItemFactory attribute(Holder<Attribute> attribute, ResourceLocation id, double amount) {
        this.attributes.add(attribute, new AttributeModifier(id, amount, Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        this.hasAttributes = true;
        return this;
    }

    private void applyDisplay(ResourceLocation id) {
        var nameStyle = this.palette != null ? plain(this.palette.base()) : Style.EMPTY.withItalic(false);
        this.props.component(DataComponents.ITEM_NAME,
                Component.empty().append(Component.translatable(id.toLanguageKey("item")).withStyle(nameStyle)));
    }

    private static float toolDamage(double damage) {
        return (float) damage - TOOL_MATERIAL.attackDamageBonus();
    }

    private static Style plain(int rgb) {
        return Style.EMPTY.withColor(rgb).withItalic(false);
    }
}
