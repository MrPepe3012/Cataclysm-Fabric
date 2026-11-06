package team.heather.cataclysm.item.data;

import com.mojang.serialization.Codec;

/** Stored as its number, so a tier can only be added after the last one. */
public enum UpgradeTierComponent {

    FIRST,
    SECOND,
    THIRD;

    public static final Codec<UpgradeTierComponent> CODEC = Codec.intRange(1, values().length)
        .xmap(UpgradeTierComponent::of, UpgradeTierComponent::tier);

    /** The closest tier, since a menu reports zero until it syncs. */
    public static UpgradeTierComponent of(int tier) {
        return values()[Math.clamp(tier - 1L, 0, values().length - 1)];
    }

    public int tier() {
        return this.ordinal() + 1;
    }
}
