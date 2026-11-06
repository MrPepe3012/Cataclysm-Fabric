package team.heather.cataclysm.component;

import net.minecraft.world.level.Level;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.component.mortality.MortalityComponent;
import team.heather.cataclysm.component.level.phase.PhaseComponent;
import team.heather.cataclysm.component.level.ragnarok.RagnarokComponent;

public final class CataclysmCardinalComponents implements EntityComponentInitializer, WorldComponentInitializer {

    public static final ComponentKey<MortalityComponent> MORTALITY = register("mortality", MortalityComponent.class);

    public static final ComponentKey<RagnarokComponent> RAGNAROK = register("ragnarok", RagnarokComponent.class);
    public static final ComponentKey<PhaseComponent> PHASE = register("phase", PhaseComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MORTALITY, MortalityComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.registerFor(Level.OVERWORLD, PHASE, PhaseComponent::new);
        registry.registerFor(Level.OVERWORLD, RAGNAROK, RagnarokComponent::new);
    }

    private static <C extends Component> ComponentKey<C> register(String path, Class<C> type) {
        return ComponentRegistry.getOrCreate(Cataclysm.resourceLocation(path), type);
    }
}
