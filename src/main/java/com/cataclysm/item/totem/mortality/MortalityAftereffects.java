package com.cataclysm.item.totem.mortality;

import com.cataclysm.core.CataclysmComponents;
import com.cataclysm.effect.CataclysmMobEffects;
import com.cataclysm.item.totem.TotemItem;
import com.cataclysm.item.totem.UseTotemCallback;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

public final class MortalityAftereffects {
    private static final PenaltyBuilder PENALTY_BUILDER = new PenaltyBuilder()
            .at(.98F, penalty -> penalty.applyEffect(MobEffects.SLOWNESS, 1))
            .at(.95F, penalty -> penalty.removeEffect(MobEffects.FIRE_RESISTANCE))
            .at(.93F, penalty -> penalty.applyEffect(MobEffects.WEAKNESS, 1))
            .at(.90F, penalty -> penalty.removeEffect(MobEffects.ABSORPTION))
            .at(.86F, penalty -> penalty.applyEffectToNearbyMobs(MobEffects.STRENGTH))
            .at(.83F, penalty -> penalty.applyEffectToNearbyMobs(MobEffects.SPEED))
            .at(.80F, penalty -> penalty.applyEffect(MobEffects.MINING_FATIGUE, 1))
            .at(.75F, penalty -> penalty.applyEffect(MobEffects.BLINDNESS, 0))
            .at(.60F, penalty -> penalty.applyEffect(CataclysmMobEffects.MORTEM, 0))
            .at(.50F, penalty -> penalty.applyEffect(CataclysmMobEffects.DISPER, 0))
            .at(.25F, penalty -> penalty.removeEffect(MobEffects.REGENERATION))
            .at(.10F, penalty -> setMaxHealth(penalty, health -> health - 1))
            .at(.0F,  penalty -> setMaxHealth(penalty, health -> 1));

    private MortalityAftereffects() {}

    public static void initialize() {
        UseTotemCallback.EVENT.register((entity, totemStack, deathProtector) -> {
            if (!(entity instanceof Player player)) {
                return deathProtector;
            }

            MortalityComponent mortalityComponent = CataclysmComponents.MORTALITY.get(player);
            float mortality = mortalityComponent.get();
            float reduction = TotemItem.findMortalityReduction(totemStack.getItem(), 1).orElse(0.0F);

            mortalityComponent.set(mortality - reduction);
            return PENALTY_BUILDER.apply(player, deathProtector, mortality);
        });
    }

    private static void setMaxHealth(Penalty penalty, DoubleUnaryOperator modifier) {
        penalty.execute(player -> {
            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(modifier.applyAsDouble(maxHealth.getBaseValue()));
            }
        });
    }

    private static final class PenaltyBuilder {
        private final Map<Float, Consumer<Penalty>> penalties = new HashMap<>();

        PenaltyBuilder at(float threshold, Consumer<Penalty> action) {
            this.penalties.put(threshold, action);
            return this;
        }

        DeathProtection apply(Player player, DeathProtection component, float mortality) {
            Penalty penalty = new Penalty(player, component);
            this.penalties.forEach((treshold, action) -> {
                if (mortality < treshold) {
                    action.accept(penalty);
                }
            });
            return penalty.build();
        }
    }

    private static final class Penalty {
        private static final int EFFECT_DURATION = 400;
        private static final float NEARBY_ASSUMPTION = 5.0F;

        private final List<MobEffect> removedEffects = new ArrayList<>();
        private final Player player;
        private final DeathProtection deathProtector;

        Penalty(Player player, DeathProtection deathProtector) {
            this.player = player;
            this.deathProtector = deathProtector;
        }

        void execute(Consumer<Player> action) {
            action.accept(this.player);
        }

        void applyEffect(Holder<MobEffect> holder, int amplifier) {
            this.player.addEffect(new MobEffectInstance(holder, EFFECT_DURATION, amplifier));
        }

        void removeEffect(Holder<MobEffect> holder) {
            this.removedEffects.add(holder.value());
        }

        void applyEffectToNearbyMobs(Holder<MobEffect> holder) {
            this.player.level().getEntitiesOfClass(Monster.class, this.player.getBoundingBox().inflate(NEARBY_ASSUMPTION))
                    .forEach(entity -> entity.addEffect(new MobEffectInstance(holder, EFFECT_DURATION, 1)));
        }

        DeathProtection build() {
            return new DeathProtection(this.deathProtector.deathEffects().stream().filter(this::keepEffect).toList());
        }

        private boolean keepEffect(ConsumeEffect effect) {
            return !(effect instanceof ApplyStatusEffectsConsumeEffect applyEffects) ||
                    applyEffects.effects().stream().noneMatch(instance ->
                            this.removedEffects.contains(instance.getEffect().value()));
        }
    }
}