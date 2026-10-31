package team.heather.cataclysm.mixin;

import team.heather.cataclysm.core.item.armor.CalamityArmorEffects;
import team.heather.cataclysm.core.item.armor.PaleArmorEffects;
import team.heather.cataclysm.core.item.totem.UseTotemCallback;
import team.heather.cataclysm.effect.event.TotemEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float hurtServer$0(float amount, ServerLevel level, DamageSource damageSource) {
        LivingEntity self = (LivingEntity) (Object) this;
        amount = CalamityArmorEffects.modifyDamage(self, damageSource, amount);
        return PaleArmorEffects.modifyDamage(self, damageSource, amount);
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void canBeAffected$0(MobEffectInstance effect,
                                 CallbackInfoReturnable<Boolean> callback) {
        if (PaleArmorEffects.isImmuneTo((LivingEntity) (Object) this, effect.getEffect())) {
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection$0(DamageSource damageSource,
                                             CallbackInfoReturnable<Boolean> callback) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            callback.setReturnValue(false);
            return;
        }

        ItemStack totemStack = null;
        DeathProtection component = null;

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack handStack = self.getItemInHand(hand);
            component = handStack.get(DataComponents.DEATH_PROTECTION);
            if (component != null) {
                // Both mods replace this method at HEAD; asking here keeps Mortem working whichever runs first.
                if (!TotemEvents.ALLOW_ACTIVATION.invoker().allowActivation(self, damageSource, handStack)) {
                    callback.setReturnValue(false);
                    return;
                }

                totemStack = handStack.copy();
                handStack.shrink(1);
                break;
            }
        }

        if (totemStack == null) {
            callback.setReturnValue(false);
            return;
        }

        component = UseTotemCallback.EVENT.invoker().resurrect(self, totemStack, component);

        if (component == null) {
            callback.setReturnValue(false);
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
        callback.setReturnValue(true);
    }
}