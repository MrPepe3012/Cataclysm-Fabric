package team.heather.cataclysm.item;

/** Shared item name colors. */
public enum ItemPalette {

    PARAGON("#C6A96E"),
    LEMEGETON("#CDCDCD"),
    ENDER("#5D368A"),
    TWISTED("#6C5B9A"),
    ARCANE("#D5B56E"),
    CALAMITY("#CAA207"),
    MIDWAY("#FFC158"),
    MIRAGE("#9649A6"),
    PALE("#D1D1D1"),
    TECTONITE("#82878C"),
    ONYX("#505050"),
    VOID_STONE("#949494"),
    ARCANE_MATERIAL("#D19D4D"),
    VOID_HEART("#46424A"),
    UR_TEAR("#ADA7A5"),
    TOXIC("#4D694F"),
    GOLDEN("#C2B25F"),
    NIGHTMARE("#363636"),
    CATACLYST("#BABABA"),
    GUARDIAN("#828C8B"),
    PALE_REMNANT("#C4C4C4"),
    LLAMA("#C4C4B9"),
    ELDER("#5E3C2A"),
    GOLEM("#9B8D75"),
    DROWNED("#6F8C8E"),
    WANDERING_HEART("#7A9CA1"),
    WANDERING_SOUL("#B5A6D3"),
    CHALICE("#D5B56E"),
    UPGRADE("#935151");

    private final int base;

    ItemPalette(String base) {
        this.base = rgb(base);
    }

    private static int rgb(String html) {
        return Integer.parseInt(html.substring(1), 16);
    }

    public int base() {
        return this.base;
    }
}
