package team.heather.cataclysm.item.mirage;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import team.heather.cataclysm.Cataclysm;

public final class MirageElytraItem extends Item {

    private static final Equippable EQUIPPABLE = Equippable.builder(EquipmentSlot.CHEST)
            .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA)
            .setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, Cataclysm.createNamespacedKey("mirage_elytra")))
            .setDamageOnHurt(false)
            .build();

    public MirageElytraItem(Properties properties) {
        super(properties.stacksTo(1).component(DataComponents.GLIDER, Unit.INSTANCE)
                .component(DataComponents.EQUIPPABLE, EQUIPPABLE));
    }
}
