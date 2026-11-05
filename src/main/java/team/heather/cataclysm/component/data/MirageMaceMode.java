package team.heather.cataclysm.component.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/** What the Mirage Mace launches on use, toggled by sneak-use. */
public enum MirageMaceMode implements StringRepresentable {

    WIND,
    HOOK,

    ;

    public static final Codec<MirageMaceMode> CODEC = StringRepresentable.fromEnum(MirageMaceMode::values);

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
