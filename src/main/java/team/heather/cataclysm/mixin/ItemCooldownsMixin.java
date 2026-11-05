package team.heather.cataclysm.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemCooldowns.class)
public interface ItemCooldownsMixin {

    @Accessor("cooldowns")
    Map<ResourceLocation, ItemCooldowns.CooldownInstance> getCooldowns$0();

    @Accessor("tickCount")
    int getTickCount$0();
}
