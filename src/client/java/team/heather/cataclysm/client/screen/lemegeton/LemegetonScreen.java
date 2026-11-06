package team.heather.cataclysm.client.screen.lemegeton;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.data.LemegetonComponent;
import team.heather.cataclysm.item.data.UpgradeTierComponent;
import team.heather.cataclysm.menu.CataclysmMenus;
import team.heather.cataclysm.menu.lemegeton.LemegetonMenu;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

/**
 * An open grimoire. Its first spread is the Circle of Solomon on the left page, with one
 * seal per spirit, and everything about the chosen seal on the right. The pages that
 * follow are {@link LemegetonJournal}, a field journal of Cataclysm's bosses.
 */
public final class LemegetonScreen extends AbstractContainerScreen<LemegetonMenu> {

    private static final LemegetonComponent.Upgrade[] UPGRADES = LemegetonComponent.Upgrade.values();

    private static final int BOOK_WIDTH = 352;
    private static final int BOOK_HEIGHT = 216;

    private static final int CIRCLE_X = 90;
    private static final int CIRCLE_Y = 108;
    private static final int ORBIT_RADIUS = 60;
    private static final long SEAL_APPEAR_DELAY_MILLIS = 35;

    private static final int PAGE_X = 262;
    private static final int TITLE_Y = 28;
    private static final int LARGE_SEAL_Y = 48;
    private static final int LEVELS_Y = 96;
    private static final int LEVEL_LINE_HEIGHT = 11;
    private static final int MAX_LEVEL_LINES = 3;
    private static final int LEVEL_MARK_SIZE = 4;
    private static final int LEVEL_MARK_GAP = 4;
    private static final int DESCRIPTION_Y = 132;
    private static final int TEXT_WIDTH = 140;
    private static final int PACT_Y = 152;
    private static final int HINT_CENTER_Y = 181;
    private static final int HINT_WIDTH = 132;
    private static final int ICON_SIZE = 16;
    private static final int ICON_GAP = 4;
    private static final float PAGE_FADE_MILLIS = 250;
    private static final float WINDOW_FILL = 0.98F;
    private static final float ZOOM_STEP = 0.5F;

    private static final int PREVIOUS_X = 14;
    private static final int NEXT_X = 326;
    private static final int TURN_Y = 182;
    private static final int PREVIOUS_ICON = 0;
    private static final int NEXT_ICON = 1;

    private final Map<LemegetonComponent.Upgrade, ResourceLocation> engravings = new EnumMap<>(LemegetonComponent.Upgrade.class);
    private final List<SealButton> seals = new ArrayList<>();
    private final Embers embers = new Embers();
    private final LemegetonJournal journal = new LemegetonJournal(this::renderSealPage);
    private final long openedAt = Util.getMillis();
    private LemegetonComponent.Upgrade selected = UPGRADES[0];
    private LemegetonComponent.Upgrade shown = this.selected;
    private long shownAt = this.openedAt;
    private LemegetonComponent.Upgrade awaited;
    private int awaitedLevel;
    private PactButton pactButton;
    private IconButton previousButton;
    private IconButton nextButton;
    private float zoom = 1;

    public LemegetonScreen(LemegetonMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = BOOK_WIDTH;
        this.imageHeight = BOOK_HEIGHT;
        for (var upgrade : UPGRADES) {
            this.engravings.put(upgrade, LemegetonTheme.engrave(upgrade));
        }
    }

    public static void register() {
        MenuScreens.register(CataclysmMenus.LEMEGETON, LemegetonScreen::new);
    }

    @Override
    protected void init() {
        var window = this.minecraft.getWindow();
        this.zoom = this.zoomToFit();
        this.width = (int) (window.getGuiScaledWidth() / this.zoom);
        this.height = (int) (window.getGuiScaledHeight() / this.zoom);
        super.init();
        this.seals.clear();
        for (var upgrade : UPGRADES) {
            this.seals.add(this.addRenderableWidget(this.createSeal(upgrade)));
        }
        this.pactButton = this.addRenderableWidget(new PactButton(
            this.leftPos + PAGE_X, this.topPos + PACT_Y,
            this::pactLabel, this::canBuySelected, this::requestPact));
        this.previousButton = this.addRenderableWidget(new IconButton(this.leftPos + PREVIOUS_X, this.topPos + TURN_Y,
            () -> PREVIOUS_ICON, () -> this.turnPage(-1)));
        this.nextButton = this.addRenderableWidget(new IconButton(this.leftPos + NEXT_X, this.topPos + TURN_Y,
            () -> NEXT_ICON, () -> this.turnPage(1)));
        this.refreshWidgets();
    }

    /**
     * Grows the whole book to fill the window. Screen pixels per book pixel move in half steps, so
     * the pixel art stays as even as it can, and everything inside just works in zoomed coordinates.
     */
    private float zoomToFit() {
        var window = this.minecraft.getWindow();
        float fit = Math.min(window.getWidth() / (float) BOOK_WIDTH, window.getHeight() / (float) BOOK_HEIGHT) * WINDOW_FILL;
        float pixels = (float) Math.floor(fit / ZOOM_STEP) * ZOOM_STEP;
        return Math.max(1, pixels / (float) window.getGuiScale());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX / this.zoom, mouseY / this.zoom, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX / this.zoom, mouseY / this.zoom, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return super.mouseDragged(mouseX / this.zoom, mouseY / this.zoom, button, dragX / this.zoom, dragY / this.zoom);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX / this.zoom, mouseY / this.zoom);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            this.pressTurnButton(this.previousButton);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.pressTurnButton(this.nextButton);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void pressTurnButton(IconButton button) {
        if (button.active) {
            button.playDownSound(this.minecraft.getSoundManager());
            button.onPress();
        }
    }

    @Override
    protected void containerTick() {
        if (this.awaited != null && this.menu.level(this.awaited) > this.awaitedLevel) {
            this.celebrate(this.awaited);
            this.awaited = null;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.refreshWidgets();
        if (this.journal.isOnSeals()) {
            this.showPage(this.hoveredUpgrade());
        }
        graphics.pose().pushPose();
        graphics.pose().scale(this.zoom, this.zoom, 1);
        super.render(graphics, (int) (mouseX / this.zoom), (int) (mouseY / this.zoom), partialTick);
        this.embers.render(graphics);
        graphics.pose().popPose();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        this.journal.drawBook(graphics, this.leftPos, this.topPos);
        this.journal.drawArt(graphics, this.leftPos, this.topPos, this.pageInk());
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        float ink = this.pageInk();
        if (this.journal.showsCircle()) {
            this.renderCounter(graphics);
        }
        if (this.journal.isOnSeals()) {
            this.renderSealDetails(graphics, ink);
        } else {
            this.journal.drawWriting(graphics, this.font, ink);
        }
    }

    private void renderSealDetails(GuiGraphics graphics, float ink) {
        this.renderTitle(graphics, ink);
        this.renderLargeSeal(graphics, ink);
        this.renderLevels(graphics, ink);
        this.renderDescription(graphics, ink);
        this.renderHint(graphics, ink);
    }

    /** Paints the seal page onto a turning leaf, with its button, since widgets stay out of a moving page. */
    private void renderSealPage(GuiGraphics graphics) {
        this.renderSealDetails(graphics, 1);
        graphics.pose().pushPose();
        graphics.pose().translate(-this.leftPos, -this.topPos, 0);
        this.pactButton.visible = true;
        this.pactButton.render(graphics, -1, -1, 0);
        this.pactButton.visible = false;
        graphics.pose().popPose();
    }

    private float pageInk() {
        return Math.min(1, (Util.getMillis() - this.shownAt) / PAGE_FADE_MILLIS);
    }

    private void turnPage(int direction) {
        this.journal.turn(direction);
        this.refreshWidgets();
    }

    private void refreshWidgets() {
        for (var seal : this.seals) {
            seal.visible = this.journal.showsCircle();
        }
        this.pactButton.visible = this.journal.isOnSeals();
        this.previousButton.visible = this.journal.hasPrevious();
        this.nextButton.active = this.journal.hasNext();
    }

    private SealButton createSeal(LemegetonComponent.Upgrade upgrade) {
        double angle = Math.toRadians(-90 + upgrade.ordinal() * 360.0 / UPGRADES.length);
        int x = CIRCLE_X + (int) Math.round(Math.cos(angle) * ORBIT_RADIUS) - SealButton.SIZE / 2;
        int y = CIRCLE_Y + (int) Math.round(Math.sin(angle) * ORBIT_RADIUS) - SealButton.SIZE / 2;
        long appearsAt = this.openedAt + upgrade.ordinal() * SEAL_APPEAR_DELAY_MILLIS;
        return new SealButton(this.leftPos + x, this.topPos + y, upgrade, this.menu, this.engravings.get(upgrade), appearsAt,
            () -> this.selected == upgrade, this::select);
    }

    private void select(LemegetonComponent.Upgrade upgrade) {
        this.selected = upgrade;
    }

    private LemegetonComponent.Upgrade hoveredUpgrade() {
        for (var seal : this.seals) {
            if (seal.isHovered()) {
                return seal.getUpgrade();
            }
        }
        return this.selected;
    }

    private void showPage(LemegetonComponent.Upgrade upgrade) {
        if (upgrade != this.shown) {
            this.shown = upgrade;
            this.shownAt = Util.getMillis();
        }
    }

    private boolean canBuySelected() {
        return this.menu.denial(this.selected).isEmpty();
    }

    private Component pactLabel() {
        int level = this.menu.level(this.selected);
        String action = level >= this.selected.maxLevel() ? "complete" : level > 0 ? "upgrade" : "unlock";
        return Component.translatable("menu.cataclysm.lemegeton." + action);
    }

    private void requestPact() {
        if (this.canBuySelected()) {
            this.awaited = this.selected;
            this.awaitedLevel = this.menu.level(this.selected);
        } else {
            this.pactButton.shake();
        }
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, this.selected.ordinal());
    }

    private void celebrate(LemegetonComponent.Upgrade upgrade) {
        var seal = this.seals.get(upgrade.ordinal());
        seal.flash();
        this.embers.burst(seal.getX() + SealButton.SIZE / 2, seal.getY() + SealButton.SIZE / 2);
        this.embers.burst(this.leftPos + PAGE_X, this.topPos + LARGE_SEAL_Y + SealButton.LARGE_SIZE / 2);
        this.shownAt = Util.getMillis();
    }

    private void renderCounter(GuiGraphics graphics) {
        int total = this.menu.totalLevels();
        var text = Component.literal(total + "/" + this.menu.capacity());
        int color = total >= this.menu.capacity() ? LemegetonTheme.RUBRIC : LemegetonTheme.INK;
        this.drawCentered(graphics, text, CIRCLE_X, CIRCLE_Y - this.font.lineHeight / 2, color);
    }

    private void renderTitle(GuiGraphics graphics, float ink) {
        var name = Component.translatable(this.shown.translationKey());
        this.drawCentered(graphics, name, PAGE_X, TITLE_Y, LemegetonTheme.faded(LemegetonTheme.RUBRIC, ink));
    }

    private void renderLargeSeal(GuiGraphics graphics, float ink) {
        this.seals.get(this.shown.ordinal()).paintLarge(graphics, PAGE_X - SealButton.LARGE_SIZE / 2, LARGE_SEAL_Y, ink);
    }

    private void renderLevels(GuiGraphics graphics, float ink) {
        int owned = this.menu.level(this.shown);
        int count = this.shown.maxLevel();
        int top = LEVELS_Y + (MAX_LEVEL_LINES - count) * LEVEL_LINE_HEIGHT / 2;
        for (int level = 1; level <= count; level++) {
            int color = level <= owned ? LemegetonTheme.RUBRIC : level == owned + 1 ? LemegetonTheme.INK : LemegetonTheme.FADED_INK;
            this.renderLevelLine(graphics, level, top + (level - 1) * LEVEL_LINE_HEIGHT, level <= owned,
                LemegetonTheme.faded(color, ink));
        }
    }

    private void renderLevelLine(GuiGraphics graphics, int level, int y, boolean owned, int color) {
        var text = Component.translatable(this.shown.translationKey())
            .append(" ")
            .append(Component.translatable("enchantment.level." + this.shown.effectLevel(level)));
        int width = LEVEL_MARK_SIZE + LEVEL_MARK_GAP + this.font.width(text);
        int x = PAGE_X - width / 2;
        int markY = y + (this.font.lineHeight - LEVEL_MARK_SIZE) / 2 - 1;
        if (owned) {
            graphics.fill(x, markY, x + LEVEL_MARK_SIZE, markY + LEVEL_MARK_SIZE, color);
        } else {
            graphics.renderOutline(x, markY, LEVEL_MARK_SIZE, LEVEL_MARK_SIZE, color);
        }
        graphics.drawString(this.font, text, x + LEVEL_MARK_SIZE + LEVEL_MARK_GAP, y, color, false);
    }

    private void renderDescription(GuiGraphics graphics, float ink) {
        var lines = this.font.split(Component.translatable(this.shown.descriptionKey()), TEXT_WIDTH);
        int y = DESCRIPTION_Y;
        for (var line : lines) {
            graphics.drawString(this.font, line, PAGE_X - this.font.width(line) / 2, y,
                LemegetonTheme.faded(LemegetonTheme.INK, ink), false);
            y += this.font.lineHeight;
        }
    }

    private void renderHint(GuiGraphics graphics, float ink) {
        var denial = this.menu.denial(this.selected);
        var hint = denial.orElseGet(() -> Component.translatable("menu.cataclysm.lemegeton.ready"));
        int color = LemegetonTheme.faded(denial.isPresent() ? LemegetonTheme.RUBRIC : LemegetonTheme.FADED_INK, ink);
        boolean showsCost = this.menu.level(this.selected) < this.selected.maxLevel();
        var lines = this.font.split(hint, HINT_WIDTH);
        int textWidth = this.widestLine(lines);
        int iconWidth = showsCost ? ICON_SIZE + ICON_GAP : 0;
        int left = PAGE_X - (iconWidth + textWidth) / 2;
        if (showsCost) {
            graphics.renderItem(this.costIcon(), left, HINT_CENTER_Y - ICON_SIZE / 2);
        }
        int y = HINT_CENTER_Y - lines.size() * this.font.lineHeight / 2;
        for (var line : lines) {
            int x = showsCost ? left + iconWidth : PAGE_X - this.font.width(line) / 2;
            graphics.drawString(this.font, line, x, y, color, false);
            y += this.font.lineHeight;
        }
    }

    private int widestLine(List<FormattedCharSequence> lines) {
        int widest = 0;
        for (var line : lines) {
            widest = Math.max(widest, this.font.width(line));
        }
        return widest;
    }

    private ItemStack costIcon() {
        var stack = new ItemStack(CataclysmItems.CATACLYSM_UPGRADE);
        stack.set(CataclysmDataComponents.UPGRADE_TIER, UpgradeTierComponent.of(this.menu.requiredTier()));
        return stack;
    }

    private void drawCentered(GuiGraphics graphics, Component text, int x, int y, int color) {
        graphics.drawString(this.font, text, x - this.font.width(text) / 2, y, color, false);
    }

    /** A small inked icon that reddens under the cursor and turns a page when pressed. */
    private static final class IconButton extends AbstractButton {

        private final IntSupplier icon;
        private final Runnable action;

        private IconButton(int x, int y, IntSupplier icon, Runnable action) {
            super(x, y, LemegetonTheme.ICON_SIZE, LemegetonTheme.ICON_SIZE, Component.empty());
            this.icon = icon;
            this.action = action;
        }

        @Override
        public void onPress() {
            if (this.active) {
                this.action.run();
            }
        }

        @Override
        public void playDownSound(SoundManager sounds) {
            sounds.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1));
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            int color = !this.active ? LemegetonTheme.FADED_INK
                : this.isHoveredOrFocused() ? LemegetonTheme.RUBRIC : LemegetonTheme.INK;
            LemegetonTheme.drawJournalIcon(graphics, this.icon.getAsInt(), this.getX(), this.getY(), color);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }
}
