package team.heather.cataclysm.core.item.armor;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.effect.status.CataclysmicMobEffects;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Full Pale Armor set: every Calamity Armor effect, extra hearts (see {@link ArmorSetBonus})
 * and immunity to the effects registered through {@link #registerImmunity(Holder)}
 * (Pale Corrosion and Mortem).
 */
public final class PaleArmorEffects {

    private static final Set<Holder<MobEffect>> IMMUNITIES = new LinkedHashSet<>();

    private PaleArmorEffects() {}

    public static void initialize() {
        registerImmunity(CataclysmicMobEffects.PALE_CORROSION);
        registerImmunity(CataclysmicMobEffects.MORTEM);

        ServerTickEvents.END_SERVER_TICK.register(server ->
                server.getPlayerList().getPlayers().forEach(PaleArmorEffects::tick));
    }

    /**
     * Grants immunity to {@code effect} while the full set is worn. Call it once when
     * registering Pale Corrosion and Mortem.
     */
    public static void registerImmunity(Holder<MobEffect> effect) {
        IMMUNITIES.add(Objects.requireNonNull(effect));
    }

    public static boolean isWearingFullSet(LivingEntity entity) {
        return entity instanceof ServerPlayer player && ArmorSetBonus.PALE.isWornBy(player);
    }

    public static boolean isImmuneTo(LivingEntity entity, Holder<MobEffect> effect) {
        return IMMUNITIES.contains(effect) && isWearingFullSet(entity);
    }

    // --- Handlers

    /** Called from {@code LivingEntityMixin} at the head of {@code hurtServer}. */
    public static float modifyDamage(LivingEntity entity,
                                     DamageSource source,
                                     float amount) {
        return isWearingFullSet(entity)
                ? CalamityArmorEffects.reduceDamage(source, amount)
                : amount;
    }

    private static void tick(ServerPlayer player) {
        if (!isWearingFullSet(player)) return;

        // canBeAffected only blocks new applications; purge the ones active before equipping
        for (var effect : IMMUNITIES) {
            if (player.hasEffect(effect)) {
                player.removeEffect(effect);
            }
        }
    }
}
