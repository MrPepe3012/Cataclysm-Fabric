package team.heather.cataclysm.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public final class SoundMashup {

    public static final SoundMashup PARAGON_BLESSING = new SoundMashup(
            context(SoundEvents.BEACON_ACTIVATE, 0.65F),
            context(SoundEvents.IRON_GOLEM_DEATH, 0.85F),
            context(SoundEvents.TOTEM_USE, 1.35F));

    public static final SoundMashup MORTEM = new SoundMashup(
            context(SoundEvents.TRIDENT_THUNDER.value(), 1.9F),
            context(SoundEvents.TRIDENT_THUNDER.value(), 1.75F));

    public static final SoundMashup PARAGON_DAMAGE = new SoundMashup(
            context(SoundEvents.IRON_GOLEM_REPAIR, 0.85F));

    public static final SoundMashup ARCANE_MACE = new SoundMashup(SoundSource.PLAYERS,
            new SoundContext(SoundEvents.MACE_SMASH_AIR, 0.8F, 0.8F),
            new SoundContext(SoundEvents.AMETHYST_BLOCK_BREAK, 0.55F, 0.8F));

    public static final SoundMashup ARCANE_BOW = new SoundMashup(SoundSource.PLAYERS,
            new SoundContext(SoundEvents.AMETHYST_BLOCK_BREAK, 0.65F, 1.2F));

    public static final SoundMashup ARCANE_SHIELD = new SoundMashup(SoundSource.PLAYERS,
            new SoundContext(SoundEvents.GUARDIAN_DEATH, 0.55F, 1.95F));

    public static final SoundMashup ARCANE_TRIDENT = new SoundMashup(SoundSource.PLAYERS,
            new SoundContext(SoundEvents.TRIDENT_RIPTIDE_2.value(), 0.55F, 0.8F),
            new SoundContext(SoundEvents.TRIDENT_RIPTIDE_3.value(), 0.55F, 1.15F));

    public static final SoundMashup MIRAGE_HOOK_MODE = new SoundMashup(SoundSource.PLAYERS,
            new SoundContext(SoundEvents.END_PORTAL_FRAME_FILL, 0.8F, 0.6F),
            new SoundContext(SoundEvents.MACE_SMASH_GROUND_HEAVY, 0.5F, 0.6F));
    public static final SoundMashup MIRAGE_NORMAL_MODE = new SoundMashup(SoundSource.PLAYERS,
            new SoundContext(SoundEvents.END_PORTAL_FRAME_FILL, 0.8F, 1.55F),
            new SoundContext(SoundEvents.MACE_SMASH_GROUND_HEAVY, 0.5F, 0.6F));
    public static final SoundMashup MIRAGE_HOOK = new SoundMashup(SoundSource.PLAYERS,
            new SoundContext(SoundEvents.FISHING_BOBBER_THROW, 0.85F, 1.15F),
            new SoundContext(SoundEvents.TRIDENT_RIPTIDE_1.value(), 0.85F, 1.15F),
            new SoundContext(SoundEvents.MACE_SMASH_AIR, 0.65F, 1.15F));

    private final SoundSource source;
    private final SoundContext[] contexts;

    private SoundMashup(SoundContext... contexts) {
        this(SoundSource.MASTER, contexts);
    }

    private SoundMashup(SoundSource source,
                        SoundContext... contexts) {
        this.source = source;
        this.contexts = contexts;
    }

    public void playAtEntity(Entity entity) {
        for (SoundContext context : this.contexts) {
            try (var level = entity.level()) {
                level.playSound(null, entity.blockPosition(), context.sound(), this.source, context.volume(), context.pitch());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public record SoundContext(SoundEvent sound,
                               float volume,
                               float pitch) {}

    private static SoundContext context(SoundEvent sound,
                                        float pitch) {
        return new SoundContext(sound, 1.0F, pitch);
    }
}