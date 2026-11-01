package team.heather.cataclysm.item.totem;

import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.item.paragon.ImmunityTable;
import team.heather.cataclysm.item.paragon.ImmunityGrantingItem;
import team.heather.cataclysm.item.paragon.ParagonItem;
import team.heather.cataclysm.item.totem.mortality.MortalityTable;
import team.heather.cataclysm.sound.SoundMashup;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.List;

public class ParagonTotem extends TotemItem implements ImmunityGrantingItem {

    public ParagonTotem(Properties properties) {
        super(ParagonItem.applyProperties(properties), MortalityTable.PARAGON);
    }

    @Override
    public ImmunityTable getImmunityTable() {
        return ImmunityTable.PARAGON_TOTEM;
    }

    @Override
    public boolean canResurrect(LivingEntity entity) {
        return true;
    }

    @Override
    public DeathProtection resurrect(LivingEntity entity,
                                     DeathProtection protection) {
        var immunity = this.createImmunity(Cataclysm.getWeek());
        var glowing = new MobEffectInstance(MobEffects.GLOWING, immunity.getDuration());

        List<ConsumeEffect> effects = new ArrayList<>(protection.deathEffects());
        effects.add(new ApplyStatusEffectsConsumeEffect(List.of(immunity, glowing)));

        SoundMashup.PARAGON_BLESSING.playAtEntity(entity);
        return new DeathProtection(effects);
    }
}
