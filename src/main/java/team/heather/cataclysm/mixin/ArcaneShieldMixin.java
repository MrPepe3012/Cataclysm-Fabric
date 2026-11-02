package team.heather.cataclysm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.heather.cataclysm.registry.item.arcane.ArcaneTools;

@Mixin(LivingEntity.class)
public abstract class ArcaneShieldMixin {
    @WrapOperation(method = "applyItemBlocking", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/BlocksAttacks;hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;F)V"))
    private void cataclysm$arcaneBlock(BlocksAttacks blocking, Level level, ItemStack stack,
                                      LivingEntity defender, InteractionHand hand, float blocked,
                                      Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
        original.call(blocking, level, stack, defender, hand, blocked);
        ArcaneTools.onBlockedDamage(defender, stack, source, blocked);
    }
}
