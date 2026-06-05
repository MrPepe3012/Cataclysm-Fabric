package com.cataclysm.core;

import com.cataclysm.Cataclysm;
import com.cataclysm.player.mortality.MortalityComponent;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public final class CataclysmComponents implements EntityComponentInitializer {
    public static final ComponentKey<MortalityComponent> MORTALITY = register("mortality", MortalityComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MORTALITY, player -> new MortalityComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    private static <C extends Component> ComponentKey<C> register(String string, Class<C> component) {
        return ComponentRegistry.getOrCreate(Cataclysm.createResourceLocation(string), component);
    }
}
