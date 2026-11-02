package team.heather.cataclysm.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.effect.status.harmful.MortemMobEffect;
import team.heather.cataclysm.registry.item.totem.ParagonTotem;

@Mixin(value = MortemMobEffect.class, remap = false)
public abstract class MortemMobEffectMixin {

    @Inject(method = "tryBlockTotem", at = @At("HEAD"), cancellable = true)
    private static void tryBlockTotem$0(LivingEntity entity, DamageSource source, ItemStack stack,
                                        CallbackInfoReturnable<Boolean> callback) {
        if (stack.getItem() instanceof ParagonTotem) {
            callback.setReturnValue(false);
        }
    }
}
