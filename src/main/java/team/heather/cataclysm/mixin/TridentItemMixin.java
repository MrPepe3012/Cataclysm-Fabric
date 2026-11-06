package team.heather.cataclysm.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.sound.SoundMashup;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {

    @Inject(method = "releaseUsing", at = @At("RETURN"))
    private void releaseUsing$0(ItemStack stack,
                                Level level,
                                LivingEntity user,
                                int remainingTicks,
                                CallbackInfoReturnable<Boolean> callback) {
        boolean riptide = EnchantmentHelper.getTridentSpinAttackStrength(stack, user) > 0.0F;
        if (callback.getReturnValue() && level instanceof ServerLevel && stack.is(CataclysmItems.ARCANE_TRIDENT) && riptide) {
            SoundMashup.ARCANE_TRIDENT.playAtEntity(user);
        }
    }
}
