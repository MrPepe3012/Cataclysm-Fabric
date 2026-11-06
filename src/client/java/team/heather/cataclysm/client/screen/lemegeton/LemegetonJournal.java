package team.heather.cataclysm.client.screen.lemegeton;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import team.heather.cataclysm.Cataclysm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * The journal half of the Lemegeton. Its pages are data, not code: each one is a
 * JSON file in {@code assets/cataclysm/lemegeton_journal}, read in filename order
 * every time the book opens. The readme in that folder explains the format.
 */
final class LemegetonJournal {

    private static final String FOLDER = "lemegeton_journal";
    private static final int PAGES_PER_SPREAD = 2;
    private static final int[] PAGE_X = {10, 186};
    private static final int[] PAGE_NUMBER_X = {137, 14};
    private static final int PAGE_Y = 10;
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

    private final List<Page> pages = new ArrayList<>();
    private final Map<String, Page> pagesById = new HashMap<>();
    private int spread;

    LemegetonJournal() {
        var files = Minecraft.getInstance().getResourceManager()
            .listResources(FOLDER, file -> file.getPath().endsWith(".json"));
        for (var file : new TreeMap<>(files).entrySet()) {
            try (var reader = file.getValue().openAsReader()) {
                var page = Page.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow();
                String path = file.getKey().getPath();
                this.pages.add(page);
                this.pagesById.put(path.substring(path.lastIndexOf('/') + 1, path.length() - ".json".length()), page);
            } catch (IOException | RuntimeException exception) {
                Cataclysm.LOGGER.warn("Could not read journal page {}", file.getKey(), exception);
            }
        }
    }

    void turn(int direction) {
        this.spread = Mth.clamp(this.spread + direction, 0, this.lastSpread());
    }

    boolean hasPrevious() {
        return this.spread > 0;
    }

    boolean hasNext() {
        return this.spread < this.lastSpread();
    }

    private int lastSpread() {
        return Math.max(this.pages.size() - 1, 0) / PAGES_PER_SPREAD;
    }

    /** Draws the art of the open spread, under the book's buttons. */
    void drawArt(GuiGraphics graphics, int bookX, int bookY, float ink) {
        for (int side = 0; side < PAGES_PER_SPREAD; side++) {
            int index = this.spread * PAGES_PER_SPREAD + side;
            if (index < this.pages.size()) {
                var page = this.pages.get(index);
                var bottom = page.behind.map(this.pagesById::get).orElse(page);
                drawPlate(graphics, bottom, bookX + PAGE_X[side], bookY + PAGE_Y, ink);
            }
        }
    }

    /** Draws the writing of the open spread. A page torn over another one goes on top of that page's writing. */
    void drawWriting(GuiGraphics graphics, Font font, float ink) {
        for (int side = 0; side < PAGES_PER_SPREAD; side++) {
            int index = this.spread * PAGES_PER_SPREAD + side;
            if (index >= this.pages.size()) {
                continue;
            }
            var page = this.pages.get(index);
            var below = page.behind.map(this.pagesById::get);
            if (below.isPresent()) {
                drawWriting(graphics, font, below.get(), this.pages.indexOf(below.get()), side, ink);
                drawPlate(graphics, page, PAGE_X[side], PAGE_Y, ink);
            } else {
                drawWriting(graphics, font, page, index, side, ink);
            }
        }
    }

    private static void drawPlate(GuiGraphics graphics, Page page, int x, int y, float ink) {
        var plate = Cataclysm.resourceLocation("textures/gui/lemegeton/journal/" + page.art + ".png");
        LemegetonTheme.drawPlate(graphics, plate, x, y, LemegetonTheme.faded(PLATE_COLOR, ink));
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
            var number = LemegetonTheme.written(Component.literal(String.valueOf(index + 1)));
            graphics.drawString(font, number, x + PAGE_NUMBER_X[side], PAGE_Y + PAGE_NUMBER_Y, pencil, false);
        }
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
    private record Page(String art, Optional<String> title, Optional<String> subtitle, int titleSize,
                        boolean dark, Optional<String> behind, List<Block> notes) {

        private static final Codec<Page> CODEC = RecordCodecBuilder.create(page -> page.group(
            Codec.STRING.fieldOf("art").forGetter(Page::art),
            Codec.STRING.optionalFieldOf("title").forGetter(Page::title),
            Codec.STRING.optionalFieldOf("subtitle").forGetter(Page::subtitle),
            Codec.INT.optionalFieldOf("title_size", 1).forGetter(Page::titleSize),
            Codec.BOOL.optionalFieldOf("dark", false).forGetter(Page::dark),
            Codec.STRING.optionalFieldOf("behind").forGetter(Page::behind),
            Block.CODEC.listOf().optionalFieldOf("notes", List.of()).forGetter(Page::notes)
        ).apply(page, Page::new));
    }
}
