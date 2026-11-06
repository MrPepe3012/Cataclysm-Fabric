package team.heather.cataclysm.client.screen.lemegeton;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.item.data.LemegetonComponent;

import java.io.IOException;

/** A manuscript has three colors: the parchment, black ink and red ink for what matters. */
final class LemegetonTheme {

    static final int PARCHMENT = 0xFFDCCEAD;
    static final int INK = 0xFF2A1F18;
    static final int FADED_INK = 0x802A1F18;
    static final int PENCIL = 0xFF6F5C47;
    static final int ASH = 0xFFB3A285;
    static final int EMBER = 0xFFD9542E;
    static final int RUBRIC = 0xFF8E1F1A;
    static final int BRIGHT_RUBRIC = 0xFFB02A22;

    private static final ResourceLocation BOOK = Cataclysm.resourceLocation("textures/gui/lemegeton/book.png");
    private static final ResourceLocation SEALS = Cataclysm.resourceLocation("textures/gui/lemegeton/seals.png");
    private static final ResourceLocation JOURNAL = Cataclysm.resourceLocation("textures/gui/lemegeton/journal.png");
    private static final ResourceLocation JOURNAL_ICONS = Cataclysm.resourceLocation("textures/gui/lemegeton/journal_icons.png");
    private static final ResourceLocation SCRIPT = Cataclysm.resourceLocation("journal_hand");
    private static final ResourceLocation SMALL_SCRIPT = Cataclysm.resourceLocation("journal_small");
    private static final ResourceLocation LETTERING = Cataclysm.resourceLocation("journal_title");
    private static final int BOOK_WIDTH = 352;
    private static final int BOOK_HEIGHT = 192;
    private static final int PLATE_WIDTH = 156;
    private static final int PLATE_HEIGHT = 172;
    private static final int ICON_COUNT = 4;
    static final int ICON_SIZE = 12;
    private static final int SEALS_WIDTH = 124;
    private static final int SEALS_HEIGHT = 68;
    private static final int ENGRAVING_SIZE = 16;

    private static final int OPAQUE_ALPHA = 128;
    private static final int[] DITHER = {0, 8, 2, 10, 12, 4, 14, 6, 3, 11, 1, 9, 15, 7, 13, 5};
    private static final int DITHER_SIZE = 4;
    private static final float DITHER_STEPS = 16;
    private static final float SHADING_STRENGTH = 0.6F;

    /** The font treats a nearly transparent color as opaque, so text never fades all the way out. */
    private static final float MIN_OPACITY = 0.05F;

    private LemegetonTheme() {}

    static int faded(int color, float opacity) {
        int alpha = (int) ((color >>> 24) * Math.max(opacity, MIN_OPACITY));
        return alpha << 24 | color & 0xFFFFFF;
    }

    /** The journal's small, even script. */
    static Component written(MutableComponent text) {
        return text.withStyle(style -> style.withFont(SCRIPT));
    }

    /** An attack is a note that opens with its name, as in "NAME: what it does". */
    static boolean isAttack(String note) {
        return note.indexOf(':') >= 0;
    }

    /** Writes an attack in small script, with its name in red the way a manuscript marks what matters. */
    static Component rubricated(String attack, int nameColor) {
        int nameEnd = attack.indexOf(':') + 1;
        return Component.empty()
            .append(Component.literal(attack.substring(0, nameEnd)).withColor(nameColor))
            .append(attack.substring(nameEnd))
            .withStyle(style -> style.withFont(SMALL_SCRIPT));
    }

    /** The journal's page titles, always in capitals. */
    static Component lettered(MutableComponent text) {
        return text.withStyle(style -> style.withFont(LETTERING));
    }

    static void drawBook(GuiGraphics graphics, int x, int y) {
        graphics.blit(RenderType::guiTextured, BOOK, x, y, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, BOOK_WIDTH, BOOK_HEIGHT);
    }

    static void drawJournal(GuiGraphics graphics, int x, int y) {
        graphics.blit(RenderType::guiTextured, JOURNAL, x, y, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, BOOK_WIDTH, BOOK_HEIGHT);
    }

    /** Draws one bare page of the journal, cut from the book at (u, v), so a leaf can carry it. */
    static void drawJournalPage(GuiGraphics graphics, int x, int y, int u, int v, int width, int height) {
        graphics.blit(RenderType::guiTextured, JOURNAL, x, y, u, v, width, height, BOOK_WIDTH, BOOK_HEIGHT);
    }

    /** Draws one bare page of the seals where it lies in the book, for a leaf that has been lifted off it. */
    static void drawBookPage(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.blit(RenderType::guiTextured, BOOK, x, y, x, y, width, height, BOOK_WIDTH, BOOK_HEIGHT);
    }

    /** Draws a full journal plate, faded by the color's alpha. */
    static void drawPlate(GuiGraphics graphics, ResourceLocation plate, int x, int y, int color) {
        graphics.blit(RenderType::guiTextured, plate, x, y, 0, 0, PLATE_WIDTH, PLATE_HEIGHT, PLATE_WIDTH, PLATE_HEIGHT, color);
    }

    static void drawJournalIcon(GuiGraphics graphics, int icon, int x, int y, int color) {
        graphics.blit(RenderType::guiTextured, JOURNAL_ICONS, x, y, icon * ICON_SIZE, 0, ICON_SIZE, ICON_SIZE,
            ICON_COUNT * ICON_SIZE, ICON_SIZE, color);
    }

    /** Draws a white region of the seal sheet tinted with one of the colors above. */
    static void drawSeal(GuiGraphics graphics, int x, int y, int u, int v, int size, int color) {
        graphics.blit(RenderType::guiTextured, SEALS, x, y, u, v, size, size, SEALS_WIDTH, SEALS_HEIGHT, color);
    }

    static void drawEngraving(GuiGraphics graphics, ResourceLocation engraving, int x, int y, int size, int color) {
        graphics.blit(RenderType::guiTextured, engraving, x, y, 0, 0, size, size, size, size, color);
    }

    /**
     * Redraws the mob effect's own sprite as a white ink engraving, ready to be tinted.
     * It reads the live resource, so resource packs and sprites from other mods are respected.
     */
    static ResourceLocation engrave(LemegetonComponent.Upgrade upgrade) {
        var effect = upgrade.effect().unwrapKey().orElseThrow().location();
        var sprite = effect.withPath(path -> "textures/mob_effect/" + path + ".png");
        var engraving = Cataclysm.resourceLocation("lemegeton_engraving/" + upgrade.getSerializedName());
        var client = Minecraft.getInstance();
        try (var stream = client.getResourceManager().open(sprite);
             var source = NativeImage.read(stream);
             var centered = centered(source)) {
            client.getTextureManager().register(engraving, new DynamicTexture(engraving::toString, engrave(centered)));
        } catch (IOException exception) {
            Cataclysm.LOGGER.warn("Could not engrave {}", sprite, exception);
        }
        return engraving;
    }

    /**
     * Moves the sprite's drawing to the middle of a canvas sized in multiples of
     * {@link #ENGRAVING_SIZE} and cuts it to the seal's circle. Animated strips use their first frame.
     */
    private static NativeImage centered(NativeImage source) {
        int size = source.getWidth();
        int minX = size;
        int minY = size;
        int maxX = 0;
        int maxY = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (isOpaque(source, x, y, size)) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        int canvas = Math.max(size - size % ENGRAVING_SIZE, ENGRAVING_SIZE);
        int offsetX = (canvas - (maxX - minX + 1)) / 2 - minX;
        int offsetY = (canvas - (maxY - minY + 1)) / 2 - minY;
        float radius = canvas / 2F;
        var centered = new NativeImage(canvas, canvas, true);
        for (int y = 0; y < canvas; y++) {
            for (int x = 0; x < canvas; x++) {
                int sourceX = x - offsetX;
                int sourceY = y - offsetY;
                boolean inCircle = Mth.length(x + 0.5F - radius, y + 0.5F - radius) <= radius;
                if (inCircle && sourceX >= 0 && sourceY >= 0 && sourceX < size && sourceY < size) {
                    centered.setPixel(x, y, source.getPixel(sourceX, sourceY));
                }
            }
        }
        return centered;
    }

    /** Keeps the silhouette as a line and turns the shading into dithered ink. */
    private static NativeImage engrave(NativeImage source) {
        int size = source.getWidth();
        float darkest = 1;
        float brightest = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (isOpaque(source, x, y, size)) {
                    float brightness = brightness(source.getPixel(x, y));
                    darkest = Math.min(darkest, brightness);
                    brightest = Math.max(brightest, brightness);
                }
            }
        }
        float range = Math.max(brightest - darkest, 0.01F);
        var engraving = new NativeImage(size, size, true);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (!isOpaque(source, x, y, size)) {
                    continue;
                }
                float shade = SHADING_STRENGTH * (1 - (brightness(source.getPixel(x, y)) - darkest) / range);
                float threshold = DITHER[y % DITHER_SIZE * DITHER_SIZE + x % DITHER_SIZE] / DITHER_STEPS;
                if (isEdge(source, x, y, size) || shade > threshold) {
                    engraving.setPixel(x, y, 0xFFFFFFFF);
                }
            }
        }
        return engraving;
    }

    private static boolean isEdge(NativeImage source, int x, int y, int size) {
        return !isOpaque(source, x - 1, y, size) || !isOpaque(source, x + 1, y, size)
            || !isOpaque(source, x, y - 1, size) || !isOpaque(source, x, y + 1, size);
    }

    private static boolean isOpaque(NativeImage source, int x, int y, int size) {
        return x >= 0 && y >= 0 && x < size && y < size && ARGB.alpha(source.getPixel(x, y)) >= OPAQUE_ALPHA;
    }

    private static float brightness(int color) {
        return (0.3F * ARGB.red(color) + 0.59F * ARGB.green(color) + 0.11F * ARGB.blue(color)) / 255;
    }
}
