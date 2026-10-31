package team.heather.cataclysm.core.item.totem;

import team.heather.cataclysm.chat.CataclysmBroadcast;
import team.heather.cataclysm.core.sound.SoundMashup;

/**
 * Lets each {@link ResurrectingItem} decide whether it works and what it grants. Initialize it before
 * any other {@link UseTotemCallback} listener, so a rejected totem never reaches them.
 */
public final class TotemResurrection {

    private TotemResurrection() {}

    public static void initialize() {
        UseTotemCallback.EVENT.register((entity, stack, protection) -> {
            var totem = ResurrectingItem.of(stack.getItem()).orElse(null);
            if (totem == null) return protection;

            if (!totem.canResurrect(entity)) {
                CataclysmBroadcast.broadcast(CataclysmBroadcast.TOTEM_FAIL, entity.getName().getString());
                SoundMashup.MORTEM.playAtEntity(entity);
                return null;
            }

            return totem.resurrect(entity, protection);
        });
    }
}
