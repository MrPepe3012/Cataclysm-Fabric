package team.heather.cataclysm.client.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.Cataclysm;

/** Matches an anvil name without depending on the component's formatting or JSON representation. */
public record CustomNameProperty(String name) implements ConditionalItemModelProperty {
    public static final MapCodec<CustomNameProperty> CODEC = Codec.STRING.fieldOf("name")
            .xmap(CustomNameProperty::new, CustomNameProperty::name);

    public static void register() {
        ConditionalItemModelProperties.ID_MAPPER.put(Cataclysm.createNamespacedKey("custom_name"), CODEC);
    }

    @Override
    public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext context) {
        var customName = stack.get(DataComponents.CUSTOM_NAME);
        return customName != null && this.name.equals(customName.getString());
    }

    @Override
    public MapCodec<CustomNameProperty> type() {
        return CODEC;
    }
}
