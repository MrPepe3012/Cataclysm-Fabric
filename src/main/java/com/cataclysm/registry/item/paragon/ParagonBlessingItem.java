package com.cataclysm.registry.item.paragon;

import com.cataclysm.registry.CataclysmMobEffects;
import com.cataclysm.registry.sound.SoundMashup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ParagonBlessingItem extends Item {
    private static final int IMMUNITY_DURATION = 300;
    private static final int COOLDOWN = 20;

    public ParagonBlessingItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);

            SoundMashup.PARAGON_BLESSING.playAtEntity(player);
            player.addEffect(new MobEffectInstance(CataclysmMobEffects.IMMUNITY, IMMUNITY_DURATION));
            player.getCooldowns().addCooldown(stack, COOLDOWN);
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        return InteractionResult.SUCCESS;
    }
}
