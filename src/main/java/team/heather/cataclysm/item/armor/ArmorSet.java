package team.heather.cataclysm.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;

import java.util.Optional;
import java.util.Set;

public enum ArmorSet {

    CALAMITY(
        CataclysmRegistry.ITEMS.CALAMITY_HELMET,
        CataclysmRegistry.ITEMS.CALAMITY_CHESTPLATE,
        CataclysmRegistry.ITEMS.CALAMITY_LEGGINGS,
        CataclysmRegistry.ITEMS.CALAMITY_BOOTS,
        Set.of()
    ),
    PALE(
        CataclysmRegistry.ITEMS.PALE_HELMET,
        CataclysmRegistry.ITEMS.PALE_CHESTPLATE,
        CataclysmRegistry.ITEMS.PALE_LEGGINGS,
        CataclysmRegistry.ITEMS.PALE_BOOTS,
        Set.of(CataclysmicMobEffects.PALE_CORROSION, CataclysmicMobEffects.MORTEM)
    );

    private static final ArmorSet[] VALUES = values();

    private final Item helmet;
    private final Item chestplate;
    private final Item leggings;
    private final Item boots;
    private final Set<Holder<MobEffect>> immunities;

    ArmorSet(Item helmet,
             Item chestplate,
             Item leggings,
             Item boots,
             Set<Holder<MobEffect>> immunities) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.immunities = immunities;
    }

    public static Optional<ArmorSet> wornBy(LivingEntity entity) {
        for (ArmorSet set : VALUES) {
            if (set.isWornBy(entity)) {
                return Optional.of(set);
            }
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

    public Set<Holder<MobEffect>> immunities() {
        return this.immunities;
    }

}
