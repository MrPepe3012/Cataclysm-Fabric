package team.heather.cataclysm.item.feature.lemegeton;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/** Boss encounters can deny activation, opening the catalogue and purchasing without changing item state. */
@FunctionalInterface
public interface AllowLemegetonUseCallback {

    Event<AllowLemegetonUseCallback> EVENT = EventFactory.createArrayBacked(AllowLemegetonUseCallback.class, callbacks -> (player, book) -> {
        for (AllowLemegetonUseCallback callback : callbacks) {
            if (!callback.allowUse(player, book)) {
                return false;
            }
        }

        return true;
    });

    boolean allowUse(ServerPlayer player, ItemStack book);
}
