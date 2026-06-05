package com.cataclysm.server;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.border.WorldBorder;

public final class CataclysmServerManager {
    private static CataclysmServerManager instance;

    private final MinecraftServer server;
    private final ServerLevel level;

    private CataclysmServerManager(MinecraftServer server) {
        this.server = server;
        this.level = server.getLevel(ServerLevel.OVERWORLD);
    }

    public void configurate() {
        this.configurateWorldBorder(this.level.getWorldBorder());
        this.configurateGameRules(this.level.getGameRules());
    }

    private void configurateWorldBorder(WorldBorder border) {
        border.setCenter(0, 0);
        border.setSize(12_000);
    }

    private void configurateGameRules(GameRules rules) {
        rules.getRule(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE).set(10, this.server);
    }


    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            instance = new CataclysmServerManager(server);
            instance.configurate();
        });
    }

    public static CataclysmServerManager getInstance() {
        return instance;
    }
}