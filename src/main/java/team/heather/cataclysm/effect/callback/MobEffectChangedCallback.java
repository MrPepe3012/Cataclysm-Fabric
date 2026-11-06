package team.heather.cataclysm.effect.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/** Called on the server after a mob effect is added to, refreshed on or removed from an entity. */
@FunctionalInterface
public interface MobEffectChangedCallback {

    Event<MobEffectChangedCallback> EVENT = EventFactory.createArrayBacked(MobEffectChangedCallback.class, callbacks -> (entity, instance, change, source) -> {
        for (MobEffectChangedCallback callback : callbacks) {
            callback.onEffectChanged(entity, instance, change, source);
        }
    });

    /**
     * Creates a callback that only listens to the given effect.
     *
     * @param effect the effect to listen to
     * @param callback the callback to run for that effect
     * @return the filtered callback
     */
    static MobEffectChangedCallback only(Holder<MobEffect> effect, MobEffectChangedCallback callback) {
        return (entity, instance, change, source) -> {
            if (instance.is(effect)) {
                callback.onEffectChanged(entity, instance, change, source);
            }
        };
    }

    /**
     * @param entity the entity whose effects changed
     * @param instance the effect that changed
     * @param change what happened to the effect
     * @param source the entity that caused the change, if any
     */
    void onEffectChanged(LivingEntity entity, MobEffectInstance instance, Change change, @Nullable Entity source);

    enum Change {
        ADDED,
        UPDATED,
        REMOVED,
        EXPIRED
    }
}
