package team.heather.cataclysm.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoodData.class)
public interface FoodDataMixin {

    @Accessor("exhaustionLevel")
    void setExhaustionLevel$0(float value);
}
