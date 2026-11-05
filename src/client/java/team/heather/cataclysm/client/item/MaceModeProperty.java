package team.heather.cataclysm.client.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.component.data.MirageMaceMode;

public record MaceModeProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<MaceModeProperty> CODEC = MapCodec.unit(new MaceModeProperty());

    public static void register() {
        RangeSelectItemModelProperties.ID_MAPPER.put(Cataclysm.createNamespacedKey("mace_mode"), CODEC);
    }

    @Override
    public float get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        return stack.getOrDefault(CataclysmRegistry.DATA_COMPONENTS.MACE_MODE, MirageMaceMode.WIND).ordinal();
    }

    @Override
    public MapCodec<MaceModeProperty> type() {
        return CODEC;
    }
}
