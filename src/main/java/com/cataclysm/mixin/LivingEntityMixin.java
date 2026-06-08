package com.cataclysm.mixin;

import com.cataclysm.registry.item.totem.UseTotemCallback;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection$0(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            cir.setReturnValue(false);
            return;
        }

        ItemStack totemStack = null;
        DeathProtection component = null;

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack handStack = self.getItemInHand(hand);
            component = handStack.get(DataComponents.DEATH_PROTECTION);
            if (component != null) {
                totemStack = handStack.copy();
                handStack.shrink(1);
                break;
            }
        }

        if (totemStack == null) {
            cir.setReturnValue(false);
            return;
        }

        component = UseTotemCallback.EVENT.invoker().resurrect(self, totemStack, component);

        if (component == null) {
            cir.setReturnValue(false);
            return;
        }

        if (self instanceof ServerPlayer player) {
            player.awardStat(Stats.ITEM_USED.get(totemStack.getItem()));
            CriteriaTriggers.USED_TOTEM.trigger(player, totemStack);
            self.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }

        component.applyEffects(totemStack, self);
        self.setHealth(1.0F);
        self.level().broadcastEntityEvent(self, (byte) 35);
        cir.setReturnValue(true);
    }
}