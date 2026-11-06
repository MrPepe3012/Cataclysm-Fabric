package team.heather.cataclysm.item.feature.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.item.CataclysmItems;

import java.util.Optional;
import java.util.Set;

public enum ArmorSet {

    CALAMITY(
        CataclysmItems.CALAMITY_HELMET,
        CataclysmItems.CALAMITY_CHESTPLATE,
        CataclysmItems.CALAMITY_LEGGINGS,
        CataclysmItems.CALAMITY_BOOTS,
        Set.of()
    ),

    PALE(
        CataclysmItems.PALE_HELMET,
        CataclysmItems.PALE_CHESTPLATE,
        CataclysmItems.PALE_LEGGINGS,
        CataclysmItems.PALE_BOOTS,
        Set.of(CataclysmMobEffects.PALE_CORROSION, CataclysmMobEffects.MORTEM)
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
