package team.heather.cataclysm.component.mortality;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import team.heather.cataclysm.component.CataclysmCardinalComponents;
import team.heather.cataclysm.item.feature.totem.ResurrectionProvider;
import team.heather.cataclysm.item.feature.totem.UseTotemCallback;
import team.heather.cataclysm.server.CataclysmServer;
import team.heather.cataclysm.util.CataclysmBroadcast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

public final class MortalityProcessor {

    private static final PenaltyScale PENALTIES = new PenaltyScale()
            .below(.98F, context -> context.addEffect(MobEffects.SLOWNESS, 1))
            .below(.95F, context -> context.removeEffect(MobEffects.FIRE_RESISTANCE))
            .below(.93F, context -> context.addEffect(MobEffects.WEAKNESS, 1))
            .below(.90F, context -> context.removeEffect(MobEffects.ABSORPTION))
            .below(.86F, context -> context.affectNearbyMonsters(MobEffects.STRENGTH))
            .below(.83F, context -> context.affectNearbyMonsters(MobEffects.SPEED))
            .below(.80F, context -> context.addEffect(MobEffects.MINING_FATIGUE, 1))
            .below(.75F, context -> context.addEffect(MobEffects.BLINDNESS, 0))
            .below(.25F, context -> context.removeEffect(MobEffects.REGENERATION))
            .below(.10F, context -> context.adjustMaxHealth(health -> health - 1))
            .below(.00F, context -> context.adjustMaxHealth(health -> 1));

    private MortalityProcessor() {}

    public static void initialize() {
        UseTotemCallback.EVENT.register((entity, stack, protection) -> {
            if (!(entity instanceof Player player)) {
                return protection;
            }

            var component = CataclysmCardinalComponents.MORTALITY.get(player);
            var mortality = component.get();
            var reduction = ResurrectionProvider.of(stack.getItem())
                    .map(totem -> totem.getMortalityReduction(CataclysmServer.getPhase()))
                    .orElse(0.0F);

            CataclysmBroadcast.broadcast(
                    CataclysmBroadcast.TOTEM_USE,
                    player.getName().getString(),
                    mortality * 100
            );

            component.set(mortality - reduction);
            return PENALTIES.apply(player, protection, mortality);
        });
    }

    private static final class PenaltyScale {

        private final Map<Float, Consumer<PenaltyContext>> thresholds = new TreeMap<>(Comparator.reverseOrder());

        PenaltyScale below(float threshold,
                           Consumer<PenaltyContext> action) {
            this.thresholds.put(threshold, action);
            return this;
        }

        DeathProtection apply(Player player,
                              DeathProtection protection,
                              float mortality) {
            var context = new PenaltyContext(protection, player);

            this.thresholds.forEach((threshold, action) -> {
                if (mortality < threshold) {
                    action.accept(context);
                }
            });

            return context.resolve();
        }
    }

    private static final class PenaltyContext {

        private static final int EFFECT_DURATION = 400;
        private static final double MONSTER_RADIUS = 5.0;

        private final List<MobEffectInstance> addedEffects = new ArrayList<>();
        private final List<MobEffect> removedEffects = new ArrayList<>();

        private final DeathProtection protection;
        private final Player player;

        PenaltyContext(DeathProtection protection,
                       Player player) {
            this.protection = protection;
            this.player = player;
        }

        void addEffect(Holder<MobEffect> effect,
                       int amplifier) {
            this.addedEffects.add(new MobEffectInstance(effect, EFFECT_DURATION, amplifier));
        }

        void removeEffect(Holder<MobEffect> effect) {
            this.removedEffects.add(effect.value());
        }

        void affectNearbyMonsters(Holder<MobEffect> effect) {
            var box = this.player.getBoundingBox().inflate(MONSTER_RADIUS);
            var effectInstance = new MobEffectInstance(effect, EFFECT_DURATION, 1);
            this.player.level().getEntitiesOfClass(Monster.class, box).forEach(monster -> monster.addEffect(effectInstance));
        }

        void adjustMaxHealth(DoubleUnaryOperator modifier) {
            var attribute = this.player.getAttribute(Attributes.MAX_HEALTH);
            if (attribute == null) {
                return;
            }

            var health = modifier.applyAsDouble(attribute.getBaseValue());
            this.player.setHealth((float) health);
            attribute.setBaseValue(health);
        }

        DeathProtection resolve() {
            List<ConsumeEffect> effects = new ArrayList<>(
                    this.protection.deathEffects().stream()
                            .filter(this::keepEffect)
                            .toList()
            );

            if (!this.addedEffects.isEmpty()) {
                effects.add(new ApplyStatusEffectsConsumeEffect(this.addedEffects));
            }

            return new DeathProtection(effects);
        }

        private boolean keepEffect(ConsumeEffect effect) {
            return !(effect instanceof ApplyStatusEffectsConsumeEffect applied)
                    || applied.effects().stream().noneMatch(instance ->
                    this.removedEffects.contains(instance.getEffect().value()));
        }
    }
}
