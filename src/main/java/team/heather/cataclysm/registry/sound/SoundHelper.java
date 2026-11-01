package team.heather.cataclysm.registry.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class SoundHelper {
    private SoundHelper() {}

    public static void playAtPlayer(Player player, SoundEvent sound, SoundSource source, float volume, float pitch) {
        player.level().playSound(null, player.blockPosition(), sound, source, volume, pitch);
    }

    public static void playAtPlayer(Player player, SoundEvent sound, SoundSource source, float pitch) {
        playAtPlayer(player, sound, source, 1.0F, pitch);
    }

    public static void playAtPlayer(Player player, SoundEvent sound, SoundSource source) {
        playAtPlayer(player, sound, source, 1.0F, 1.0F);
    }

    public static void playAtEntity(Entity entity, SoundEvent sound, SoundSource source, float volume, float pitch) {
        entity.level().playSound(null, entity.blockPosition(), sound, source, volume, pitch);
    }

    public static void playAtEntity(Entity entity, SoundEvent sound, SoundSource source) {
        playAtEntity(entity, sound, source, 1.0F, 1.0F);
    }

    public static void playAtPos(Level level, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        level.playSound(null, pos, sound, source, volume, pitch);
    }

    public static void playAtPos(Level level, BlockPos pos, SoundEvent sound, SoundSource source) {
        playAtPos(level, pos, sound, source, 1.0F, 1.0F);
    }

    /** Plays only for the player themselves, not broadcast to others. */
    public static void playLocal(Player player, SoundEvent sound, SoundSource source, float volume, float pitch) {
        player.level().playSound(player, player.blockPosition(), sound, source, volume, pitch);
    }

    public static void playLocal(Player player, SoundEvent sound, SoundSource source) {
        playLocal(player, sound, source, 1.0F, 1.0F);
    }

    public static float randomPitch(float base, float variance, net.minecraft.util.RandomSource random) {
        return base + (random.nextFloat() * 2 - 1) * variance;
    }
}