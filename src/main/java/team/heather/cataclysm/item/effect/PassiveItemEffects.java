package team.heather.cataclysm.item.effect;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import team.heather.cataclysm.mixin.FoodDataMixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class PassiveItemEffects {

    private static final int HOOK_TRAIL_TICKS = 50;
    private static final int TICKS_PER_SECOND = 20;
    private static final int RELIC_MORTEM_WEEK = 4;
    private static final AttributeModifier RELIC_HEALTH = new AttributeModifier(Cataclysm.createNamespacedKey("relic_health"), 8, AttributeModifier.Operation.ADD_VALUE);
    private static final Map<ServerPlayer, Integer> HOOK_TRAILS = new HashMap<>();

    private PassiveItemEffects() {}

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerList().getPlayers().forEach(PassiveItemEffects::tick));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> HOOK_TRAILS.remove(handler.player));
    }

    public static void startHookTrail(ServerPlayer player) {
        HOOK_TRAILS.put(player, HOOK_TRAIL_TICKS);
    }

    /** Removes every harmful effect from the entity except the ones listed in {@code keep}. */
    public static void cleanse(LivingEntity entity, Set<Holder<MobEffect>> keep) {
        entity.getActiveEffects().stream()
                .map(MobEffectInstance::getEffect)
                .filter(effect -> effect.value().getCategory() == MobEffectCategory.HARMFUL && !keep.contains(effect))
                .toList()
                .forEach(entity::removeEffect);
    }

    public static void cleanse(LivingEntity entity) {
        cleanse(entity, Set.of());
    }

    /** Puts out fire and resets freezing, drowning and hunger exhaustion. */
    public static void restoreVitals(ServerPlayer player) {
        player.clearFire();
        player.setTicksFrozen(0);
        player.setAirSupply(player.getMaxAirSupply());
        ((FoodDataMixin) player.getFoodData()).setExhaustionLevel$0(0);
    }

    public static boolean isImmuneTo(LivingEntity entity, Holder<MobEffect> effect) {
        return entity instanceof ServerPlayer
                && hasMirageHelmet(entity)
                && effect.value().getCategory() == MobEffectCategory.HARMFUL;
    }

    public static float modifyDamage(LivingEntity entity, DamageSource source, float amount) {
        boolean immune = hasMirageHelmet(entity) && (source.is(DamageTypes.DROWN) || source.is(DamageTypes.FREEZE));
        return immune ? 0 : amount;
    }

    private static boolean hasMirageHelmet(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(CataclysmRegistry.ITEMS.MIRAGE_HELMET);
    }

    private static void tick(ServerPlayer player) {
        boolean midway = player.getInventory().contains(stack -> stack.is(CataclysmRegistry.ITEMS.MIDWAY_RELIC));
        tickRelicHealth(player, midway);
        if (hasMirageHelmet(player)) {
            player.setTicksFrozen(0);
            player.setAirSupply(player.getMaxAirSupply());
            cleanse(player);
        }
        if (player.tickCount % TICKS_PER_SECOND == 0) {
            tickPeriodicEffects(player, midway);
        }
        tickHookTrail(player);
    }

    private static void tickRelicHealth(ServerPlayer player, boolean midway) {
        var health = player.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) {
            return;
        }
        boolean relic = Cataclysm.getWeek() >= RELIC_MORTEM_WEEK
                ? midway
                : player.getInventory().contains(stack -> stack.is(CataclysmRegistry.ITEMS.TWISTED_RELIC));
        if (relic && !health.hasModifier(RELIC_HEALTH.id())) {
            health.addTransientModifier(RELIC_HEALTH);
        } else if (!relic && health.removeModifier(RELIC_HEALTH.id()) && player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static void tickPeriodicEffects(ServerPlayer player, boolean midway) {
        if (player.isHolding(CataclysmRegistry.ITEMS.TWISTED_HOE)) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 60, 1));
            player.addEffect(new MobEffectInstance(MobEffects.HASTE, 60, 1));
        }
        if (Cataclysm.getWeek() >= RELIC_MORTEM_WEEK && !midway && !player.isCreative() && !player.isSpectator()) {
            player.addEffect(new MobEffectInstance(CataclysmicMobEffects.MORTEM, 60));
        }
    }

    private static void tickHookTrail(ServerPlayer player) {
        Integer trail = HOOK_TRAILS.get(player);
        if (trail == null) {
            return;
        }
        if (trail <= 0 || player.onGround() || !player.isAlive()) {
            HOOK_TRAILS.remove(player);
            return;
        }
        player.serverLevel().sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0);
        HOOK_TRAILS.put(player, trail - 1);
    }
}
