package team.heather.cataclysm.client.effect;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.effect.harmful.PaleCorrosionMobEffect;

import java.util.UUID;

/**
 * Draws Immortality and Pale Corrosion on the health bar.
 *
 * <p>Immortality replaces every heart with an immunity heart, and makes the
 * hearts a cancelled hit would have taken blink. Pale Corrosion draws the hearts
 * it disabled; its half sprites cover the right half of a heart, as hearts are
 * disabled from the end of the bar and the left half may still be alive.</p>
 */
public final class MobEffectHeartRenderer {

    private static final ResourceLocation IMMUNITY_FULL = Cataclysm.resourceLocation("hud/heart/immunity_full");
    private static final ResourceLocation IMMUNITY_FULL_BLINKING = Cataclysm.resourceLocation("hud/heart/immunity_full_blinking");
    private static final ResourceLocation IMMUNITY_HALF = Cataclysm.resourceLocation("hud/heart/immunity_half");
    private static final ResourceLocation IMMUNITY_HALF_BLINKING = Cataclysm.resourceLocation("hud/heart/immunity_half_blinking");
    private static final ResourceLocation PALE_FULL = Cataclysm.resourceLocation("hud/heart/pale_full");
    private static final ResourceLocation PALE_FULL_BLINKING = Cataclysm.resourceLocation("hud/heart/pale_full_blinking");
    private static final ResourceLocation PALE_HALF = Cataclysm.resourceLocation("hud/heart/pale_half");
    private static final ResourceLocation PALE_HALF_BLINKING = Cataclysm.resourceLocation("hud/heart/pale_half_blinking");

    private static final String VANILLA_HEART_PREFIX = "hud/heart/";

    private static final int BLINK_DURATION = 20;
    private static final int BLINK_INTERVAL = 3;

    private static final int HEART_SIZE = 9;
    private static final int HEART_SPACING = 8;
    private static final int HEARTS_PER_ROW = 10;
    private static final int HEALTH_PER_HEART = 2;
    private static final int REGENERATION_LIFT = 2;

    private static final int SHAKE_HEALTH = 4;
    private static final long SHAKE_SEED = 312871L;

    private static final RandomSource SHAKE_RANDOM = RandomSource.create();

    private static UUID suppressedPlayer;
    private static float suppressedDamage;
    private static int suppressedEndTick;

    private static float disabledHealth;
    private static int paleEndTick;

    private MobEffectHeartRenderer() {}

    /**
     * Makes the hearts a cancelled hit would have taken blink for a moment.
     *
     * @param player the player owning the health bar
     * @param damage the damage Immortality cancelled
     */
    public static void showSuppressedDamage(Player player, float damage) {
        suppressedPlayer = player.getUUID();
        suppressedDamage = damage;
        suppressedEndTick = player.tickCount + BLINK_DURATION;
    }

    /**
     * Swaps a vanilla heart for its immunity version while the player has Immortality.
     *
     * @param player the player owning the health bar
     * @param sprite the sprite vanilla is about to draw
     * @return the sprite to draw
     */
    public static ResourceLocation immunitySprite(Player player, ResourceLocation sprite) {
        String path = sprite.getPath();

        if (!player.hasEffect(CataclysmMobEffects.IMMORTALITY) || !ResourceLocation.DEFAULT_NAMESPACE.equals(sprite.getNamespace())
                || !path.startsWith(VANILLA_HEART_PREFIX) || path.contains("container")) {
            return sprite;
        }

        return immunitySprite(path.contains("half"), path.endsWith("_blinking"));
    }

    private static ResourceLocation immunitySprite(boolean half, boolean blinking) {
        if (half) {
            return blinking ? IMMUNITY_HALF_BLINKING : IMMUNITY_HALF;
        }

        return blinking ? IMMUNITY_FULL_BLINKING : IMMUNITY_FULL;
    }

    /**
     * Makes the hearts a cancelled hit would have taken blink, absorption first.
     *
     * @param graphics the graphics to render with
     * @param player the player owning the health bar
     * @param bar the layout of the health bar
     * @param maxHealth the health the bar has room for
     * @param health the current health
     * @param absorption the current absorption
     * @param guiTick the tick count of the GUI, which seeds the low health shake
     */
    public static void renderSuppressedDamage(GuiGraphics graphics, Player player, HeartBar bar, float maxHealth, int health, int absorption, int guiTick) {
        int suppressed = Math.min(suppressedHealth(player), health + absorption);

        if (suppressed <= 0 || !player.hasEffect(CataclysmMobEffects.IMMORTALITY) || !blinks(suppressedEndTick - player.tickCount)) {
            return;
        }

        int healthHearts = Mth.ceil(maxHealth / HEALTH_PER_HEART);
        int totalHearts = healthHearts + Mth.ceil((float) absorption / HEALTH_PER_HEART);
        int[] shake = shakeOffsets(totalHearts, health + absorption, guiTick);
        int suppressedAbsorption = Math.min(suppressed, absorption);

        renderDamageRange(graphics, bar, shake, healthHearts, absorption - suppressedAbsorption, absorption);
        renderDamageRange(graphics, bar, shake, 0, health - suppressed + suppressedAbsorption, health);
    }

    private static int suppressedHealth(Player player) {
        boolean active = player.getUUID().equals(suppressedPlayer) && player.tickCount < suppressedEndTick;
        return active ? Mth.ceil(suppressedDamage) : 0;
    }

    /** Mirrors the low health shake of vanilla, so the blinking hearts move with the ones below them. */
    private static int[] shakeOffsets(int totalHearts, int totalHealth, int guiTick) {
        int[] offsets = new int[totalHearts];

        if (totalHealth <= SHAKE_HEALTH) {
            SHAKE_RANDOM.setSeed(guiTick * SHAKE_SEED);

            for (int heart = totalHearts - 1; heart >= 0; heart--) {
                offsets[heart] = SHAKE_RANDOM.nextInt(2);
            }
        }

        return offsets;
    }

    private static void renderDamageRange(GuiGraphics graphics, HeartBar bar, int[] shake, int firstHeart, int start, int end) {
        for (int relativeHeart = (end - 1) / HEALTH_PER_HEART; relativeHeart >= start / HEALTH_PER_HEART; relativeHeart--) {
            int heartStart = relativeHeart * HEALTH_PER_HEART;
            int points = Math.min(end, heartStart + HEALTH_PER_HEART) - Math.max(start, heartStart);

            if (points > 0) {
                int heart = firstHeart + relativeHeart;
                int lift = firstHeart == 0 && relativeHeart == bar.regenerationSlot() ? REGENERATION_LIFT : 0;
                int x = bar.left() + heart % HEARTS_PER_ROW * HEART_SPACING;
                int y = bar.bottom() - heart / HEARTS_PER_ROW * bar.rowHeight() + shake[heart] - lift;

                drawHeart(graphics, immunitySprite(points == 1, true), x, y);
            }
        }
    }

    /**
     * Makes the most recently disabled heart blink when another half is disabled.
     *
     * @param player the player owning the health bar
     */
    public static void updatePaleHearts(Player player) {
        float currentDisabledHealth = PaleCorrosionMobEffect.getDisabledHealth(player);

        if (currentDisabledHealth > disabledHealth) {
            paleEndTick = player.tickCount + BLINK_DURATION;
        }

        disabledHealth = currentDisabledHealth;
    }

    /**
     * Renders the disabled part of a heart, if any.
     *
     * @param graphics the graphics to render with
     * @param player the player owning the health bar
     * @param slot the index of the heart in the health bar
     * @param x the x position of the heart
     * @param y the y position of the heart
     */
    public static void renderPaleHeart(GuiGraphics graphics, Player player, int slot, int x, int y) {
        float disabled = PaleCorrosionMobEffect.getDisabledHealth(player);
        if (disabled <= 0.0F) return;

        int firstDisabled = Mth.ceil(player.getMaxHealth());
        int lastDisabled = firstDisabled + Mth.ceil(disabled);
        int left = slot * HEALTH_PER_HEART;

        boolean full = left >= firstDisabled && left < lastDisabled;
        boolean half = left + 1 == firstDisabled;
        if (!full && !half) return;

        boolean blinking = slot == firstDisabled / HEALTH_PER_HEART && blinks(paleEndTick - player.tickCount);

        drawHeart(graphics, paleSprite(full, blinking), x, y);
    }

    private static ResourceLocation paleSprite(boolean full, boolean blinking) {
        if (full) {
            return blinking ? PALE_FULL_BLINKING : PALE_FULL;
        }

        return blinking ? PALE_HALF_BLINKING : PALE_HALF;
    }

    private static boolean blinks(int remainingTicks) {
        return remainingTicks > 0 && remainingTicks / BLINK_INTERVAL % 2 == 1;
    }

    private static void drawHeart(GuiGraphics graphics, ResourceLocation sprite, int x, int y) {
        graphics.blitSprite(RenderType::guiTextured, sprite, x, y, HEART_SIZE, HEART_SIZE);
    }

    /** Where the health bar is drawn. Passing a slot no heart has means no heart is lifted by regeneration. */
    public record HeartBar(int left, int bottom, int rowHeight, int regenerationSlot) {}
}
