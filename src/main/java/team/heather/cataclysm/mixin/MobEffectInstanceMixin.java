package team.heather.cataclysm.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.effect.callback.MobEffectTickCallback;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {

    @Inject(method = "tickServer", at = @At("HEAD"))
    private void tickServer$0(ServerLevel level,
                              LivingEntity entity,
                              Runnable onUpdate,
                              CallbackInfoReturnable<Boolean> callback) {
        MobEffectTickCallback.EVENT.invoker().onEffectTick(level, entity, (MobEffectInstance) (Object) this);
    }
}
