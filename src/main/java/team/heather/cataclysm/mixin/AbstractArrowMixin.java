package team.heather.cataclysm.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.heather.cataclysm.item.arcane.ArcaneEffects;
import team.heather.cataclysm.item.effect.WeaponHitEffects;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void onHitEntity$0(EntityHitResult hit,
                               CallbackInfo callback) {
        if (!(hit.getEntity() instanceof LivingEntity target)) return;

        AbstractArrow self = (AbstractArrow) (Object) this;

        ArcaneEffects.onArrowHit(self, target);

        if (self.getOwner() instanceof LivingEntity attacker) {
            WeaponHitEffects.afterHit(self.getWeaponItem(), target, attacker);
        }
    }
}
