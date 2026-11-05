package team.heather.cataclysm.item.paragon;

import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public interface ImmunityProvider {

    ImmunityTable getImmunityTable();

    default int getImmunityTicks(int week) {
        return this.getImmunityTable().getTicks(week);
    }

    default MobEffectInstance createImmunity(int week) {
        return new MobEffectInstance(CataclysmicMobEffects.IMMORTALITY, this.getImmunityTicks(week));
    }
}
