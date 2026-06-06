package com.cataclysm.item.totem;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import org.jetbrains.annotations.Nullable;

/**
 * Callback for when a living entity is about to be resurrected by a totem.
 *
 * <p>Returning {@code null} cancels the totem use entirely. Returning a
 * {@link DeathProtection} instance (including the original {@code deathProtection})
 * allows resurrection to proceed, optionally with modified effects.
 */
public interface UseTotemCallback {
    Event<UseTotemCallback> EVENT = EventFactory.createArrayBacked(UseTotemCallback.class, listeners -> (entity, totemStack, deathProtection) -> {
        for (UseTotemCallback listener : listeners) {
            deathProtection = listener.resurrect(entity, totemStack, deathProtection);
            if (deathProtection == null) return null;
        }
        return deathProtection;
    });

    /**
     * Called when {@code entity} is about to be resurrected.
     *
     * @param entity    the entity being resurrected
     * @param deathProtection the {@link DeathProtection} deathProtection from the totem being consumed
     * @return a {@link DeathProtection} instance describing the resurrection effects,
     *         or {@code null} to cancel the totem use entirely
     */
    @Nullable DeathProtection resurrect(LivingEntity entity, ItemStack totemStack, DeathProtection deathProtection);
}