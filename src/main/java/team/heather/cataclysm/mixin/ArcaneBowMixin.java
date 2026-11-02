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
import team.heather.cataclysm.registry.item.CataclysmItems;
import team.heather.cataclysm.registry.sound.SoundMashup;

@Mixin(BowItem.class)
public abstract class ArcaneBowMixin {
    @Inject(method = "releaseUsing", at = @At("RETURN"))
    private void cataclysm$arcaneBowSound(ItemStack stack, Level level, LivingEntity shooter,
                                         int remainingTicks, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && level instanceof ServerLevel && stack.is(CataclysmItems.ARCANE_BOW)) {
            SoundMashup.ARCANE_BOW.playAtEntity(shooter);
        }
    }
}
