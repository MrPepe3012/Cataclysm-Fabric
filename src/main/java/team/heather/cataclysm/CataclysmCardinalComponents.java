package team.heather.cataclysm;

import team.heather.cataclysm.registry.item.totem.mortality.MortalityComponent;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public final class CataclysmCardinalComponents implements EntityComponentInitializer {

    public static final ComponentKey<MortalityComponent> MORTALITY = register("mortality", MortalityComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MORTALITY, player -> new MortalityComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    private static <C extends Component> ComponentKey<C> register(String path,
                                                                  Class<C> clazz) {
        return ComponentRegistry.getOrCreate(Cataclysm.createNamespacedKey(path), clazz);
    }
}
