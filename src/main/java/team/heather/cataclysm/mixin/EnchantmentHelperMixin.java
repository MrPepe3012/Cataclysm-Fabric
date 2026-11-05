package team.heather.cataclysm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import team.heather.cataclysm.item.effect.EquipmentEnchantmentEffects;
import team.heather.cataclysm.item.twisted.TwistedEffects;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getItemEnchantmentLevel", at = @At("RETURN"), cancellable = true)
    private static void getItemEnchantmentLevel$0(Holder<Enchantment> enchantment,
                                                  ItemStack stack,
                                                  CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(TwistedEffects.level(stack, enchantment, callback.getReturnValue()));
    }

    /** Changes only the level used by the effect, preserving the stored enchantment and anvil rules. */
    @ModifyArgs(method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;accept(Lnet/minecraft/core/Holder;I)V"))
    private static void runIterationOnItem$0(Args args,
                                             @Local(argsOnly = true) ItemStack stack) {
        args.set(1, EquipmentEnchantmentEffects.effectiveLevel(stack, args.get(0), args.get(1)));
    }
}
