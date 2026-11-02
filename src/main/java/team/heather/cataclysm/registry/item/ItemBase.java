package team.heather.cataclysm.registry.item;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.block.CataclysmBlocks;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.arcane.ArcaneMaceItem;
import team.heather.cataclysm.registry.item.mirage.MirageMaceItem;
import team.heather.cataclysm.registry.item.mirage.EnderBagItem;
import team.heather.cataclysm.registry.item.calamity.CalamityFoodItem;
import team.heather.cataclysm.registry.item.paragon.BlessingItem;
import team.heather.cataclysm.registry.item.totem.ParagonTotem;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonItem;
import team.heather.cataclysm.registry.item.totem.TotemItem;
import team.heather.cataclysm.registry.item.totem.mortality.MortalityTable;
import team.heather.cataclysm.registry.item.twisted.TwistedPickaxeItem;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/** Explicit behavior and components for equipment; never copies a registered item. */
public enum ItemBase {
    // Tools
    SWORD, PICKAXE, AXE, SHOVEL, HOE,

    TWISTED_PICKAXE,

    // Combat and equipment
    MACE, ARCANE_MACE, MIRAGE_MACE, BOW, TRIDENT, SHIELD, TOTEM, ARCANE_TOTEM, CALAMITY_TOTEM, PARAGON_TOTEM, GLIDER,

    // Food
    GOLDEN_FOOD, ENCHANTED_FOOD, CARROT_FOOD, CALAMITY_APPLE, ENCHANTED_CALAMITY_APPLE, CALAMITY_CARROT,

    // Custom items and blocks
    LEMEGETON, ARCANE_CORE, GOLEM_HEAD, PARAGON_BLESSING, PARAGON_PEARL, MIRAGE_BLESSING, ENDER_BAG;

    private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);

    public Configuration damage(double damage) {
        return new Configuration(this, OptionalDouble.of(damage), OptionalDouble.empty());
    }

    public Configuration speed(double speed) {
        return new Configuration(this, OptionalDouble.empty(), OptionalDouble.of(speed));
    }

    /** Immutable per-item settings; chaining never modifies the shared enum constant. */
    public record Configuration(ItemBase base, OptionalDouble damage, OptionalDouble speed) {
        public Configuration {
            Objects.requireNonNull(base);
            Objects.requireNonNull(damage);
            Objects.requireNonNull(speed);
        }

        public Configuration damage(double damage) {
            return new Configuration(this.base, OptionalDouble.of(damage), this.speed);
        }

        public Configuration speed(double speed) {
            return new Configuration(this.base, this.damage, OptionalDouble.of(speed));
        }
    }

    public Item.Properties configure(Item.Properties properties) {
        return switch (this) {
            case SWORD -> properties.sword(TOOL_MATERIAL, 3, -2.4F).fireResistant();
            case PICKAXE, TWISTED_PICKAXE -> properties.pickaxe(TOOL_MATERIAL, 1, -2.8F).fireResistant();
            case AXE, SHOVEL, HOE -> properties.fireResistant();
            case MACE, ARCANE_MACE, MIRAGE_MACE -> weapon(properties, 500, 15, MaceItem.createToolProperties());
            case BOW -> properties.durability(384).enchantable(1);
            case TRIDENT -> weapon(properties, 250, 1, TridentItem.createToolProperties());
            case SHIELD -> shield(properties);
            case TOTEM, ARCANE_TOTEM, CALAMITY_TOTEM, PARAGON_TOTEM -> properties.stacksTo(1).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING);
            case GLIDER -> glider(properties);
            case GOLDEN_FOOD -> properties.food(Foods.GOLDEN_APPLE, Consumables.GOLDEN_APPLE);
            case ENCHANTED_FOOD -> properties.food(Foods.ENCHANTED_GOLDEN_APPLE, Consumables.ENCHANTED_GOLDEN_APPLE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            case CARROT_FOOD -> properties.food(Foods.GOLDEN_CARROT);
            case LEMEGETON -> properties.stacksTo(1);
            case GOLEM_HEAD -> properties.equippableUnswappable(EquipmentSlot.HEAD);
            case PARAGON_PEARL -> properties.stacksTo(16);
            case PARAGON_BLESSING, MIRAGE_BLESSING, ARCANE_CORE, ENDER_BAG, CALAMITY_APPLE, ENCHANTED_CALAMITY_APPLE, CALAMITY_CARROT -> properties;
        };
    }

    public Item create(Item.Properties properties, double damage, double speed) {
        float toolDamage = (float) damage - TOOL_MATERIAL.attackDamageBonus();
        float toolSpeed = (float) speed;
        return switch (this) {
            case TWISTED_PICKAXE -> new TwistedPickaxeItem(properties.component(CataclysmDataComponents.PICKAXE_MODE, 0));
            case AXE -> new AxeItem(TOOL_MATERIAL, toolDamage, toolSpeed, properties);
            case SHOVEL -> new ShovelItem(TOOL_MATERIAL, toolDamage, toolSpeed, properties);
            case HOE -> new HoeItem(TOOL_MATERIAL, toolDamage, toolSpeed, properties);
            case MACE -> new MaceItem(properties);
            case ARCANE_MACE -> new ArcaneMaceItem(properties);
            case MIRAGE_MACE -> new MirageMaceItem(properties);
            case BOW -> new BowItem(properties);
            case TRIDENT -> new TridentItem(properties);
            case SHIELD -> new ShieldItem(properties);
            case ARCANE_TOTEM -> new TotemItem(properties, MortalityTable.ARCANE);
            case CALAMITY_TOTEM -> new TotemItem(properties, MortalityTable.CALAMITY);
            case PARAGON_TOTEM -> new ParagonTotem(properties);
            case PARAGON_BLESSING -> new BlessingItem(properties, false);
            case MIRAGE_BLESSING -> new BlessingItem(properties, true);
            case PARAGON_PEARL -> new EnderpearlItem(properties);
            case ENDER_BAG -> new EnderBagItem(properties);
            case CALAMITY_APPLE -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.APPLE);
            case ENCHANTED_CALAMITY_APPLE -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.ENCHANTED_APPLE);
            case CALAMITY_CARROT -> new CalamityFoodItem(properties, CalamityFoodItem.Kind.CARROT);
            case LEMEGETON -> new LemegetonItem(properties);
            case ARCANE_CORE -> new BlockItem(CataclysmBlocks.ARCANE_CORE, properties);
            case GOLEM_HEAD -> new StandingAndWallBlockItem(CataclysmBlocks.GOLEM_HEAD, CataclysmBlocks.GOLEM_WALL_HEAD, Direction.DOWN, properties);
            default -> new Item(properties);
        };
    }

    private static Item.Properties weapon(Item.Properties properties, int durability, int enchantability, Tool tool) {
        return properties.durability(durability).enchantable(enchantability)
                .component(DataComponents.TOOL, tool)
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    private static Item.Properties shield(Item.Properties properties) {
        return properties.durability(336).equippableUnswappable(EquipmentSlot.OFFHAND)
                .component(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(0.25F, 1.0F,
                    List.of(new BlocksAttacks.DamageReduction(90, Optional.empty(), 0, 1)),
                    new BlocksAttacks.ItemDamageFunction(3, 1, 1),
                    Optional.of(DamageTypeTags.BYPASSES_SHIELD), Optional.of(SoundEvents.SHIELD_BLOCK),
                    Optional.of(SoundEvents.SHIELD_BREAK)))
                .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK);
    }

    private static Item.Properties glider(Item.Properties properties) {
        return properties.durability(432).component(DataComponents.GLIDER, Unit.INSTANCE)
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST)
                    .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA)
                    .setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, Cataclysm.createNamespacedKey("mirage_elytra")))
                    .setDamageOnHurt(false).build());
    }
}
