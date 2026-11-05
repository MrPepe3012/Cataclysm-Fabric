package team.heather.cataclysm.client.screen.lemegeton;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.component.data.lemegeton.LemegetonUpgrade;
import team.heather.cataclysm.item.lemegeton.LemegetonMenu;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * An open grimoire: the Circle of Solomon on the left page, with one seal per
 * spirit, and on the right page everything about the chosen seal.
 */
public final class LemegetonScreen extends AbstractContainerScreen<LemegetonMenu> {

    private static final LemegetonUpgrade[] UPGRADES = LemegetonUpgrade.values();

    private static final int BOOK_WIDTH = 352;
    private static final int BOOK_HEIGHT = 192;

    private static final int CIRCLE_X = 90;
    private static final int CIRCLE_Y = 96;
    private static final int ORBIT_RADIUS = 60;
    private static final long SEAL_APPEAR_DELAY_MILLIS = 35;

    private static final int PAGE_X = 262;
    private static final int TITLE_Y = 16;
    private static final int LARGE_SEAL_Y = 36;
    private static final int LEVELS_Y = 84;
    private static final int LEVEL_LINE_HEIGHT = 11;
    private static final int MAX_LEVEL_LINES = 3;
    private static final int LEVEL_MARK_SIZE = 4;
    private static final int LEVEL_MARK_GAP = 4;
    private static final int DESCRIPTION_Y = 120;
    private static final int TEXT_WIDTH = 140;
    private static final int PACT_Y = 140;
    private static final int HINT_Y = 160;
    private static final int HINT_WIDTH = 132;
    private static final int ICON_SIZE = 16;
    private static final int ICON_GAP = 4;
    private static final float PAGE_FADE_MILLIS = 250;

    private final Map<LemegetonUpgrade, ResourceLocation> engravings = new EnumMap<>(LemegetonUpgrade.class);
    private final List<SealButton> seals = new ArrayList<>();
    private final Embers embers = new Embers();
    private final long openedAt = Util.getMillis();
    private LemegetonUpgrade selected = UPGRADES[0];
    private LemegetonUpgrade shown = this.selected;
    private long shownAt = this.openedAt;
    private LemegetonUpgrade awaited;
    private int awaitedLevel;
    private PactButton pactButton;

    public LemegetonScreen(LemegetonMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = BOOK_WIDTH;
        this.imageHeight = BOOK_HEIGHT;
        for (var upgrade : UPGRADES) {
            this.engravings.put(upgrade, LemegetonTheme.engrave(upgrade));
        }
    }

    public static void register() {
        MenuScreens.register(CataclysmRegistry.MENUS.LEMEGETON, LemegetonScreen::new);
    }

    @Override
    protected void init() {
        super.init();
        this.seals.clear();
        for (var upgrade : UPGRADES) {
            this.seals.add(this.addRenderableWidget(this.createSeal(upgrade)));
        }
        this.pactButton = this.addRenderableWidget(new PactButton(
            this.leftPos + PAGE_X, this.topPos + PACT_Y,
            this::pactLabel, this::canBuySelected, this::requestPact));
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
        this.showPage(this.hoveredUpgrade());
        super.render(graphics, mouseX, mouseY, partialTick);
        this.embers.render(graphics);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        LemegetonTheme.drawBook(graphics, this.leftPos, this.topPos);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        this.renderCounter(graphics);
        float ink = Math.min(1, (Util.getMillis() - this.shownAt) / PAGE_FADE_MILLIS);
        this.renderTitle(graphics, ink);
        this.renderLargeSeal(graphics, ink);
        this.renderLevels(graphics, ink);
        this.renderDescription(graphics, ink);
        this.renderHint(graphics, ink);
    }

    private SealButton createSeal(LemegetonUpgrade upgrade) {
        double angle = Math.toRadians(-90 + upgrade.ordinal() * 360.0 / UPGRADES.length);
        int x = CIRCLE_X + (int) Math.round(Math.cos(angle) * ORBIT_RADIUS) - SealButton.SIZE / 2;
        int y = CIRCLE_Y + (int) Math.round(Math.sin(angle) * ORBIT_RADIUS) - SealButton.SIZE / 2;
        long appearsAt = this.openedAt + upgrade.ordinal() * SEAL_APPEAR_DELAY_MILLIS;
        return new SealButton(this.leftPos + x, this.topPos + y, upgrade, this.menu, this.engravings.get(upgrade), appearsAt,
            () -> this.selected == upgrade, this::select);
    }

    private void select(LemegetonUpgrade upgrade) {
        this.selected = upgrade;
    }

    private LemegetonUpgrade hoveredUpgrade() {
        for (var seal : this.seals) {
            if (seal.isHovered()) {
                return seal.getUpgrade();
            }
        }
        return this.selected;
    }

    private void showPage(LemegetonUpgrade upgrade) {
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

    private void celebrate(LemegetonUpgrade upgrade) {
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
        int x = PAGE_X - HINT_WIDTH / 2;
        if (showsCost) {
            x += (ICON_SIZE + ICON_GAP) / 2;
            graphics.renderItem(this.costIcon(), x - ICON_SIZE - ICON_GAP, HINT_Y);
        }
        int y = HINT_Y + (showsCost ? 0 : 1);
        for (var line : this.font.split(hint, HINT_WIDTH)) {
            graphics.drawString(this.font, line, x, y, color, false);
            y += this.font.lineHeight;
        }
    }

    private ItemStack costIcon() {
        var stack = new ItemStack(CataclysmRegistry.ITEMS.CATACLYSM_UPGRADE);
        stack.set(CataclysmRegistry.DATA_COMPONENTS.UPGRADE_TIER, Math.max(this.menu.requiredTier(), CataclysmRegistry.DATA_COMPONENTS.MIN_UPGRADE_TIER));
        return stack;
    }

    private void drawCentered(GuiGraphics graphics, Component text, int x, int y, int color) {
        graphics.drawString(this.font, text, x - this.font.width(text) / 2, y, color, false);
    }
}
