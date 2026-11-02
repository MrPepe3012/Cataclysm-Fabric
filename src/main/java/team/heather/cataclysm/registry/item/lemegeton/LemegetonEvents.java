package team.heather.cataclysm.registry.item.lemegeton;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/** Boss encounters can deny activation, opening the catalogue and purchasing without changing item state. */
public final class LemegetonEvents {
    public static final Event<AllowUse> ALLOW_USE = EventFactory.createArrayBacked(AllowUse.class,
            listeners -> (player, book) -> {
                for (var listener : listeners) {
                    if (!listener.allow(player, book)) {
                        return false;
                    }
                }
                return true;
            });

    private LemegetonEvents() {}

    @FunctionalInterface
    public interface AllowUse {
        boolean allow(ServerPlayer player, ItemStack book);
    }
}
