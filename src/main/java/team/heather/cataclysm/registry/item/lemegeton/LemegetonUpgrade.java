package team.heather.cataclysm.registry.item.lemegeton;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import team.heather.cataclysm.registry.item.effect.PassiveItemEffects;

import java.util.ArrayList;
import java.util.Locale;

public enum LemegetonUpgrade implements StringRepresentable {
    REGENERATION(11, Items.APPLE, MobEffects.REGENERATION, 0, 1),
    JUMP_BOOST(12, Items.FROG_SPAWN_EGG, MobEffects.JUMP_BOOST, 1),
    SPEED(13, Items.RABBIT_FOOT, MobEffects.SPEED, 1, 3),
    STRENGTH(14, Items.IRON_SWORD, MobEffects.STRENGTH, 0, 1, 2),
    DOLPHIN_GRACE(15, Items.DOLPHIN_SPAWN_EGG, MobEffects.DOLPHINS_GRACE, 2, 4, 6),
    HEALTH_BLESSING(20, Items.GOLDEN_APPLE, MobEffects.HEALTH_BOOST, 2, 4, 6),
    RESISTANCE(21, Items.IRON_CHESTPLATE, MobEffects.RESISTANCE, 0, 1, 2),
    HERO_OF_THE_VILLAGE(22, Items.EMERALD, MobEffects.HERO_OF_THE_VILLAGE, 4),
    HASTE(23, Items.GOLDEN_PICKAXE, MobEffects.HASTE, 0, 1, 2),
    INVISIBILITY(24, Items.GOLDEN_CARROT, MobEffects.INVISIBILITY, 0),
    FIRE_RESISTANCE(30, Items.MAGMA_CREAM, MobEffects.FIRE_RESISTANCE, 0),
    CONDUIT_POWER(31, Items.HEART_OF_THE_SEA, MobEffects.CONDUIT_POWER, 0),
    SLOW_FALLING(32, Items.PHANTOM_MEMBRANE, MobEffects.SLOW_FALLING, 0),
    CLEANSING(40, Items.MILK_BUCKET, null, 0, 1);

    public static final Codec<LemegetonUpgrade> CODEC = StringRepresentable.fromEnum(LemegetonUpgrade::values);
    private final int slot;
    private final Item icon;
    private final Holder<MobEffect> effect;
    private final int[] amplifiers;

    LemegetonUpgrade(int slot, Item icon, Holder<MobEffect> effect, int... amplifiers) {
        this.slot = slot;
        this.icon = icon;
        this.effect = effect;
        this.amplifiers = amplifiers.clone();
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String translationKey() {
        return "upgrade.cataclysm." + this.getSerializedName();
    }

    public int slot() {
        return this.slot;
    }

    public int maxLevel() {
        return this.amplifiers.length;
    }

    public int effectLevel(int purchasedLevel) {
        return this.amplifiers[purchasedLevel - 1] + 1;
    }


    public ItemStack menuIcon(LemegetonState state) {
        var stack = new ItemStack(this.icon);
        boolean complete = state.level(this) == this.maxLevel();
        stack.set(DataComponents.ITEM_NAME, Component.empty().append(Component.translatable(this.translationKey())
                .withStyle(style -> style.withColor(complete ? 0xd78f4b : 0xb9b9b9).withBold(complete).withItalic(false))));
        var lore = new ArrayList<Component>();
        for (int level = 1; level <= this.maxLevel(); level++) {
            boolean unlocked = state.level(this) >= level;
            lore.add(Component.literal(unlocked ? "☑ " : "☐ ")
                    .append(Component.translatable(this.translationKey()).append(" ").append(Component.translatable("enchantment.level." + this.effectLevel(level))))
                    .withStyle(style -> style.withItalic(false).withColor(unlocked ? 0xd7a24b : 0xc4c4c4)
                            .withStrikethrough(unlocked)));
        }
        stack.set(DataComponents.LORE, new ItemLore(lore));
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, complete);
        return stack;
    }

    public void apply(ServerPlayer player, int purchasedLevel, int duration) {
        if (purchasedLevel < 1 || purchasedLevel > this.maxLevel()) {
            return;
        }
        if (this == CLEANSING) {
            this.cleanse(player, purchasedLevel, duration);
            return;
        }
        int amplifier = this.amplifiers[purchasedLevel - 1];
        player.addEffect(new MobEffectInstance(this.effect, duration, amplifier));
        if (this == DOLPHIN_GRACE) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, duration, amplifier));
        } else if (this == HEALTH_BLESSING) {
            player.addEffect(new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, amplifier));
        }
    }

    private void cleanse(ServerPlayer player, int purchasedLevel, int duration) {
        PassiveItemEffects.cleanse(player);
        if (purchasedLevel < 2) {
            return;
        }
        PassiveItemEffects.restoreVitals(player);
        player.invulnerableTime = Math.max(player.invulnerableTime, 20);
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, duration, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, duration, 10, false, false));
    }
}
