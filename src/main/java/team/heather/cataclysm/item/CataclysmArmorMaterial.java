package team.heather.cataclysm.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import team.heather.cataclysm.Cataclysm;

import java.util.Map;

public final class CataclysmArmorMaterial {

    public static final ArmorMaterial CALAMITY = of("calamity")
        .enchantability(15)
        .toughness(3.0F)
        .knockbackResistance(0.1F)
        .sound(SoundEvents.ARMOR_EQUIP_NETHERITE)
        .defense(3, 8, 6, 3)
        .build();

    public static final ArmorMaterial PALE = of("pale")
        .enchantability(15)
        .toughness(3.0F)
        .knockbackResistance(0.1F)
        .sound(SoundEvents.ARMOR_EQUIP_NETHERITE)
        .defense(3, 8, 6, 3)
        .build();

    public static final ArmorMaterial MIRAGE = of("mirage")
        .enchantability(9)
        .toughness(0.0F)
        .knockbackResistance(0.0F)
        .sound(SoundEvents.ARMOR_EQUIP_TURTLE)
        .defense(1, 0, 0, 0)
        .build();

    private final String asset;

    private Map<ArmorType, Integer> defense = Map.of();
    private int enchantability;
    private float toughness;
    private float knockbackResistance;
    private Holder<SoundEvent> sound;

    private CataclysmArmorMaterial(String asset) {
        this.asset = asset;
    }

    /** {@code asset} names the equipment model under {@code assets/cataclysm/equipment}. */
    public static CataclysmArmorMaterial of(String asset) {
        return new CataclysmArmorMaterial(asset);
    }

    public CataclysmArmorMaterial enchantability(int enchantability) {
        this.enchantability = enchantability;
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
        this.sound = sound;
        return this;
    }

    public CataclysmArmorMaterial defense(int helmet,
                                          int chestplate,
                                          int leggings,
                                          int boots) {
        this.defense = Map.of(
            ArmorType.HELMET, helmet,
            ArmorType.CHESTPLATE, chestplate,
            ArmorType.LEGGINGS, leggings,
            ArmorType.BOOTS, boots);
        return this;
    }

    public ArmorMaterial build() {
        // Every piece is unbreakable, so ArmorMaterial's required durability and repair tag have no effect
        return new ArmorMaterial(
            1,
            this.defense,
            this.enchantability,
            this.sound,
            this.toughness,
            this.knockbackResistance,
            ItemTags.REPAIRS_NETHERITE_ARMOR,
            ResourceKey.create(EquipmentAssets.ROOT_ID, Cataclysm.resourceLocation(this.asset)));
    }
}
