package team.heather.cataclysm.builder;

import net.minecraft.ChatFormatting;
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
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
import java.util.function.UnaryOperator;

public final class ItemFactory {

    private static final Map<Item, List<BiConsumer<ItemStack, HolderLookup.Provider>>> FINISHERS = new HashMap<>();
    private static final Map<Item, List<UnaryOperator<ItemStack>>> VARIANTS = new HashMap<>();

    private final String path;
    private final List<BiConsumer<ItemStack, HolderLookup.Provider>> stackFinishers = new ArrayList<>();
    private final List<UnaryOperator<ItemStack>> creativeVariants = new ArrayList<>();
    private final ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.builder();

    private Item.Properties props = new Item.Properties();
    private ItemBase base;
    private Palette palette;
    private List<Component> descriptionArguments = List.of();
    private boolean descriptionGap;
    private boolean hasAttributes;
    private double attackDamage;
    private double attackSpeed;

    private ItemFactory(String path) {
        this.path = Objects.requireNonNull(path);
    }

    public static Item register(String path) {
        return new ItemFactory(path).build();
    }

    public static Item register(String path, UnaryOperator<ItemFactory> configure) {
        return configure.apply(new ItemFactory(path)).build();
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

    public ItemFactory base(ItemBase base) {
        this.base = Objects.requireNonNull(base);
        this.props = base.configure(this.props);
        return this;
    }

    public ItemFactory base(ItemBase.Configuration configuration) {
        base(configuration.base());
        configuration.damage().ifPresent(this::attack);
        configuration.speed().ifPresent(this::speed);
        return this;
    }

    public ItemFactory family(ItemFamily family) {
        Objects.requireNonNull(family);
        this.palette = family.palette();
        this.props = family.apply(this.props);
        return this;
    }

    public ItemFactory props(UnaryOperator<Item.Properties> configure) {
        this.props = Objects.requireNonNull(configure.apply(this.props));
        return this;
    }

    public ItemFactory armor(ArmorMaterial material, ArmorType type) {
        return props(properties -> properties.humanoidArmor(material, type));
    }

    public <T> ItemFactory variants(DataComponentType<T> component, List<T> values) {
        Objects.requireNonNull(component);
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

    public ItemFactory palette(Palette palette) {
        this.palette = Objects.requireNonNull(palette);
        return this;
    }

    public ItemFactory description(boolean gap) {
        this.descriptionGap = gap;
        return this;
    }

    public ItemFactory descriptionArguments(Component... arguments) {
        this.descriptionArguments = List.of(arguments);
        return this;
    }

    public ItemFactory glint() {
        return props(properties -> properties.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    }

    public ItemFactory unbreakable() {
        return props(properties -> properties.component(DataComponents.UNBREAKABLE, Unit.INSTANCE).fireResistant());
    }

    public ItemFactory hide(DataComponentType<?> component) {
        return props(properties -> properties.component(DataComponents.TOOLTIP_DISPLAY,
                TooltipDisplay.DEFAULT.withHidden(component, true)));
    }

    public ItemFactory attack(double damage, double speed) {
        return attack(damage).speed(speed);
    }

    public ItemFactory attack(double damage) {
        this.attackDamage = damage;
        return attribute(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID, damage, EquipmentSlotGroup.MAINHAND);
    }

    public ItemFactory enchant(ResourceKey<Enchantment> enchantment, int level) {
        this.stackFinishers.add((stack, registries) ->
                stack.enchant(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment), level));
        return this;
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

        var item = Registry.register(BuiltInRegistries.ITEM, key, create());
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

    private ItemFactory speed(double speed) {
        this.attackSpeed = speed;
        return attribute(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID, speed, EquipmentSlotGroup.MAINHAND);
    }

    private ItemFactory attribute(Holder<Attribute> attribute, ResourceLocation id, double amount, EquipmentSlotGroup slot) {
        this.attributes.add(attribute, new AttributeModifier(id, amount, Operation.ADD_VALUE), slot);
        this.hasAttributes = true;
        return this;
    }

    private Item create() {
        return this.base == null ? new Item(this.props) : this.base.create(this.props, this.attackDamage, this.attackSpeed);
    }

    private void applyDisplay(ResourceLocation id) {
        var nameStyle = Style.EMPTY.withItalic(false);
        var descriptionStyle = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false);
        if (this.palette != null) {
            nameStyle = plain(this.palette.base());
            descriptionStyle = plain(this.palette.alternate());
        }
        this.props.component(DataComponents.ITEM_NAME,
                Component.empty().append(Component.translatable(id.toLanguageKey("item")).withStyle(nameStyle)));
        var description = Component.translatable(id.toLanguageKey("item", "description"), this.descriptionArguments.toArray())
                .withStyle(descriptionStyle);
        this.props.component(DataComponents.LORE, new ItemLore(this.descriptionGap
                ? List.of(Component.empty(), description) : List.of(description)));
    }

    private static Style plain(int rgb) {
        return Style.EMPTY.withColor(rgb).withItalic(false);
    }
}
