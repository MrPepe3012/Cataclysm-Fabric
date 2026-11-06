package team.heather.cataclysm.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.sound.SoundMashup;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Inject(method = "releaseUsing", at = @At("RETURN"))
    private void releaseUsing$0(ItemStack stack,
                                Level level,
                                LivingEntity shooter,
                                int remainingTicks,
                                CallbackInfoReturnable<Boolean> callback) {
        if (callback.getReturnValue() && level instanceof ServerLevel && stack.is(CataclysmItems.ARCANE_BOW)) {
            SoundMashup.ARCANE_BOW.playAtEntity(shooter);
        }
    }
}
