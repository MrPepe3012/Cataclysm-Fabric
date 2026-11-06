package team.heather.cataclysm.item.feature.totem;

import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.effect.callback.AllowMortemTotemBlockCallback;
import team.heather.cataclysm.effect.callback.MortemTotemBlockedCallback;
import team.heather.cataclysm.item.feature.paragon.ParagonTotemItem;
import team.heather.cataclysm.sound.SoundMashup;
import team.heather.cataclysm.util.CataclysmBroadcast;

/**
 * Lets each {@link ResurrectionProvider} decide whether it works and what it grants. Initialize it before
 * any other {@link UseTotemCallback} listener, so a rejected totem never reaches them.
 */
public final class TotemProcessor {

    private TotemProcessor() {}

    public static void initialize() {
        UseTotemCallback.EVENT.register((entity, stack, protection) -> {
            var totem = ResurrectionProvider.of(stack.getItem()).orElse(null);
            if (totem == null) {
                return protection;
            }

            if (!totem.canResurrect(entity)) {
                announceFailure(entity);
                return null;
            }

            return totem.resurrect(entity, protection);
        });

        AllowMortemTotemBlockCallback.EVENT.register((entity, source, totem) -> !(totem.getItem() instanceof ParagonTotemItem));
        MortemTotemBlockedCallback.EVENT.register((entity, source, totem) -> announceFailure(entity));
    }

    private static void announceFailure(LivingEntity entity) {
        CataclysmBroadcast.broadcast(CataclysmBroadcast.TOTEM_FAIL, entity.getName().getString());
        SoundMashup.MORTEM.playAtEntity(entity);
    }
}
