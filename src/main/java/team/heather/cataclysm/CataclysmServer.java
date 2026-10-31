package team.heather.cataclysm;

import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;

final class CataclysmServer {

    @Getter private static CataclysmServer instance;

    @Getter private final MinecraftServer server;
    @Getter private final ServerLevel level;

    private CataclysmServer(MinecraftServer server) {
        this.server = server;
        this.level = server.getLevel(Level.OVERWORLD);
        this.onInitialize();
    }

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(CataclysmServer::new);
    }

    public void onInitialize() {
        this.configureWorldBorder(this.level.getWorldBorder());
        this.configureGameRules(this.level.getGameRules());
        instance = this;
    }

    private void configureWorldBorder(WorldBorder border) {
        border.setCenter(0, 0);
        border.setSize(12_000);
    }

    private void configureGameRules(GameRules rules) {
        this.setGameRule(rules, GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE, 10);
    }

    private void setGameRule(GameRules rules, GameRules.Key<GameRules.IntegerValue> key, int value) {
        rules.getRule(key).set(value, this.server);
    }

    private void setGameRule(GameRules rules, GameRules.Key<GameRules.BooleanValue> key, boolean value) {
        rules.getRule(key).set(value, this.server);
    }
}
