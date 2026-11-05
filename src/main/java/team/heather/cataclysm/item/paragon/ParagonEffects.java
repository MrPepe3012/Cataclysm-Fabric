package team.heather.cataclysm.item.paragon;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.component.CataclysmCardinalComponents;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import team.heather.cataclysm.sound.SoundMashup;

public final class ParagonEffects {

    private ParagonEffects() {}

    public static int protect(LivingEntity entity,
                              ImmunityTable table) {
        var current = entity.getEffect(CataclysmicMobEffects.IMMORTALITY);
        int duration = table.getTicks(Cataclysm.getWeek());

        if (current != null) duration += current.getDuration();

        entity.addEffect(new MobEffectInstance(CataclysmicMobEffects.IMMORTALITY, duration));
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

        int week = Cataclysm.getWeek();
        if (week >= 5) reduceMortality(player, week >= 6 ? 0.1F : 0.005F);
        if (week >= 6) {
            player.addEffect(new MobEffectInstance(CataclysmicMobEffects.PALE_CORROSION, 2400));
            player.addEffect(new MobEffectInstance(CataclysmicMobEffects.MORTEM, 2400));
        }

        SoundMashup.PARAGON_BLESSING.playAtEntity(player);
    }
}
