package team.heather.cataclysm.registry.item;

import net.minecraft.ChatFormatting;
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
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import team.heather.cataclysm.Cataclysm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Fluent registration with optional explicit equipment behavior and independent item models.
 *
 * <p>Enchantments and trims live in dynamic registries, which do not exist while items are registered.
 * They are applied when a stack is created through {@link #stackOf(Item, HolderLookup.Provider)}.
 */
public final class Foundry {

    private static final Map<Item, List<BiConsumer<ItemStack, HolderLookup.Provider>>> FINISHERS = new HashMap<>();
    private static final Map<Item, List<UnaryOperator<ItemStack>>> VARIANTS = new HashMap<>();

    private final String path;
    private final List<BiConsumer<ItemStack, HolderLookup.Provider>> finishers = new ArrayList<>();
    private final ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.builder();

    private ItemBase base;
    private double attackDamage;
    private double attackSpeed;
    private Item.Properties props = new Item.Properties();
    private boolean hasAttributes;
    private final List<UnaryOperator<ItemStack>> variants = new ArrayList<>();

    private Foundry(String path) {
        this.path = Objects.requireNonNull(path);
    }

    public static Item item(String path, Function<Foundry, Item> configure) {
        return configure.apply(new Foundry(path));
    }

    /** A stack of {@code item} with its enchantments and trims applied. */
    public static ItemStack stackOf(Item item, HolderLookup.Provider registries) {
        var stack = new ItemStack(item);
        FINISHERS.getOrDefault(item, List.of()).forEach(finisher -> finisher.accept(stack, registries));
        return stack;
    }

    /** Expands templates into their declared variants; ordinary items produce one stack. */
    public static List<ItemStack> stacksOf(Item item, HolderLookup.Provider registries) {
        var variants = VARIANTS.get(item);
        if (variants == null) return List.of(stackOf(item, registries));
        return variants.stream().map(variant -> variant.apply(stackOf(item, registries))).toList();
    }

    /** Declares the component values that define this item's creative variants. */
    public <T> Foundry variants(DataComponentType<T> component, List<T> values) {
        Objects.requireNonNull(component);
        if (values.isEmpty()) throw new IllegalArgumentException("A template must declare at least one variant");
        this.variants.clear();
        for (T value : List.copyOf(values)) {
            this.variants.add(stack -> {
                stack.set(component, value);
                return stack;
            });
        }
        return this;
    }

    // --- Base

    public Foundry base(ItemBase base) {
        this.base = Objects.requireNonNull(base);
        this.props = this.base.configure(this.props);
        return this;
    }

    public Foundry props(UnaryOperator<Item.Properties> configure) {
        this.props = Objects.requireNonNull(configure.apply(this.props));
        return this;
    }

    public Foundry armor(ArmorMaterial material, ArmorType type) {
        return this.props(p -> p.humanoidArmor(material, type));
    }

    // --- Display

    private Palette palette;
    private boolean descriptionGap;
    private Object[] descriptionArguments = new Object[0];

    public Foundry palette(Palette palette) {
        this.palette = Objects.requireNonNull(palette);
        return this;
    }

    public Foundry description(boolean gap) {
        this.descriptionGap = gap;
        return this;
    }

    public Foundry descriptionArguments(Component... arguments) {
        this.descriptionArguments = arguments.clone();
        return this;
    }

    public Foundry glint() {
        return this.props(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    }

    public Foundry unbreakable() {
        return this.props(p -> p.component(DataComponents.UNBREAKABLE, Unit.INSTANCE).fireResistant());
    }

    public Foundry hide(DataComponentType<?> component) {
        return this.props(p -> p.component(DataComponents.TOOLTIP_DISPLAY,
                TooltipDisplay.DEFAULT.withHidden(component, true)));
    }

    // --- Attributes

    /** Adds {@code damage} and {@code speed} to the player's base values, replacing the base item's own. */
    public Foundry attack(double damage, double speed) {
        this.attackSpeed = speed;
        return this.attack(damage).attribute(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID, speed, EquipmentSlotGroup.MAINHAND);
    }

    public Foundry attack(double damage) {
        this.attackDamage = damage;
        return this.attribute(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID, damage, EquipmentSlotGroup.MAINHAND);
    }

    public Foundry armorPoints(double points, EquipmentSlotGroup slot) {
        return this.attribute(Attributes.ARMOR, Cataclysm.createNamespacedKey("armor." + slot.getSerializedName()), points, slot);
    }

    private Foundry attribute(net.minecraft.core.Holder<Attribute> attribute,
                              ResourceLocation id,
                              double amount,
                              EquipmentSlotGroup slot) {
        this.attributes.add(attribute, new AttributeModifier(id, amount, Operation.ADD_VALUE), slot);
        this.hasAttributes = true;
        return this;
    }

    // --- Dynamic registries

    public Foundry enchant(ResourceKey<Enchantment> enchantment, int level) {
        this.finishers.add((stack, registries) ->
                stack.enchant(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment), level));
        return this;
    }

    public Foundry trim(ResourceKey<TrimMaterial> material, ResourceKey<TrimPattern> pattern) {
        this.finishers.add((stack, registries) -> stack.set(DataComponents.TRIM, new ArmorTrim(
                registries.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(material),
                registries.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(pattern))));
        return this;
    }

    // --- Build

    public Item build() {
        var id = Cataclysm.createNamespacedKey(this.path);
        var nameStyle = Style.EMPTY.withItalic(false);
        var descriptionStyle = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false);
        if (this.palette != null) {
            nameStyle = plain(this.palette.base());
            descriptionStyle = plain(this.palette.alternate());
        }
        this.props.component(DataComponents.ITEM_NAME,
                Component.empty().append(Component.translatable(id.toLanguageKey("item")).withStyle(nameStyle)));
        var description = Component.translatable(id.toLanguageKey("item", "description"), this.descriptionArguments)
                .withStyle(descriptionStyle);
        this.props.component(DataComponents.LORE, new ItemLore(this.descriptionGap
                ? List.of(Component.empty(), description) : List.of(description)));
        if (this.hasAttributes) {
            this.props.component(DataComponents.ATTRIBUTE_MODIFIERS, this.attributes.build());
        }

        var key = ResourceKey.create(Registries.ITEM, Cataclysm.createNamespacedKey(this.path));
        this.props.setId(key);
        var item = Registry.register(BuiltInRegistries.ITEM, key, this.base == null
                ? new Item(this.props) : this.base.create(this.props, this.attackDamage, this.attackSpeed));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        if (!this.finishers.isEmpty()) {
            FINISHERS.put(item, List.copyOf(this.finishers));
        }
        if (!this.variants.isEmpty()) {
            VARIANTS.put(item, List.copyOf(this.variants));
        }
        return item;
    }

    // --- Internals

    private static Style plain(int rgb) {
        return Style.EMPTY.withColor(rgb).withItalic(false);
    }


}
