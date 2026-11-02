package team.heather.cataclysm.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.heather.cataclysm.registry.item.effect.WeaponHitEffects;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;hurtEnemy(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private void attack$0(Entity target, CallbackInfo callback) {
        Player attacker = (Player) (Object) this;
        if (target instanceof LivingEntity living) {
            WeaponHitEffects.afterHit(attacker.getMainHandItem(), living, attacker);
        }
    }
}
