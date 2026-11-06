package team.heather.cataclysm.item.feature.paragon;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.component.CataclysmCardinalComponents;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.sound.SoundMashup;
import team.heather.cataclysm.server.CataclysmServer;
import team.heather.cataclysm.component.level.phase.Phase;

public final class ParagonProcessor {

    private ParagonProcessor() {}

    public static int protect(LivingEntity entity,
                              ImmunityTable table) {
        var current = entity.getEffect(CataclysmMobEffects.IMMORTALITY);
        int duration = table.getTicks(CataclysmServer.getPhase());

        if (current != null) duration += current.getDuration();

        entity.addEffect(new MobEffectInstance(CataclysmMobEffects.IMMORTALITY, duration));
        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration));

        return duration;
    }

    public static void reduceMortality(ServerPlayer player,
                                       float reduction) {
        var mortality = CataclysmCardinalComponents.MORTALITY.get(player);
        mortality.set(mortality.get() - reduction);
    }

    public static void onPearlTeleport(ServerPlayer player) {
        protect(player, ImmunityTable.PARAGON_PEARL);

        var phase = CataclysmServer.getPhase();
        if (phase.isAtLeast(Phase.PHASE_5)) reduceMortality(player, phase.isAtLeast(Phase.PANTHEON) ? 0.1F : 0.005F);
        if (phase.isAtLeast(Phase.PANTHEON)) {
            player.addEffect(new MobEffectInstance(CataclysmMobEffects.PALE_CORROSION, 2400));
            player.addEffect(new MobEffectInstance(CataclysmMobEffects.MORTEM, 2400));
        }

        SoundMashup.PARAGON_BLESSING.playAtEntity(player);
    }
}
