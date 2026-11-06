package team.heather.cataclysm.server;

import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import team.heather.cataclysm.component.CataclysmCardinalComponents;
import team.heather.cataclysm.component.level.phase.Phase;

public final class CataclysmServer {

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

    public static Phase getPhase() {
        return instance == null ? Phase.PHASE_1 : CataclysmCardinalComponents.PHASE.get(instance.level).get();
    }

    public static void setPhase(Phase phase) {
        CataclysmCardinalComponents.PHASE.get(instance.level).set(phase);
        instance.update();
    }

    public static boolean isRagnarok() {
        return instance != null && CataclysmCardinalComponents.RAGNAROK.get(instance.level).isActive();
    }

    public static void setRagnarok(boolean active) {
        CataclysmCardinalComponents.RAGNAROK.get(instance.level).setActive(active);
        instance.update();
    }

    public void onInitialize() {
        this.configureWorldBorder(this.level.getWorldBorder());
        this.configureGameRules(this.level.getGameRules());
        instance = this;
    }

    private void update() {
        var phase = getPhase();
        boolean ragnarok = isRagnarok();
        var rules = this.level.getGameRules();

        rules.getRule(GameRules.RULE_DOFIRETICK).set(!phase.isAtLeast(Phase.PHASE_2), this.server);
        rules.getRule(GameRules.RULE_DAYLIGHT).set(!phase.isAtLeast(Phase.PHASE_3) && !ragnarok, this.server);
        rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(!ragnarok, this.server);

        if (phase.isAtLeast(Phase.PHASE_3) || ragnarok) this.level.setDayTime(18000);
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
