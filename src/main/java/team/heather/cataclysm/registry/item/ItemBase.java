package team.heather.cataclysm.registry.item;

import net.minecraft.core.component.DataComponents;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.resources.ResourceKey;
import team.heather.cataclysm.Cataclysm;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.BlockItem;
import team.heather.cataclysm.registry.block.CataclysmBlocks;

/** Explicit behavior and components for equipment; never copies a registered item. */
public enum ItemBase {
    ARCANE_CORE, GOLEM_HEAD, LEMEGETON, SWORD, PICKAXE, TWISTED_PICKAXE, AXE, SHOVEL, HOE, MACE, BOW, SHIELD, TRIDENT, TOTEM,
    GLIDER, GOLDEN_FOOD, ENCHANTED_FOOD, CARROT_FOOD;

    private static final ToolMaterial MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);

    public Item.Properties configure(Item.Properties properties) {
        return switch (this) {
            case ARCANE_CORE -> properties;
            case GOLEM_HEAD -> properties.equippableUnswappable(EquipmentSlot.HEAD);
            case LEMEGETON -> properties.stacksTo(1);
            case SWORD -> properties.sword(MATERIAL, 3, -2.4F).fireResistant();
            case PICKAXE, TWISTED_PICKAXE -> properties.pickaxe(MATERIAL, 1, -2.8F).fireResistant();
            case AXE, SHOVEL, HOE -> properties.fireResistant();
            case MACE -> properties.durability(500).enchantable(15)
                    .component(DataComponents.TOOL, MaceItem.createToolProperties())
                    .component(DataComponents.WEAPON, new Weapon(1));
            case BOW -> properties.durability(384).enchantable(1);
            case SHIELD -> properties.durability(336).equippableUnswappable(EquipmentSlot.OFFHAND)
                    .component(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(0.25F, 1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90, Optional.empty(), 0, 1)),
                            new BlocksAttacks.ItemDamageFunction(3, 1, 1),
                            Optional.of(DamageTypeTags.BYPASSES_SHIELD), Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)))
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK);
            case TRIDENT -> properties.durability(250).enchantable(1)
                    .component(DataComponents.TOOL, TridentItem.createToolProperties())
                    .component(DataComponents.WEAPON, new Weapon(1));
            case TOTEM -> properties.stacksTo(1).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING);
            case GLIDER -> properties.durability(432).component(DataComponents.GLIDER, Unit.INSTANCE)
                    .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST)
                            .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA)
                            .setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, Cataclysm.createNamespacedKey("mirage_elytra")))
                            .setDamageOnHurt(false).build());
            case GOLDEN_FOOD -> properties.food(Foods.GOLDEN_APPLE, Consumables.GOLDEN_APPLE);
            case ENCHANTED_FOOD -> properties.food(Foods.ENCHANTED_GOLDEN_APPLE, Consumables.ENCHANTED_GOLDEN_APPLE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            case CARROT_FOOD -> properties.food(Foods.GOLDEN_CARROT);
        };
    }

    public Item create(Item.Properties properties, double damage, double speed) {
        return switch (this) {
            case ARCANE_CORE -> new BlockItem(CataclysmBlocks.ARCANE_CORE, properties);
            case GOLEM_HEAD -> new StandingAndWallBlockItem(CataclysmBlocks.GOLEM_HEAD,
                    CataclysmBlocks.GOLEM_WALL_HEAD, Direction.DOWN, properties);
            case LEMEGETON -> new LemegetonItem(properties);
            case AXE -> new AxeItem(MATERIAL, (float) damage - MATERIAL.attackDamageBonus(), (float) speed, properties);
            case SHOVEL -> new ShovelItem(MATERIAL, (float) damage - MATERIAL.attackDamageBonus(), (float) speed, properties);
            case HOE -> new HoeItem(MATERIAL, (float) damage - MATERIAL.attackDamageBonus(), (float) speed, properties);
            case TWISTED_PICKAXE -> new TwistedPickaxeItem(properties.component(CataclysmDataComponents.PICKAXE_MODE, 0));
            case MACE -> new MaceItem(properties);
            case BOW -> new BowItem(properties);
            case SHIELD -> new ShieldItem(properties);
            case TRIDENT -> new TridentItem(properties);
            default -> new Item(properties);
        };
    }
}
