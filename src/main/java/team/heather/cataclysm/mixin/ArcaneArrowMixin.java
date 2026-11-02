package team.heather.cataclysm.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.heather.cataclysm.registry.item.arcane.ArcaneTools;
import team.heather.cataclysm.registry.item.WeaponHitEffects;

@Mixin(AbstractArrow.class)
public abstract class ArcaneArrowMixin {
    @Inject(method = "onHitEntity", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void cataclysm$arcaneArrowHit(EntityHitResult hit, CallbackInfo ci) {
        if (hit.getEntity() instanceof LivingEntity target) {
            ArcaneTools.onArrowHit((AbstractArrow) (Object) this, target);
            var arrow = (AbstractArrow) (Object) this;
            if (arrow.getOwner() instanceof LivingEntity attacker) {
                WeaponHitEffects.afterHit(arrow.getWeaponItem(), target, attacker);
            }
        }
    }
}
