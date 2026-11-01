package team.heather.cataclysm.registry.item.material;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import team.heather.cataclysm.Cataclysm;

import java.util.Map;
import java.util.Objects;

/**
 * Cataclysm armor materials, built fluently. Both sets mirror netherite stats; durability
 * is irrelevant since the items are unbreakable.
 */
public final class CataclysmArmorMaterial {

    public static final ArmorMaterial CALAMITY = of("calamity")
            .durability(37)
            .enchantability(15)
            .toughness(3.0F)
            .knockbackResistance(0.1F)
            .repair(ItemTags.REPAIRS_NETHERITE_ARMOR)
            .sound(SoundEvents.ARMOR_EQUIP_NETHERITE)
            .defense(3, 8, 6, 3)
            .build();

    public static final ArmorMaterial PALE = of("pale")
            .durability(37)
            .enchantability(15)
            .toughness(3.0F)
            .knockbackResistance(0.1F)
            .repair(ItemTags.REPAIRS_NETHERITE_ARMOR)
            .sound(SoundEvents.ARMOR_EQUIP_NETHERITE)
            .defense(3, 8, 6, 3)
            .build();

    public static final ArmorMaterial MIRAGE = of("mirage")
            .durability(25)
            .enchantability(9)
            .toughness(0.0F)
            .knockbackResistance(0.0F)
            .repair(ItemTags.REPAIRS_TURTLE_HELMET)
            .sound(SoundEvents.ARMOR_EQUIP_TURTLE)
            .defense(1, 0, 0, 0)
            .build();

    private final String asset;

    private Map<ArmorType, Integer> defense = Map.of();
    private int durability;
    private int enchantability;
    private float toughness;
    private float knockbackResistance;
    private TagKey<Item> repair;
    private Holder<SoundEvent> sound;

    private CataclysmArmorMaterial(String asset) {
        this.asset = Objects.requireNonNull(asset);
    }

    /** {@code asset} names the equipment model under {@code assets/cataclysm/equipment}. */
    public static CataclysmArmorMaterial of(String asset) {
        return new CataclysmArmorMaterial(asset);
    }

    public CataclysmArmorMaterial durability(int durability) {
        this.durability = durability;
        return this;
    }

    public CataclysmArmorMaterial enchantability(int enchantability) {
        this.enchantability = enchantability;
        return this;
    }

    public CataclysmArmorMaterial repair(TagKey<Item> repair) {
        this.repair = Objects.requireNonNull(repair);
        return this;
    }

    public CataclysmArmorMaterial toughness(float toughness) {
        this.toughness = toughness;
        return this;
    }

    public CataclysmArmorMaterial knockbackResistance(float knockbackResistance) {
        this.knockbackResistance = knockbackResistance;
        return this;
    }

    public CataclysmArmorMaterial sound(Holder<SoundEvent> sound) {
        this.sound = Objects.requireNonNull(sound);
        return this;
    }

    public CataclysmArmorMaterial defense(int helmet, int chestplate, int leggings, int boots) {
        this.defense = Map.of(
                ArmorType.HELMET, helmet,
                ArmorType.CHESTPLATE, chestplate,
                ArmorType.LEGGINGS, leggings,
                ArmorType.BOOTS, boots);
        return this;
    }

    public ArmorMaterial build() {
        return new ArmorMaterial(
                this.durability,
                this.defense,
                this.enchantability,
                Objects.requireNonNull(this.sound),
                this.toughness,
                this.knockbackResistance,
                Objects.requireNonNull(this.repair),
                ResourceKey.create(EquipmentAssets.ROOT_ID, Cataclysm.createNamespacedKey(this.asset)));
    }
}
