package com.cataclysm.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public final class SoundHelper {

    private SoundHelper() {}

    public static void playAtPlayer(Player player, SoundEvent sound, SoundSource source, float pitch) {
        player.level().playSound(null, player.blockPosition(), sound, source, 1.0F, pitch);
    }
}
