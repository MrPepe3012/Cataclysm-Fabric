package team.heather.cataclysm.core.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.core.item.CataclysmItems;
import team.heather.cataclysm.effect.status.CataclysmicMobEffects;

import java.util.Optional;
import java.util.Set;

public enum ArmorSet {

    CALAMITY(
        "calamity_armor",
        CataclysmItems.CALAMITY_HELMET,
        CataclysmItems.CALAMITY_CHESTPLATE,
        CataclysmItems.CALAMITY_LEGGINGS,
        CataclysmItems.CALAMITY_BOOTS,
        Set.of()
    ),
    PALE(
        "pale_armor",
        CataclysmItems.PALE_HELMET,
        CataclysmItems.PALE_CHESTPLATE,
        CataclysmItems.PALE_LEGGINGS,
        CataclysmItems.PALE_BOOTS,
        Set.of(CataclysmicMobEffects.PALE_CORROSION, CataclysmicMobEffects.MORTEM)
    );

    private static final ArmorSet[] VALUES = values();

    private final ResourceLocation id;
    private final Item helmet;
    private final Item chestplate;
    private final Item leggings;
    private final Item boots;
    private final Set<Holder<MobEffect>> immunities;

    ArmorSet(String path,
             Item helmet,
             Item chestplate,
             Item leggings,
             Item boots,
             Set<Holder<MobEffect>> immunities) {
        this.id = Cataclysm.createNamespacedKey(path);
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.immunities = immunities;
    }

    public static Optional<ArmorSet> wornBy(LivingEntity entity) {
        for (ArmorSet set : VALUES) {
            if (set.isWornBy(entity)) return Optional.of(set);
        }
        return Optional.empty();
    }

    public boolean isWornBy(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.CHEST).is(this.chestplate)
            && entity.getItemBySlot(EquipmentSlot.LEGS).is(this.leggings)
            && entity.getItemBySlot(EquipmentSlot.HEAD).is(this.helmet)
            && entity.getItemBySlot(EquipmentSlot.FEET).is(this.boots);
    }

    public boolean isImmuneTo(Holder<MobEffect> effect) {
        return this.immunities.contains(effect);
    }

    public ResourceLocation id() {
        return this.id;
    }

    public Set<Holder<MobEffect>> immunities() {
        return this.immunities;
    }

    public static ArmorSet[] all() {
        return VALUES;
    }
}
