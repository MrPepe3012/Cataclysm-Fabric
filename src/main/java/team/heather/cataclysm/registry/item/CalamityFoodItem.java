package team.heather.cataclysm.registry.item;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.effect.status.CataclysmicMobEffects;

public final class CalamityFoodItem extends Item {
    public enum Kind { APPLE, ENCHANTED_APPLE, CARROT }
    private final Kind kind;

    public CalamityFoodItem(Properties properties, Kind kind) {
        super(properties.food(kind == Kind.CARROT ? Foods.GOLDEN_CARROT : Foods.GOLDEN_APPLE, Consumables.DEFAULT_FOOD));
        this.kind = kind;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        ItemStack result = super.finishUsingItem(stack, level, user);
        if (level.isClientSide) return result;
        if (this.kind == Kind.CARROT) {
            if (user instanceof Player player) player.getFoodData().eat(0, 0.4F);
            user.addEffect(new MobEffectInstance(MobEffects.SPEED, 300, 3));
        } else if (this.kind == Kind.APPLE) {
            user.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
            user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3));
            user.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3000, 1));
        } else {
            user.getActiveEffects().stream().filter(effect -> effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL
                            && !effect.getEffect().equals(CataclysmicMobEffects.PALE_CORROSION)
                            && !effect.getEffect().equals(CataclysmicMobEffects.MORTEM))
                    .map(MobEffectInstance::getEffect).toList().forEach(user::removeEffect);
            user.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 2));
            user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 4));
            user.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 1));
            user.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 300, Cataclysm.getWeek() < 5 ? 2 : 0));
        }
        return result;
    }
}
