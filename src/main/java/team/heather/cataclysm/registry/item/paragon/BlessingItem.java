package team.heather.cataclysm.registry.item.paragon;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.sound.SoundMashup;
import team.heather.cataclysm.mixin.FoodDataAccessor;

public final class BlessingItem extends Item implements ImmunityGrantingItem {
    private final boolean mirage;

    public BlessingItem(Properties properties, boolean mirage) {
        super(properties);
        this.mirage = mirage;
    }

    @Override
    public ImmunityTable getImmunityTable() {
        return this.mirage ? ImmunityTable.MIRAGE_BLESSING : ImmunityTable.PARAGON_BLESSING;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (player.isSpectator() || player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;
        if (player instanceof ServerPlayer serverPlayer) {
            if (this.mirage) {
                for (var recipient : serverPlayer.serverLevel().players()) {
                    if (recipient.distanceToSqr(player) <= 25) applyMirage(recipient);
                }
            } else {
                int duration = ParagonEffects.protect(player, getImmunityTable());
                if (Cataclysm.getWeek() >= 4) {
                    ParagonEffects.reduceMortality(serverPlayer, Cataclysm.getWeek() >= 5 ? 0.01F : 0.0025F);
                    player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 1));
                }
            }
            player.getCooldowns().addCooldown(stack, 20);
            stack.consume(1, player);
            SoundMashup.PARAGON_BLESSING.playAtEntity(player);
        }
        return InteractionResult.SUCCESS;
    }

    private void applyMirage(ServerPlayer player) {
        // Cleanse before granting protection so the old negative effects cannot survive the blessing.
        player.getActiveEffects().stream().filter(effect -> effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
                .map(MobEffectInstance::getEffect).toList().forEach(player::removeEffect);
        int duration = ParagonEffects.protect(player, getImmunityTable());
        player.addEffect(new MobEffectInstance(MobEffects.SPEED, duration, 2));
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600));
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 40, 10));
        player.clearFire();
        player.setTicksFrozen(0);
        player.setAirSupply(player.getMaxAirSupply());
        ((FoodDataAccessor) player.getFoodData()).cataclysm$setExhaustion(0);
    }
}
