package team.heather.cataclysm.core.item.paragon;

import team.heather.cataclysm.cataclysmiceffects.effect.CataclysmicMobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public interface ImmunityGrantingItem {

    ImmunityTable getImmunityTable();

    default int getImmunityTicks(int week) {
        return this.getImmunityTable().getTicks(week);
    }

    default MobEffectInstance createImmunity(int week) {
        return new MobEffectInstance(CataclysmicMobEffects.IMMORTALITY, this.getImmunityTicks(week));
    }
}
