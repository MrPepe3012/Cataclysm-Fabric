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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.effect.beneficial.CleansingMobEffect;
import team.heather.cataclysm.effect.callback.AllowMobEffectCallback;
import team.heather.cataclysm.effect.callback.MobEffectChangedCallback;
import team.heather.cataclysm.effect.harmful.MortemMobEffect;
import team.heather.cataclysm.effect.harmful.PaleCorrosionMobEffect;
import team.heather.cataclysm.item.behavior.PassiveItemProcessor;
import team.heather.cataclysm.item.feature.arcane.ArcaneProcessor;
import team.heather.cataclysm.item.feature.armor.ArmorSetProcessor;
import team.heather.cataclysm.item.feature.totem.UseTotemCallback;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float hurtServer$0(float amount,
                               ServerLevel level,
                               DamageSource damageSource) {
        LivingEntity self = (LivingEntity) (Object) this;
        return PassiveItemProcessor.modifyDamage(self, damageSource, ArmorSetProcessor.modifyDamage(self, damageSource, amount));
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void canBeAffected$0(MobEffectInstance effect,
                                 CallbackInfoReturnable<Boolean> callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        boolean cleansed = !self.level().isClientSide && CleansingMobEffect.blocks(self, effect);
        if (cleansed || ArmorSetProcessor.isImmuneTo(self, effect.getEffect()) || PassiveItemProcessor.isImmuneTo(self, effect.getEffect())) {
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "onEffectsRemoved", at = @At("HEAD"))
    private void onEffectsRemoved$0(Collection<MobEffectInstance> effects,
                                    CallbackInfo callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        for (MobEffectInstance effect : effects) {
            if (effect.is(CataclysmMobEffects.PALE_CORROSION)) {
                PaleCorrosionMobEffect.restoreHearts(self);
            }
        }
    }

    @Inject(method = "onEffectsRemoved", at = @At("TAIL"))
    private void onEffectsRemoved$1(Collection<MobEffectInstance> effects,
                                    CallbackInfo callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide) return;

        for (MobEffectInstance effect : effects) {
            var change = effect.getDuration() == 0 ? MobEffectChangedCallback.Change.EXPIRED : MobEffectChangedCallback.Change.REMOVED;
            MobEffectChangedCallback.EVENT.invoker().onEffectChanged(self, effect, change, null);
        }
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void tickEffects$0(CallbackInfo callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level() instanceof ServerLevel level) {
            CleansingMobEffect.cleanseEffects(level, self);
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void addEffect$0(MobEffectInstance effect,
                             @Nullable Entity source,
                             CallbackInfoReturnable<Boolean> callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!self.level().isClientSide && !AllowMobEffectCallback.EVENT.invoker().allowEffect(self, effect, source)) {
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void onEffectAdded$0(MobEffectInstance effect,
                                 @Nullable Entity source,
                                 CallbackInfo callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!self.level().isClientSide) {
            MobEffectChangedCallback.EVENT.invoker().onEffectChanged(self, effect, MobEffectChangedCallback.Change.ADDED, source);
        }
    }

    /** Vanilla also calls this every 30 seconds to resync effects that did not change. */
    @Inject(method = "onEffectUpdated", at = @At("TAIL"))
    private void onEffectUpdated$0(MobEffectInstance effect,
                                   boolean changed,
                                   @Nullable Entity source,
                                   CallbackInfo callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (changed && !self.level().isClientSide) {
            MobEffectChangedCallback.EVENT.invoker().onEffectChanged(self, effect, MobEffectChangedCallback.Change.UPDATED, source);
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
        ArcaneProcessor.onBlockedDamage(defender, stack, source, blocked);
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
     * is held or Mortem blocks it.
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
