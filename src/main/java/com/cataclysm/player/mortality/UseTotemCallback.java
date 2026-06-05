package com.cataclysm.player.mortality;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import org.jetbrains.annotations.Nullable;

/**
 * Callback for when a living entity is about to be resurrected by a totem.
 *
 * <p>Returning {@code false} cancels the totem use entirely.
 */
public interface UseTotemCallback {
    Event<UseTotemCallback> EVENT = EventFactory.createArrayBacked(UseTotemCallback.class, listeners -> (entity, component) -> {
        for (UseTotemCallback listener : listeners) {
            return listener.resurrect(entity, component);
        }
        return DeathProtection.TOTEM_OF_UNDYING;
    });

    @Nullable DeathProtection resurrect(LivingEntity entity, DeathProtection component);
}