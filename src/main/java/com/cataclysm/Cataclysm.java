package com.cataclysm;

import com.cataclysm.command.CataclysmCommand;
import com.cataclysm.core.CataclysmRegistries;
import com.cataclysm.item.totem.mortality.MortalityAftereffects;
import com.cataclysm.server.CataclysmServerManager;
import com.cataclysm.server.ServerEventInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Cataclysm implements ModInitializer {
    public static final String MOD_ID = "cataclysm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Cataclysm");

        // High priority
        CataclysmRegistries.bootstrap();

        // Medium priority
        CataclysmServerManager.initialize();
        ServerEventInitializer.initialize();

        // Low priority
        CataclysmCommand.initialize();

        LOGGER.info("Cataclysm initialized!");
    }

    public static ResourceLocation createResourceLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, string);
    }
}