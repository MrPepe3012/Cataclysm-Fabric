package team.heather.cataclysm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.registry.item.twisted.TwistedMining;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import team.heather.cataclysm.registry.item.EquipmentEnchantments;

@Mixin(EnchantmentHelper.class)
public abstract class EquipmentEnchantmentMixin {
    @Inject(method = "getItemEnchantmentLevel", at = @At("RETURN"), cancellable = true)
    private static void cataclysm$intrinsicMiningLevel(Holder<Enchantment> enchantment, ItemStack stack,
                                                       CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(TwistedMining.level(stack, enchantment, cir.getReturnValue()));
    }

    // Change only the level used by the effect, preserving the stored enchantment and anvil rules.
    @ModifyArgs(method = "runIterationOnItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;accept(Lnet/minecraft/core/Holder;I)V"))
    private static void cataclysm$arcaneEnchantmentLevel(Args args, @Local(argsOnly = true) ItemStack stack) {
        args.set(1, EquipmentEnchantments.effectiveLevel(stack, args.get(0), args.get(1)));
    }
}
