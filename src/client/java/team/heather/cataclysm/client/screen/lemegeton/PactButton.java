package team.heather.cataclysm.client.screen.lemegeton;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/** Red and breathing when the upgrade can be bought; a faded outline, which shakes if pressed, when not. */
final class PactButton extends AbstractButton {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 16;
    private static final float BREATH_MILLIS = 450;
    private static final float SHAKE_MILLIS = 350;
    private static final float SHAKE_SPEED = 0.06F;
    private static final int SHAKE_DISTANCE = 2;

    private final Supplier<Component> label;
    private final BooleanSupplier available;
    private final Runnable action;
    private long shookAt;

    PactButton(int centerX, int y, Supplier<Component> label, BooleanSupplier available, Runnable action) {
        super(centerX - WIDTH / 2, y, WIDTH, HEIGHT, label.get());
        this.label = label;
        this.available = available;
        this.action = action;
    }

    void shake() {
        this.shookAt = Util.getMillis();
    }

    @Override
    public void onPress() {
        this.action.run();
    }

    /** The server answers every press, so a local click would only sound twice. */
    @Override
    public void playDownSound(SoundManager sounds) {}

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = this.getX() + this.shakeOffset();
        int y = this.getY();
        if (this.available.getAsBoolean()) {
            this.renderFilled(graphics, x, y);
            this.renderLabel(graphics, x, LemegetonTheme.PARCHMENT);
        } else {
            int color = this.isHoveredOrFocused() ? LemegetonTheme.INK : LemegetonTheme.FADED_INK;
            graphics.renderOutline(x, y, this.width, this.height, color);
            this.renderLabel(graphics, x, color);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    private void renderFilled(GuiGraphics graphics, int x, int y) {
        float breath = this.isHoveredOrFocused() ? 1 : (Mth.sin(Util.getMillis() / BREATH_MILLIS) + 1) / 2;
        int color = ARGB.lerp(breath, LemegetonTheme.RUBRIC, LemegetonTheme.BRIGHT_RUBRIC);
        graphics.fill(x + 1, y, x + this.width - 1, y + this.height, color);
        graphics.fill(x, y + 1, x + this.width, y + this.height - 1, color);
    }

    private void renderLabel(GuiGraphics graphics, int x, int color) {
        var font = Minecraft.getInstance().font;
        var text = this.label.get();
        graphics.drawString(font, text, x + (this.width - font.width(text)) / 2,
            this.getY() + (this.height - font.lineHeight) / 2 + 1, color, false);
    }

    private int shakeOffset() {
        long elapsed = Util.getMillis() - this.shookAt;
        if (elapsed >= SHAKE_MILLIS) {
            return 0;
        }
        return Math.round(Mth.sin(elapsed * SHAKE_SPEED * Mth.PI) * SHAKE_DISTANCE);
    }
}
