package team.heather.cataclysm.client.screen.lemegeton;

import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import team.heather.cataclysm.item.data.LemegetonComponent;
import team.heather.cataclysm.menu.lemegeton.LemegetonMenu;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/** One spirit of the circle: each level adds to the frame, and red ink marks the last one. */
final class SealButton extends AbstractButton {

    static final int SIZE = 24;
    static final int LARGE_SIZE = 40;

    private static final int LARGE_FRAME_V = 28;
    private static final int LAST_FRAME = 2;
    private static final int DISC_U = 72;
    private static final int HALO_U = 96;
    private static final int HALO_SIZE = 28;
    private static final int HALO_INSET = 2;
    private static final int ENGRAVING_SIZE = 16;
    private static final int LARGE_ENGRAVING_SIZE = 32;

    private static final float APPEAR_MILLIS = 300;
    private static final float FLASH_MILLIS = 800;

    @Getter private final LemegetonComponent.Upgrade upgrade;
    private final LemegetonMenu menu;
    private final ResourceLocation engraving;
    private final long appearsAt;
    private final BooleanSupplier selected;
    private final Consumer<LemegetonComponent.Upgrade> onSelect;
    private long flashedAt;

    SealButton(int x, int y, LemegetonComponent.Upgrade upgrade, LemegetonMenu menu, ResourceLocation engraving, long appearsAt,
               BooleanSupplier selected, Consumer<LemegetonComponent.Upgrade> onSelect) {
        super(x, y, SIZE, SIZE, Component.translatable(upgrade.translationKey()));
        this.upgrade = upgrade;
        this.menu = menu;
        this.engraving = engraving;
        this.appearsAt = appearsAt;
        this.selected = selected;
        this.onSelect = onSelect;
    }

    /** The open page has its own frames: scaling the small ones would not land on whole pixels. */
    void paintLarge(GuiGraphics graphics, int x, int y, float opacity) {
        this.paint(graphics, x, y, 0, LARGE_FRAME_V, LARGE_SIZE, LARGE_ENGRAVING_SIZE, opacity);
    }

    void flash() {
        this.flashedAt = Util.getMillis();
    }

    @Override
    public void onPress() {
        this.onSelect.accept(this.upgrade);
    }

    @Override
    public void playDownSound(SoundManager sounds) {
        sounds.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.2F, 0.6F));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        float opacity = Mth.clamp((Util.getMillis() - this.appearsAt) / APPEAR_MILLIS, 0, 1);
        this.paint(graphics, this.getX(), this.getY(), 0, 0, SIZE, ENGRAVING_SIZE, opacity);
        if (this.selected.getAsBoolean() || this.isHoveredOrFocused()) {
            int color = this.selected.getAsBoolean() ? LemegetonTheme.INK : LemegetonTheme.FADED_INK;
            LemegetonTheme.drawSeal(graphics, this.getX() - HALO_INSET, this.getY() - HALO_INSET, HALO_U, 0, HALO_SIZE, color);
        }
        this.renderFlash(graphics);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    private void paint(GuiGraphics graphics, int x, int y, int frameU, int frameV, int size, int engravingSize, float opacity) {
        int level = this.menu.level(this.upgrade);
        int color = LemegetonTheme.faded(this.inkFor(level), opacity);
        int frame = Math.min(Math.max(level, 1) - 1, LAST_FRAME);
        LemegetonTheme.drawSeal(graphics, x, y, frameU + frame * size, frameV, size, color);
        int inset = (size - engravingSize) / 2;
        LemegetonTheme.drawEngraving(graphics, this.engraving, x + inset, y + inset, engravingSize, color);
    }

    private int inkFor(int level) {
        if (level == 0) {
            return LemegetonTheme.FADED_INK;
        }
        return level >= this.upgrade.maxLevel() ? LemegetonTheme.RUBRIC : LemegetonTheme.INK;
    }

    private void renderFlash(GuiGraphics graphics) {
        float left = 1 - (Util.getMillis() - this.flashedAt) / FLASH_MILLIS;
        if (left > 0) {
            int color = LemegetonTheme.faded(LemegetonTheme.BRIGHT_RUBRIC, left);
            LemegetonTheme.drawSeal(graphics, this.getX(), this.getY(), DISC_U, 0, SIZE, color);
        }
    }
}
