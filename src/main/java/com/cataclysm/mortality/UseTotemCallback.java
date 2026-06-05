package com.cataclysm.mortality;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface UseTotemCallback {
    Event<UseTotemCallback> EVENT = EventFactory.createArrayBacked(UseTotemCallback.class, listeners ->
            player -> {
                for (UseTotemCallback listener : listeners) {
                    if (!listener.resurrect(player)) {
                        return false;
                    }
                }
                return true;
            }
    );

    boolean resurrect(Player player);
}
