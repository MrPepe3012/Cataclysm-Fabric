package team.heather.cataclysm.item.feature.paragon;

import team.heather.cataclysm.component.level.phase.Phase;

import java.util.NavigableMap;
import java.util.TreeMap;

public final class ImmunityTable {

    public static final ImmunityTable PARAGON_BLESSING = immunityTable().from(Phase.PHASE_1, 15).from(Phase.PHASE_5, 7.5).from(Phase.PANTHEON, 2);
    public static final ImmunityTable PARAGON_PEARL = immunityTable().from(Phase.PHASE_1, 7).from(Phase.PHASE_5, 3.5).from(Phase.PANTHEON, 1.25);
    public static final ImmunityTable MIRAGE_BLESSING = immunityTable().from(Phase.PHASE_1, 20);
    public static final ImmunityTable PARAGON_TOTEM = immunityTable(7.5);

    private final NavigableMap<Phase, Double> seconds;

    private ImmunityTable(NavigableMap<Phase, Double> seconds) {
        this.seconds = seconds;
    }

    public static ImmunityTable immunityTable() {
        return new ImmunityTable(new TreeMap<>());
    }

    public static ImmunityTable immunityTable(double seconds) {
        return immunityTable().from(Phase.PHASE_1, seconds);
    }

    public static int toTicks(double seconds) {
        return Math.toIntExact(Math.round(seconds * 20));
    }

    public ImmunityTable from(Phase phase,
                              double seconds) {
        var next = new TreeMap<>(this.seconds);
        next.put(phase, seconds);
        return new ImmunityTable(next);
    }

    public double getSeconds(Phase phase) {
        var entry = this.seconds.floorEntry(phase);
        return entry != null ? entry.getValue() : 0;
    }

    public int getTicks(Phase phase) {
        return toTicks(this.getSeconds(phase));
    }
}
