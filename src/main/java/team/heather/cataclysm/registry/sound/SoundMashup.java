package team.heather.cataclysm.registry.sound;

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
            SoundHelper.playAtEntity(entity, context.sound(), source, context.volume(), context.pitch());
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