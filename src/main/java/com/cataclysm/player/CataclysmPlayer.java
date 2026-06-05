package com.cataclysm.player;

import com.cataclysm.core.CataclysmComponents;
import com.cataclysm.player.mortality.MortalityComponent;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class CataclysmPlayer {
    private static final Map<Player, CataclysmPlayer> INSTANCES = new HashMap<>();

    private final Player player;

    private CataclysmPlayer(Player player) {
        this.player = player;
    }

    public MortalityComponent getMortalityComponent() {
        return CataclysmComponents.MORTALITY.get(this.player);
    }

    public static CataclysmPlayer of(Player player) {
        return INSTANCES.computeIfAbsent(player, CataclysmPlayer::new);
    }
}