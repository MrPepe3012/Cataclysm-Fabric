package team.heather.cataclysm.client.screen.lemegeton;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import team.heather.cataclysm.Cataclysm;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * The journal half of the Lemegeton. Its pages are data, not code: each one is a
 * JSON file in {@code assets/cataclysm/lemegeton_journal}, read in filename order
 * every time the book opens. The readme in that folder explains the format. They follow
 * the seals, which are the book's first spread. While developing, pages are read straight
 * from the source folder, so saving a file and reopening the book is enough to see it.
 */
final class LemegetonJournal {

    private static final String FOLDER = "lemegeton_journal";
    private static final String SOURCE_FOLDER = "src/main/resources/assets/cataclysm/" + FOLDER;
    private static final String PAGE_EXTENSION = ".json";
    private static final String IGNORED_PREFIX = "_";
    private static final int PAGES_PER_SPREAD = 2;
    private static final int[] PAGE_X = {10, 186};
    private static final int[] PAGE_NUMBER_X = {137, 10};
    private static final int PLATE_Y = 10;
    private static final int PAGE_Y = 22;
    private static final int PAGE_CENTER_X = 78;
    private static final int PAGE_NUMBER_Y = 161;
    private static final int TITLE_Y = 3;
    private static final int TITLE_LINE_HEIGHT = 8;
    private static final int SUBTITLE_GAP = 6;
    private static final int LINE_HEIGHT = 8;
    private static final int ATTACK_LINE_HEIGHT = 7;
    private static final int LINE_GAP = 1;
    private static final int ATTACK_GAP = 2;
    private static final int PLATE_COLOR = 0xFFFFFFFF;
    private static final String RED_MARK = "!";
    private static final int ERROR_NOTE_Y = 40;

    private static final float TURN_MILLIS = 260;
    private static final int SPINE_X = 176;
    private static final int GUTTER = 6;
    private static final int[] LEAF_X = {8, 182};
    private static final int LEAF_Y = 8;
    private static final int LEAF_WIDTH = 162;
    private static final int LEAF_HEIGHT = 200;
    private static final int LEAF_STRIPS = 18;
    private static final float LEAF_BEND = 1.1F;
    private static final float LEAF_PERSPECTIVE = 0.16F;
    private static final float LEAF_SHADE = 0.3F;
    private static final int SHADOW_STEPS = 5;
    private static final int SHADOW_REACH = 26;
    private static final float SHADOW_STRENGTH = 0.28F;
    private static final int MOST_EXTRA_SHEETS = 6;
    private static final int SHEETS_PER_DROP = 3;
    private static final int SHEET_LIGHT = 0xFFCFBF9A;
    private static final int SHEET_DARK = 0xFF8F7D5C;

    private final Consumer<GuiGraphics> sealPage;
    private final List<Page> pages = new ArrayList<>();
    private final Map<String, Page> pagesById = new HashMap<>();
    private int spread;
    private int turningFrom = -1;
    private long turnStartedAt;

    LemegetonJournal(Consumer<GuiGraphics> sealPage) {
        this.sealPage = sealPage;
        for (var file : pageFiles().entrySet()) {
            String id = file.getKey();
            if (id.startsWith(IGNORED_PREFIX)) {
                continue;
            }
            var page = readPage(id, file.getValue());
            this.pages.add(page);
            this.pagesById.put(id, page);
        }
    }

    /** Maps each page's file name to a way of opening it, in the order the pages appear in the book. */
    private static TreeMap<String, Callable<Reader>> pageFiles() {
        var files = new TreeMap<String, Callable<Reader>>();
        var loader = FabricLoader.getInstance();
        var source = loader.getGameDir().toAbsolutePath().getParent().resolve(SOURCE_FOLDER);
        if (loader.isDevelopmentEnvironment() && Files.isDirectory(source)) {
            try (var paths = Files.list(source)) {
                paths.filter(path -> path.toString().endsWith(PAGE_EXTENSION)).forEach(path ->
                    files.put(pageId(path.getFileName().toString()), () -> Files.newBufferedReader(path, StandardCharsets.UTF_8)));
            } catch (IOException exception) {
                Cataclysm.LOGGER.warn("Could not list journal pages in {}", source, exception);
            }
            return files;
        }
        Minecraft.getInstance().getResourceManager().listResources(FOLDER, file -> file.getPath().endsWith(PAGE_EXTENSION))
            .forEach((location, resource) -> files.put(pageId(location.getPath()), resource::openAsReader));
        return files;
    }

    private static String pageId(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length() - PAGE_EXTENSION.length());
    }

    /** A page that cannot be read still shows up in the book, saying which file needs fixing. */
    private static Page readPage(String id, Callable<Reader> file) {
        try (var reader = file.call()) {
            return Page.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow();
        } catch (Exception exception) {
            Cataclysm.LOGGER.warn("Could not read journal page {}", id, exception);
            var advice = new Block(Block.DEFAULT_X, ERROR_NOTE_Y, Block.DEFAULT_WIDTH, true, List.of(
                "!Este archivo tiene un error.", "Revisa las comas, las comillas y las llaves."));
            return new Page(Optional.empty(), Optional.of("Error"), Optional.of(id.replace('_', ' ')), 1, false, Optional.empty(), List.of(advice));
        }
    }

    void turn(int direction) {
        int target = Mth.clamp(this.spread + direction, 0, this.lastSpread());
        if (target != this.spread) {
            this.turningFrom = this.spread;
            this.spread = target;
            this.turnStartedAt = Util.getMillis();
        }
    }

    boolean hasPrevious() {
        return this.spread > 0;
    }

    boolean hasNext() {
        return this.spread < this.lastSpread();
    }

    private int lastSpread() {
        return (this.pages.size() + 1) / PAGES_PER_SPREAD;
    }

    boolean isOnSeals() {
        return this.spread == 0 && !this.isTurning();
    }

    /** Whether the circle of seals is showing, which it still is while the first leaf turns. */
    boolean showsCircle() {
        return this.isTurning() ? Math.min(this.turningFrom, this.spread) == 0 : this.spread == 0;
    }

    /** The page on one side of a spread. The seals come first, so they are before page zero. */
    private static int pageAt(int spread, int side) {
        return (spread - 1) * PAGES_PER_SPREAD + side;
    }

    private boolean isTurning() {
        if (this.turningFrom >= 0 && Util.getMillis() - this.turnStartedAt >= TURN_MILLIS) {
            this.turningFrom = -1;
        }
        return this.turningFrom >= 0;
    }

    /** The pages lying open: while a leaf turns, the two pages that are not on that leaf. */
    private int[] openPages() {
        if (this.isTurning()) {
            int firstSpread = Math.min(this.turningFrom, this.spread);
            return new int[] {pageAt(firstSpread, 0), pageAt(firstSpread + 1, 1)};
        }
        return new int[] {pageAt(this.spread, 0), pageAt(this.spread, 1)};
    }

    /** Draws the bare book, with the sheets already read stacked on the left and the rest on the right. */
    void drawBook(GuiGraphics graphics, int bookX, int bookY) {
        if (this.showsCircle()) {
            LemegetonTheme.drawBook(graphics, bookX, bookY);
            if (!this.isOnSeals()) {
                LemegetonTheme.drawJournalPage(graphics, bookX + LEAF_X[1], bookY + LEAF_Y, LEAF_X[1], LEAF_Y, LEAF_WIDTH, LEAF_HEIGHT);
            }
        } else {
            LemegetonTheme.drawJournal(graphics, bookX, bookY);
        }
        boolean landed = !this.isTurning() || Util.getMillis() - this.turnStartedAt > TURN_MILLIS / 2;
        int read = landed ? this.spread : this.turningFrom;
        int readSheets = this.lastSpread() == 0 ? 0 : Math.round(MOST_EXTRA_SHEETS * (float) read / this.lastSpread());
        drawSheets(graphics, bookX, bookY, 1 + readSheets, -1);
        drawSheets(graphics, bookX, bookY, 1 + MOST_EXTRA_SHEETS - readSheets, 1);
    }

    /** Draws the edges of a pile of sheets, each one lying a little further out and lower than the last. */
    private static void drawSheets(GuiGraphics graphics, int bookX, int bookY, int sheets, int direction) {
        int outer = direction < 0 ? LEAF_X[0] - 1 : LEAF_X[1] + LEAF_WIDTH;
        int inner = direction < 0 ? LEAF_X[0] + LEAF_WIDTH : LEAF_X[1];
        for (int sheet = 0; sheet < sheets; sheet++) {
            int color = sheet % 2 == 0 ? SHEET_LIGHT : SHEET_DARK;
            int x = bookX + outer + direction * sheet;
            int drop = sheet / SHEETS_PER_DROP;
            int bottom = bookY + LEAF_Y + LEAF_HEIGHT + drop;
            graphics.fill(x, bookY + LEAF_Y + drop, x + 1, bottom + 1, color);
            graphics.fill(Math.min(x, bookX + inner), bottom, Math.max(x + 1, bookX + inner), bottom + 1, color);
        }
    }

    /** Draws the art of the open pages, under the book's buttons. */
    void drawArt(GuiGraphics graphics, int bookX, int bookY, float ink) {
        int[] open = this.openPages();
        for (int side = 0; side < PAGES_PER_SPREAD; side++) {
            if (open[side] >= 0 && open[side] < this.pages.size()) {
                var page = this.pages.get(open[side]);
                var bottom = page.behind.map(this.pagesById::get).orElse(page);
                drawPlate(graphics, bottom, bookX + PAGE_X[side], bookY + PLATE_Y, ink);
            }
        }
    }

    /** Draws the writing of the open pages and, over them, the leaf being turned. */
    void drawWriting(GuiGraphics graphics, Font font, float ink) {
        int[] open = this.openPages();
        for (int side = 0; side < PAGES_PER_SPREAD; side++) {
            if (open[side] >= 0 && open[side] < this.pages.size()) {
                this.drawOpenPage(graphics, font, open[side], side, ink);
            }
        }
        if (this.isTurning()) {
            this.drawLeaf(graphics, font);
        }
    }

    /** A page torn over another one goes on top of that page's writing. */
    private void drawOpenPage(GuiGraphics graphics, Font font, int index, int side, float ink) {
        var page = this.pages.get(index);
        var below = page.behind.map(this.pagesById::get);
        if (below.isPresent()) {
            drawWriting(graphics, font, below.get(), this.pages.indexOf(below.get()), side, ink);
            drawPlate(graphics, page, PAGE_X[side], PLATE_Y, ink);
            drawPageNumber(graphics, font, index, side, LemegetonTheme.faded(LemegetonTheme.PENCIL, ink));
        } else {
            drawWriting(graphics, font, page, index, side, ink);
        }
    }

    /**
     * Turns the leaf between two spreads around the spine. The leaf is cut into vertical strips
     * that each lean at their own angle, so the paper bows as it travels; every strip shows the
     * front or the back of the leaf, with its real page on it, and grows as it rises off the book.
     */
    private void drawLeaf(GuiGraphics graphics, Font font) {
        float time = Mth.clamp((Util.getMillis() - this.turnStartedAt) / TURN_MILLIS, 0, 1);
        float eased = time * time * (3 - 2 * time);
        float turn = Mth.PI * (this.spread > this.turningFrom ? eased : 1 - eased);
        float bend = LEAF_BEND * Mth.sin(turn);
        float[] angle = new float[LEAF_STRIPS];
        float[] across = new float[LEAF_STRIPS + 1];
        float[] lift = new float[LEAF_STRIPS + 1];
        float[] screenX = new float[LEAF_STRIPS + 1];
        screenX[0] = SPINE_X + GUTTER * Mth.cos(turn);
        for (int strip = 0; strip < LEAF_STRIPS; strip++) {
            float along = (strip + 0.5F) / LEAF_STRIPS;
            angle[strip] = Mth.clamp(turn + bend * (along - 0.5F), 0, Mth.PI);
            across[strip + 1] = across[strip] + Mth.cos(angle[strip]) / LEAF_STRIPS;
            lift[strip + 1] = lift[strip] + Mth.sin(angle[strip]) / LEAF_STRIPS;
            screenX[strip + 1] = SPINE_X + (GUTTER * Mth.cos(turn) + LEAF_WIDTH * across[strip + 1]) * (1 + lift[strip + 1] * LEAF_PERSPECTIVE);
        }
        drawLeafShadow(graphics, screenX[LEAF_STRIPS], Mth.sin(turn));
        int firstPage = pageAt(Math.min(this.turningFrom, this.spread), 1);
        for (int strip = 0; strip < LEAF_STRIPS; strip++) {
            boolean front = angle[strip] < Mth.HALF_PI;
            float height = 1 + (lift[strip] + lift[strip + 1]) / 2 * LEAF_PERSPECTIVE;
            this.drawLeafStrip(graphics, font, strip, front, front ? firstPage : firstPage + 1,
                screenX[strip], screenX[strip + 1], height, angle[strip]);
        }
    }

    private void drawLeafStrip(GuiGraphics graphics, Font font, int strip, boolean front, int index,
                               float fromX, float toX, float height, float angle) {
        if (Math.abs(toX - fromX) < 0.5F) {
            return;
        }
        float start = strip / (float) LEAF_STRIPS;
        float end = (strip + 1) / (float) LEAF_STRIPS;
        int side = front ? 1 : 0;
        float pageFrom = LEAF_X[side] + LEAF_WIDTH * (front ? start : 1 - start);
        float pageTo = LEAF_X[side] + LEAF_WIDTH * (front ? end : 1 - end);
        int left = Mth.floor(Math.min(fromX, toX));
        int right = Mth.ceil(Math.max(fromX, toX));
        float centerY = LEAF_Y + LEAF_HEIGHT / 2F;
        graphics.enableScissor(left, -LEAF_Y * 4, right, LEAF_HEIGHT + LEAF_Y * 6);
        graphics.pose().pushPose();
        graphics.pose().translate(fromX, centerY, 0);
        graphics.pose().scale((toX - fromX) / (pageTo - pageFrom), height, 1);
        graphics.pose().translate(-pageFrom, -centerY, 0);
        boolean paper = this.drawLeafFace(graphics, font, index, side);
        graphics.pose().popPose();
        if (paper) {
            int shade = (int) (LEAF_SHADE * (1 - Math.abs(Mth.cos(angle))) * 255);
            int halfHeight = Mth.ceil(LEAF_HEIGHT / 2F * height);
            graphics.fill(left, (int) centerY - halfHeight, right, (int) centerY + halfHeight, shade << 24 | LemegetonTheme.INK & 0xFFFFFF);
        }
        graphics.disableScissor();
    }

    /** Draws one side of the leaf, and says whether it is whole paper. A torn page is only its stub. */
    private boolean drawLeafFace(GuiGraphics graphics, Font font, int index, int side) {
        if (index < 0) {
            LemegetonTheme.drawBookPage(graphics, LEAF_X[side], LEAF_Y, LEAF_WIDTH, LEAF_HEIGHT);
            this.sealPage.accept(graphics);
            return true;
        }
        if (index < this.pages.size() && this.pages.get(index).behind.isPresent()) {
            drawPlate(graphics, this.pages.get(index), PAGE_X[side], PLATE_Y, 1);
            return false;
        }
        LemegetonTheme.drawJournalPage(graphics, LEAF_X[side], LEAF_Y, LEAF_X[side], LEAF_Y, LEAF_WIDTH, LEAF_HEIGHT);
        if (index < this.pages.size()) {
            drawPlate(graphics, this.pages.get(index), PAGE_X[side], PLATE_Y, 1);
            drawWriting(graphics, font, this.pages.get(index), index, side, 1);
        }
        return true;
    }

    /** The shadow the raised leaf throws on the page beyond its free edge. */
    private static void drawLeafShadow(GuiGraphics graphics, float edgeX, float height) {
        int direction = edgeX > SPINE_X ? 1 : -1;
        for (int step = 0; step < SHADOW_STEPS; step++) {
            int near = (int) (edgeX + direction * SHADOW_REACH * height * step / SHADOW_STEPS);
            int far = (int) (edgeX + direction * SHADOW_REACH * height * (step + 1) / SHADOW_STEPS);
            int alpha = (int) (SHADOW_STRENGTH * height * (SHADOW_STEPS - step) / SHADOW_STEPS * 255);
            int left = Mth.clamp(Math.min(near, far), LEAF_X[0], LEAF_X[1] + LEAF_WIDTH);
            int right = Mth.clamp(Math.max(near, far), LEAF_X[0], LEAF_X[1] + LEAF_WIDTH);
            graphics.fill(left, LEAF_Y, right, LEAF_Y + LEAF_HEIGHT, alpha << 24 | LemegetonTheme.INK & 0xFFFFFF);
        }
    }

    private static void drawPlate(GuiGraphics graphics, Page page, int x, int y, float ink) {
        page.art.ifPresent(art -> LemegetonTheme.drawPlate(graphics, Cataclysm.resourceLocation("textures/gui/lemegeton/journal/" + art + ".png"),
            x, y, LemegetonTheme.faded(PLATE_COLOR, ink)));
    }

    private static void drawWriting(GuiGraphics graphics, Font font, Page page, int index, int side, float ink) {
        int x = PAGE_X[side];
        int black = LemegetonTheme.faded(page.dark ? LemegetonTheme.PARCHMENT : LemegetonTheme.INK, ink);
        int red = LemegetonTheme.faded(page.dark ? LemegetonTheme.EMBER : LemegetonTheme.RUBRIC, ink);
        int pencil = LemegetonTheme.faded(page.dark ? LemegetonTheme.ASH : LemegetonTheme.PENCIL, ink);
        page.title.ifPresent(title -> drawTitle(graphics, font, translated(title), x + PAGE_CENTER_X, page.titleSize, red));
        page.subtitle.ifPresent(subtitle -> {
            var text = LemegetonTheme.written(Component.literal(translated(subtitle)));
            int y = PAGE_Y + TITLE_Y + page.titleSize * TITLE_LINE_HEIGHT + SUBTITLE_GAP;
            graphics.drawString(font, text, x + PAGE_CENTER_X - font.width(text) / 2, y, pencil, false);
        });
        for (var block : page.notes) {
            drawBlock(graphics, font, block, x, black, red);
        }
        if (page.title.isPresent()) {
            drawPageNumber(graphics, font, index, side, pencil);
        }
    }

    /** Page numbers count every sheet in the book, whatever the entries written on them are called. */
    private static void drawPageNumber(GuiGraphics graphics, Font font, int index, int side, int color) {
        var number = LemegetonTheme.written(Component.literal(String.valueOf(index + 1)));
        graphics.drawString(font, number, PAGE_X[side] + PAGE_NUMBER_X[side], PAGE_Y + PAGE_NUMBER_Y, color, false);
    }

    private static void drawTitle(GuiGraphics graphics, Font font, String title, int centerX, int size, int color) {
        var text = LemegetonTheme.lettered(Component.literal(title));
        graphics.pose().pushPose();
        graphics.pose().translate(centerX - font.width(text) * size / 2F, PAGE_Y + TITLE_Y, 0);
        graphics.pose().scale(size, size, 1);
        graphics.drawString(font, text, 0, 0, color, false);
        graphics.pose().popPose();
    }

    private static void drawBlock(GuiGraphics graphics, Font font, Block block, int pageX, int black, int red) {
        int y = PAGE_Y + block.y;
        for (String source : block.lines) {
            boolean marked = source.startsWith(RED_MARK);
            String text = translated(marked ? source.substring(RED_MARK.length()) : source);
            boolean attack = LemegetonTheme.isAttack(text);
            int nameColor = red & 0xFFFFFF;
            var written = attack ? LemegetonTheme.rubricated(text, nameColor) : LemegetonTheme.written(Component.literal(text));
            for (var line : font.split(written, block.width)) {
                int offset = block.centered ? (block.width - font.width(line)) / 2 : 0;
                graphics.drawString(font, line, pageX + block.x + offset, y, marked ? red : black, false);
                y += attack ? ATTACK_LINE_HEIGHT : LINE_HEIGHT;
            }
            y += attack ? ATTACK_GAP : LINE_GAP;
        }
    }

    /** Text in a page may be plain words or a language key, so a page can be translated whenever that is needed. */
    private static String translated(String text) {
        return Language.getInstance().getOrDefault(text);
    }

    /** A column of lines that flow down from one spot on the page. */
    private record Block(int x, int y, int width, boolean centered, List<String> lines) {

        private static final int DEFAULT_X = 8;
        private static final int DEFAULT_WIDTH = 144;

        private static final Codec<Block> CODEC = RecordCodecBuilder.create(block -> block.group(
            Codec.INT.optionalFieldOf("x", DEFAULT_X).forGetter(Block::x),
            Codec.INT.fieldOf("y").forGetter(Block::y),
            Codec.INT.optionalFieldOf("width", DEFAULT_WIDTH).forGetter(Block::width),
            Codec.BOOL.optionalFieldOf("centered", false).forGetter(Block::centered),
            Codec.STRING.listOf().fieldOf("lines").forGetter(Block::lines)
        ).apply(block, Block::new));
    }

    /** One page of the journal, exactly as its JSON file spells it. */
    private record Page(Optional<String> art, Optional<String> title, Optional<String> subtitle, int titleSize,
                        boolean dark, Optional<String> behind, List<Block> notes) {

        private static final Codec<Page> CODEC = RecordCodecBuilder.create(page -> page.group(
            Codec.STRING.optionalFieldOf("art").forGetter(Page::art),
            Codec.STRING.optionalFieldOf("title").forGetter(Page::title),
            Codec.STRING.optionalFieldOf("subtitle").forGetter(Page::subtitle),
            Codec.INT.optionalFieldOf("title_size", 1).forGetter(Page::titleSize),
            Codec.BOOL.optionalFieldOf("dark", false).forGetter(Page::dark),
            Codec.STRING.optionalFieldOf("behind").forGetter(Page::behind),
            Block.CODEC.listOf().optionalFieldOf("notes", List.of()).forGetter(Page::notes)
        ).apply(page, Page::new));
    }
}
