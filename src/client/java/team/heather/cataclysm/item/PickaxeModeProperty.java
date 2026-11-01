package team.heather.cataclysm.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;

/** Selects the blue or red artwork from the persistent pickaxe mode. */
public record PickaxeModeProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<PickaxeModeProperty> CODEC = MapCodec.unit(new PickaxeModeProperty());

    public static void register() {
        RangeSelectItemModelProperties.ID_MAPPER.put(Cataclysm.createNamespacedKey("pickaxe_mode"), CODEC);
    }

    @Override
    public float get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        return stack.getOrDefault(CataclysmDataComponents.PICKAXE_MODE, 0);
    }

    @Override
    public MapCodec<PickaxeModeProperty> type() {
        return CODEC;
    }
}
