package com.cataclysm;

import com.cataclysm.core.CataclysmRegistries;
import com.cataclysm.server.CataclysmServerManager;
import com.cataclysm.server.ServerEventInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Cataclysm implements ModInitializer {
    public static final String MOD_ID = "cataclysm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Cataclysm");

        CataclysmRegistries.bootstrap();

        CataclysmServerManager.initialize();
        ServerEventInitializer.initialize();

        LOGGER.info("Cataclysm initialized!");
    }

    public static void setServer(MinecraftServer server) {
        Cataclysm.server = server;
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static ResourceLocation resourceLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, string);
    }
}