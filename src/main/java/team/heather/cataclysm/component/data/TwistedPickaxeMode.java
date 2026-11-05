package team.heather.cataclysm.component.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/** The enchantment level the Twisted Pickaxe mines with, toggled by sneak-use. */
public enum TwistedPickaxeMode implements StringRepresentable {

    FORTUNE(5, 0),
    SILK_TOUCH(0, 1),

    ;

    public static final Codec<TwistedPickaxeMode> CODEC = StringRepresentable.fromEnum(TwistedPickaxeMode::values);

    private final int fortuneLevel;
    private final int silkTouchLevel;

    TwistedPickaxeMode(int fortuneLevel,
                       int silkTouchLevel) {
        this.fortuneLevel = fortuneLevel;
        this.silkTouchLevel = silkTouchLevel;
    }

    public TwistedPickaxeMode toggled() {
        return this == FORTUNE
                ? SILK_TOUCH
                : FORTUNE;
    }

    public int fortuneLevel() {
        return this.fortuneLevel;
    }

    public int silkTouchLevel() {
        return this.silkTouchLevel;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
