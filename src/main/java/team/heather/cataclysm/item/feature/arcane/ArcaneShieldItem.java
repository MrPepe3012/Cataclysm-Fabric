package team.heather.cataclysm.item.feature.arcane;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.BlocksAttacks;

import java.util.List;
import java.util.Optional;

public class ArcaneShieldItem extends ShieldItem {

    private static final BlocksAttacks BLOCKS_ATTACKS = new BlocksAttacks(
        0.25F,
        1.0F,
        List.of(new BlocksAttacks.DamageReduction(90, Optional.empty(), 0, 1)),
        new BlocksAttacks.ItemDamageFunction(3, 1, 1),
        Optional.of(DamageTypeTags.BYPASSES_SHIELD),
        Optional.of(SoundEvents.SHIELD_BLOCK),
        Optional.of(SoundEvents.SHIELD_BREAK)
    );

    public ArcaneShieldItem(Properties properties) {
        super(properties
            .stacksTo(1)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .component(DataComponents.BLOCKS_ATTACKS, BLOCKS_ATTACKS));
    }
}
