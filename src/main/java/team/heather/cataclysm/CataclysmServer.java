
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

    public void update(int day,
                       boolean ragnarok) {
        var rules = this.level.getGameRules();

        rules.getRule(GameRules.RULE_DOFIRETICK).set(day < 7, this.server);
        rules.getRule(GameRules.RULE_DAYLIGHT).set(day < 14 && !ragnarok, this.server);
        rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(!ragnarok, this.server);

        if (day >= 14 || ragnarok) this.level.setDayTime(18000);
        else this.level.setDayTime(0);
    }

    private void configureWorldBorder(WorldBorder border) {
        border.setCenter(0, 0);
        border.setSize(12_000);
    }

    private void configureGameRules(GameRules rules) {
        rules.getRule(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE).set(10, this.server);
        rules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, this.server);
    }
}
