package team.heather.cataclysm.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.item.feature.twisted.TwistedProcessor;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getItemEnchantmentLevel", at = @At("RETURN"), cancellable = true)
    private static void getItemEnchantmentLevel$0(Holder<Enchantment> enchantment,
                                                  ItemStack stack,
                                                  CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(TwistedProcessor.level(stack, enchantment, callback.getReturnValue()));
    }
}
