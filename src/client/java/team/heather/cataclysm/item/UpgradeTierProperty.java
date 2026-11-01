package team.heather.cataclysm.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;

/** Selects upgrade artwork directly from the authoritative tier component. */
public record UpgradeTierProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<UpgradeTierProperty> CODEC = MapCodec.unit(new UpgradeTierProperty());

    public static void register() {
        RangeSelectItemModelProperties.ID_MAPPER.put(Cataclysm.createNamespacedKey("upgrade_tier"), CODEC);
    }

    @Override
    public float get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        return stack.getOrDefault(CataclysmDataComponents.UPGRADE_TIER, CataclysmDataComponents.MIN_UPGRADE_TIER);
    }

    @Override
    public MapCodec<UpgradeTierProperty> type() {
        return CODEC;
    }
}
