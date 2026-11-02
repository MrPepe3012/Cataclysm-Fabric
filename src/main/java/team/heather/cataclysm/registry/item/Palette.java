package team.heather.cataclysm.registry.item;

/** Shared name colors and slightly brighter description colors. */
public enum Palette {

    PARAGON(0xc6a96e),
    LEMEGETON(0xcdcdcd),
    ENDER(0x5d368a),
    TWISTED(0x6c5b9a),
    ARCANE(0xd5b56e),
    CALAMITY(0xcaa207),
    MIDWAY(0xffc158, 0xffdb6c),
    MIRAGE(0x9649a6),
    PALE(0xd1d1d1),
    TECTONITE(0x82878c),
    ONYX(0x505050),
    VOID_STONE(0x949494),
    ARCANE_MATERIAL(0xd19d4d),
    VOID_HEART(0x46424a),
    UR_TEAR(0xada7a5),
    TOXIC(0x4d694f),
    GOLDEN(0xc2b25f),
    NIGHTMARE(0x363636),
    CATACLYST(0xbababa),
    GUARDIAN(0x828c8b),
    PALE_REMNANT(0xc4c4c4),
    LLAMA(0xc4c4b9),
    ELDER(0x5e3c2a),
    GOLEM(0x9b8d75),
    DROWNED(0x6f8c8e),
    WANDERING_HEART(0x7a9ca1),
    WANDERING_SOUL(0xb5a6d3),
    CHALICE(0xd5b56e),
    UPGRADE(0x935151);

    private final int base;
    private final int alternate;

    Palette(int base) {
        this.base = base;
        int r = (base >> 16) & 255;
        int g = (base >> 8) & 255;
        int b = base & 255;
        this.alternate = (r + (255 - r) / 5) << 16
                | (g + (255 - g) / 5) << 8 | (b + (255 - b) / 5);
    }

    Palette(int base, int alternate) {
        this.base = base;
        this.alternate = alternate;
    }

    public int base() {
        return this.base;
    }

    public int alternate() {
        return this.alternate;
    }
}
