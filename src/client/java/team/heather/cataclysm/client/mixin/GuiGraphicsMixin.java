package team.heather.cataclysm.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.heather.cataclysm.mixin.ItemCooldownsMixin;

import java.util.Locale;

/** Writes the time left on every item in cooldown at the top center of its slot. */
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Unique private static final int SLOT_CENTER = 8;
    @Unique private static final int SLOT_MARGIN = 1;
    /** The same layer vanilla uses for the stack count, so the text stays above the item. */
    @Unique private static final float TEXT_DEPTH = 200;
    @Unique private static final int TEXT_COLOR = 0xFFFFFFFF;
    @Unique private static final int GUI_SCALE_PER_TEXT_PIXEL = 2;

    @Unique private static final float TICKS_PER_SECOND = 20;
    @Unique private static final int SECONDS_PER_MINUTE = 60;
    @Unique private static final int TENTHS_PER_SECOND = 10;
    @Unique private static final int TENTHS_BELOW = 10 * TENTHS_PER_SECOND;

    @Shadow
    public abstract PoseStack pose();

    @Shadow
    public abstract int drawString(Font font, String text, int x, int y, int color);

    @Inject(method = "renderItemCooldown", at = @At("TAIL"))
    private void renderItemCooldown$0(ItemStack stack, int x, int y, CallbackInfo callback) {
        float ticks = remainingTicks$0(stack);
        if (ticks > 0) {
            this.drawTimeLeft$0(Minecraft.getInstance().font, formatTimeLeft$0(ticks / TICKS_PER_SECOND), x, y);
        }
    }

    /** Half size text, rounded so every pixel of the font covers a whole number of screen pixels at any GUI scale. */
    @Unique
    private void drawTimeLeft$0(Font font, String text, int x, int y) {
        int guiScale = (int) Minecraft.getInstance().getWindow().getGuiScale();
        float scale = (float) Math.max(guiScale / GUI_SCALE_PER_TEXT_PIXEL, 1) / guiScale;
        var pose = this.pose();
        pose.pushPose();
        pose.translate(x + SLOT_CENTER, y + SLOT_MARGIN, TEXT_DEPTH);
        pose.scale(scale, scale, 1);
        this.drawString(font, text, -font.width(text) / 2, 0, TEXT_COLOR);
        pose.popPose();
    }

    @Unique
    private static float remainingTicks$0(ItemStack stack) {
        var client = Minecraft.getInstance();
        if (client.player == null) {
            return 0;
        }
        var cooldowns = client.player.getCooldowns();
        var accessor = (ItemCooldownsMixin) cooldowns;
        var cooldown = accessor.getCooldowns$0().get(cooldowns.getCooldownGroup(stack));
        if (cooldown == null) {
            return 0;
        }
        float partialTick = client.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        return cooldown.endTime() - (accessor.getTickCount$0() + partialTick);
    }

    /** Rounds up, so the text never reads zero while the item is still waiting. */
    @Unique
    private static String formatTimeLeft$0(float seconds) {
        int tenths = Mth.ceil(seconds * TENTHS_PER_SECOND);
        if (tenths < TENTHS_BELOW) {
            return String.format(Locale.ROOT, "%.1fs", (float) tenths / TENTHS_PER_SECOND);
        }
        int wholeSeconds = Mth.ceil(seconds);
        if (wholeSeconds < SECONDS_PER_MINUTE) {
            return wholeSeconds + "s";
        }
        return Mth.positiveCeilDiv(wholeSeconds, SECONDS_PER_MINUTE) + "m";
    }
}
