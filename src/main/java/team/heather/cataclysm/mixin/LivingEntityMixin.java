package team.heather.cataclysm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
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
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.effect.effect.harmful.MortemMobEffect;
import team.heather.cataclysm.item.arcane.ArcaneEffects;
import team.heather.cataclysm.item.armor.ArmorSetEffects;
import team.heather.cataclysm.item.effect.PassiveItemEffects;
import team.heather.cataclysm.item.totem.UseTotemCallback;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float hurtServer$0(float amount,
                               ServerLevel level,
                               DamageSource damageSource) {
        LivingEntity self = (LivingEntity) (Object) this;
        return PassiveItemEffects.modifyDamage(self, damageSource, ArmorSetEffects.modifyDamage(self, damageSource, amount));
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void canBeAffected$0(MobEffectInstance effect,
                                 CallbackInfoReturnable<Boolean> callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (ArmorSetEffects.isImmuneTo(self, effect.getEffect()) || PassiveItemEffects.isImmuneTo(self, effect.getEffect())) {
            callback.setReturnValue(false);
        }
    }

    @WrapOperation(method = "applyItemBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BlocksAttacks;hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;F)V"))
    private void applyItemBlocking$0(BlocksAttacks blocking,
                                     Level level,
                                     ItemStack stack,
                                     LivingEntity defender,
                                     InteractionHand hand,
                                     float blocked,
                                     Operation<Void> original,
                                     @Local(argsOnly = true) DamageSource source) {
        original.call(blocking, level, stack, defender, hand, blocked);
        ArcaneEffects.onBlockedDamage(defender, stack, source, blocked);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection$0(DamageSource damageSource,
                                             CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(this.useTotem$0(damageSource));
    }

    @Unique
    private boolean useTotem$0(DamageSource damageSource) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;

        LivingEntity self = (LivingEntity) (Object) this;
        ItemStack totem = this.takeTotem$0(damageSource);

        if (totem == null) return false;

        DeathProtection protection = UseTotemCallback.EVENT.invoker().resurrect(self, totem, totem.get(DataComponents.DEATH_PROTECTION));
        if (protection == null) return false;

        if (self instanceof ServerPlayer player) {
            player.awardStat(Stats.ITEM_USED.get(totem.getItem()));
            CriteriaTriggers.USED_TOTEM.trigger(player, totem);
            self.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }

        protection.applyEffects(totem, self);
        self.setHealth(1.0F);
        self.level().broadcastEntityEvent(self, (byte) 35);

        return true;
    }

    /**
     * Consumes one totem from the first hand holding one and returns a copy of it, or {@code null} when none
     * is held or Mortem blocks it. Both mods replace the vanilla method at HEAD; asking here keeps Mortem
     * working whichever runs first.
     */
    @Unique
    private ItemStack takeTotem$0(DamageSource damageSource) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (MortemMobEffect.blocksTotem(self, damageSource)) return null;

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = self.getItemInHand(hand);

            if (held.get(DataComponents.DEATH_PROTECTION) == null) continue;

            ItemStack totem = held.copy();
            held.shrink(1);
            return totem;
        }
        return null;
    }
}
