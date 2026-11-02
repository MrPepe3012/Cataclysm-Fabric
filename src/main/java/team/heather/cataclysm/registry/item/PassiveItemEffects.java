package team.heather.cataclysm.registry.item;

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
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.effect.status.CataclysmicMobEffects;

import java.util.HashMap;
import java.util.Map;

public final class PassiveItemEffects {
    private static final AttributeModifier RELIC_HEALTH = new AttributeModifier(
            Cataclysm.createNamespacedKey("relic_health"), 8, AttributeModifier.Operation.ADD_VALUE);
    private static final Map<ServerPlayer, Integer> HOOK_TRAILS = new HashMap<>();

    private PassiveItemEffects() {}

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerList().getPlayers().forEach(PassiveItemEffects::tick));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> HOOK_TRAILS.remove(handler.player));
    }

    public static void startHookTrail(ServerPlayer player) {
        HOOK_TRAILS.put(player, 50);
    }

    public static boolean hasMirageHelmet(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(CataclysmItems.MIRAGE_HELMET);
    }

    public static boolean isImmuneTo(LivingEntity entity, Holder<MobEffect> effect) {
        return entity instanceof ServerPlayer && hasMirageHelmet(entity)
                && effect.value().getCategory() == MobEffectCategory.HARMFUL;
    }

    public static float modifyDamage(LivingEntity entity, DamageSource source, float amount) {
        return hasMirageHelmet(entity) && (source.is(DamageTypes.DROWN) || source.is(DamageTypes.FREEZE)) ? 0 : amount;
    }

    private static void tick(ServerPlayer player) {
        var inventory = player.getInventory();
        boolean midway = inventory.contains(stack -> stack.is(CataclysmItems.MIDWAY_RELIC));
        boolean relic = Cataclysm.getWeek() >= 4 ? midway
                : inventory.contains(stack -> stack.is(CataclysmItems.TWISTED_RELIC));
        var health = player.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            if (relic && !health.hasModifier(RELIC_HEALTH.id())) health.addTransientModifier(RELIC_HEALTH);
            if (!relic && health.removeModifier(RELIC_HEALTH.id()) && player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
        if (hasMirageHelmet(player)) {
            player.setTicksFrozen(0);
            player.setAirSupply(player.getMaxAirSupply());
            player.getActiveEffects().stream().filter(effect -> effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
                    .map(MobEffectInstance::getEffect).toList().forEach(player::removeEffect);
        }
        if (player.tickCount % 20 == 0) {
            if (player.isHolding(CataclysmItems.TWISTED_HOE)) {
                player.addEffect(new MobEffectInstance(MobEffects.SPEED, 60, 1));
                player.addEffect(new MobEffectInstance(MobEffects.HASTE, 60, 1));
            }
            if (Cataclysm.getWeek() >= 4 && !midway && !player.isCreative() && !player.isSpectator()) {
                player.addEffect(new MobEffectInstance(CataclysmicMobEffects.MORTEM, 60));
            }
        }
        Integer trail = HOOK_TRAILS.get(player);
        if (trail != null) {
            if (trail <= 0 || player.onGround() || !player.isAlive()) HOOK_TRAILS.remove(player);
            else {
                player.serverLevel().sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 2, 0.3, 0, 0.3, 0);
                HOOK_TRAILS.put(player, trail - 1);
            }
        }
    }
}
