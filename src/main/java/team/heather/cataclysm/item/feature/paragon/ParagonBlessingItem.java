package team.heather.cataclysm.item.feature.paragon;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import team.heather.cataclysm.item.behavior.PassiveItemProcessor;
import team.heather.cataclysm.sound.SoundMashup;
import team.heather.cataclysm.server.CataclysmServer;
import team.heather.cataclysm.component.level.phase.Phase;

public final class ParagonBlessingItem extends Item implements ImmunityProvider {

    private static final int COOLDOWN_TICKS = 20;
    private static final double MIRAGE_RADIUS_SQUARED = 25;

    private final boolean mirage;

    public ParagonBlessingItem(Properties properties,
                               boolean mirage) {
        super(properties);
        this.mirage = mirage;
    }

    @Override
    public ImmunityTable getImmunityTable() {
        return this.mirage
            ? ImmunityTable.MIRAGE_BLESSING
            : ImmunityTable.PARAGON_BLESSING;
    }

    @Override
    public @NotNull InteractionResult use(Level level,
                                          Player player,
                                          InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isSpectator() || player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            if (this.mirage) this.blessNearby(serverPlayer);
            else this.blessSelf(serverPlayer);

            player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
            stack.consume(1, player);
            SoundMashup.PARAGON_BLESSING.playAtEntity(player);
        }
        return InteractionResult.SUCCESS;
    }

    private void blessSelf(ServerPlayer player) {
        int duration = ParagonProcessor.protect(player, this.getImmunityTable());
        var phase = CataclysmServer.getPhase();
        if (phase.isAtLeast(Phase.PHASE_4)) {
            ParagonProcessor.reduceMortality(player, phase.isAtLeast(Phase.PHASE_5) ? 0.01F : 0.0025F);
            player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration, 2));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 1));
        }
    }

    private void blessNearby(ServerPlayer caster) {
        for (ServerPlayer recipient : caster.serverLevel().players()) {
            if (recipient.distanceToSqr(caster) <= MIRAGE_RADIUS_SQUARED) {
                this.applyMirage(recipient);
            }
        }
    }

    private void applyMirage(ServerPlayer player) {
        // Cleanse before granting protection so the old negative effects cannot survive the blessing.
        PassiveItemProcessor.cleanse(player);
        int duration = ParagonProcessor.protect(player, this.getImmunityTable());
        player.addEffect(new MobEffectInstance(MobEffects.SPEED, duration, 2));
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600));
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 40, 10));
        PassiveItemProcessor.restoreVitals(player);
    }
}
