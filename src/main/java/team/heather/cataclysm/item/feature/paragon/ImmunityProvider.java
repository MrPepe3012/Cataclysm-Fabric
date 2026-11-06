package team.heather.cataclysm.item.feature.paragon;

import net.minecraft.world.effect.MobEffectInstance;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.component.level.phase.Phase;

public interface ImmunityProvider {

    ImmunityTable getImmunityTable();

    default int getImmunityTicks(Phase phase) {
        return this.getImmunityTable().getTicks(phase);
    }

    default MobEffectInstance createImmunity(Phase phase) {
        return new MobEffectInstance(CataclysmMobEffects.IMMORTALITY, this.getImmunityTicks(phase));
    }
}
