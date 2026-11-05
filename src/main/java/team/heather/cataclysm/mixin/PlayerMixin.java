package team.heather.cataclysm.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.heather.cataclysm.item.effect.PersistentItemCooldowns;
import team.heather.cataclysm.item.effect.WeaponHitEffects;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtEnemy(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private void attack$0(Entity target, CallbackInfo callback) {
        Player attacker = (Player) (Object) this;
        if (target instanceof LivingEntity living) {
            WeaponHitEffects.afterHit(attacker.getMainHandItem(), living, attacker);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData$0(CompoundTag tag, CallbackInfo callback) {
        if ((Object) this instanceof ServerPlayer player) {
            PersistentItemCooldowns.save(player, tag);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData$0(CompoundTag tag, CallbackInfo callback) {
        if ((Object) this instanceof ServerPlayer player) {
            PersistentItemCooldowns.load(player, tag);
        }
    }
}
