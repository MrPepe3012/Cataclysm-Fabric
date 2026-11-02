package team.heather.cataclysm.registry.item.armor;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import team.heather.cataclysm.Cataclysm;

import java.util.Optional;

public final class ArmorSetEffects {

    private static final double EXTRA_HEALTH = 4.0;
    private static final float FIRE_DAMAGE_MULTIPLIER = 0.5F;
    private static final AttributeModifier HEALTH_BONUS = new AttributeModifier(
            Cataclysm.createNamespacedKey("armor_set_health"), EXTRA_HEALTH, Operation.ADD_VALUE);

    private ArmorSetEffects() {}

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server ->
            server.getPlayerList().getPlayers().forEach(ArmorSetEffects::tick));
    }

    public static float modifyDamage(LivingEntity entity,
                                     DamageSource source,
                                     float amount) {
        return source.is(DamageTypeTags.IS_FIRE) && activeSet(entity).isPresent()
            ? amount * FIRE_DAMAGE_MULTIPLIER
            : amount;
    }

    public static boolean isImmuneTo(LivingEntity entity,
                                     Holder<MobEffect> effect) {
        return activeSet(entity).filter(set -> set.isImmuneTo(effect)).isPresent();
    }

    private static Optional<ArmorSet> activeSet(LivingEntity entity) {
        return entity instanceof ServerPlayer player
            ? ArmorSet.wornBy(player)
            : Optional.empty();
    }

    private static void tick(ServerPlayer player) {
        ArmorSet worn = ArmorSet.wornBy(player).orElse(null);

        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null && updateMaxHealth(maxHealth, worn != null)
                && player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }

        if (worn != null) {
            worn.immunities().forEach(player::removeEffect);
        }
    }

    /** Returns whether the maximum changed, so removing armor can also clamp current health. */
    static boolean updateMaxHealth(AttributeInstance maxHealth, boolean fullSet) {
        if (!fullSet) {
            return maxHealth.removeModifier(HEALTH_BONUS.id());
        }
        if (maxHealth.hasModifier(HEALTH_BONUS.id())) {
            return false;
        }
        maxHealth.addTransientModifier(HEALTH_BONUS);
        return true;
    }
}
