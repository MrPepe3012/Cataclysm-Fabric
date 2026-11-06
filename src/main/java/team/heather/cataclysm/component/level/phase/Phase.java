package team.heather.cataclysm.component.level.phase;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Phase implements StringRepresentable {

    PHASE_1,
    PHASE_2,
    PHASE_3,
    PHASE_4,
    PHASE_5,
    PANTHEON,
    FINALE;

    public static final StringRepresentable.EnumCodec<Phase> CODEC = StringRepresentable.fromEnum(Phase::values);

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public boolean isAtLeast(Phase other) {
        return this.compareTo(other) >= 0;
    }
}