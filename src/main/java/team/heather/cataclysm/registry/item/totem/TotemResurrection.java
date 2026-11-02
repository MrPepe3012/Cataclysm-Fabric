package team.heather.cataclysm.registry.item.totem;

import team.heather.cataclysm.util.CataclysmBroadcast;
import team.heather.cataclysm.registry.sound.SoundMashup;
import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.effect.event.TotemEvents;

/**
 * Lets each {@link ResurrectingItem} decide whether it works and what it grants. Initialize it before
 * any other {@link UseTotemCallback} listener, so a rejected totem never reaches them.
 */
public final class TotemResurrection {

    private TotemResurrection() {}

    public static void initialize() {
        UseTotemCallback.EVENT.register((entity, stack, protection) -> {
            var totem = ResurrectingItem.of(stack.getItem()).orElse(null);
            if (totem == null) {
                return protection;
            }

            if (!totem.canResurrect(entity)) {
                announceFailure(entity);
                return null;
            }

            return totem.resurrect(entity, protection);
        });

        TotemEvents.MORTEM_BLOCKED.register((entity, source, totem, amplifier) -> announceFailure(entity));
    }

    private static void announceFailure(LivingEntity entity) {
        CataclysmBroadcast.broadcast(CataclysmBroadcast.TOTEM_FAIL, entity.getName().getString());
        SoundMashup.MORTEM.playAtEntity(entity);
    }
}
