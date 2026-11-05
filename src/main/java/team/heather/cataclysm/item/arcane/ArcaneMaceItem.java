package team.heather.cataclysm.item.arcane;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.sound.SoundMashup;

public class ArcaneMaceItem extends MaceItem {
    public ArcaneMaceItem(Properties properties) {
        super(properties.stacksTo(1).enchantable(15)
                .component(DataComponents.TOOL, createToolProperties())
                .component(DataComponents.WEAPON, new Weapon(1)));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        if (level instanceof ServerLevel serverLevel) {
            var charge = new WindCharge(player, serverLevel, player.getX(), player.getEyeY(), player.getZ());
            var direction = player.getLookAngle();
            charge.shoot(direction.x, direction.y, direction.z, 2.0F, 0.0F);
            serverLevel.addFreshEntity(charge);
            player.getCooldowns().addCooldown(stack, windChargeCooldown());
            player.awardStat(Stats.ITEM_USED.get(this));
            SoundMashup.ARCANE_MACE.playAtEntity(player);
        }
        return InteractionResult.SUCCESS;
    }

    protected int windChargeCooldown() {
        return 30;
    }
}
