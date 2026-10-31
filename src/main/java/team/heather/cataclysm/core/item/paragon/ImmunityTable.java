package team.heather.cataclysm.core.item.paragon;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Immunity duration granted by a paragon item, by week. Each entry applies from its
 * week until the next one; weeks before the first entry grant no immunity.
 */
public final class ImmunityTable {

    public static final ImmunityTable PARAGON_BLESSING = immunityTable()
            .from(1, 15)
            .from(5, 7.5)
            .from(6, 2);

    public static final ImmunityTable PARAGON_PEARL = immunityTable()
            .from(1, 7)
            .from(5, 3.5)
            .from(6, 1.25);

    public static final ImmunityTable MIRAGE_BLESSING = immunityTable()
            .from(1, 20);

    public static final ImmunityTable PARAGON_TOTEM = immunityTable(7.5);

    private final NavigableMap<Integer, Double> seconds;

    private ImmunityTable(NavigableMap<Integer, Double> seconds) {
        this.seconds = seconds;
    }

    /** Starts an empty table; add entries with {@link #from(int, double)}. */
    public static ImmunityTable immunityTable() {
        return new ImmunityTable(new TreeMap<>());
    }

    /** A table that grants the same {@code seconds} every week. */
    public static ImmunityTable immunityTable(double seconds) {
        return immunityTable().from(Integer.MIN_VALUE, seconds);
    }

    /** Converts seconds to game ticks, rounding to the nearest tick. */
    public static int toTicks(double seconds) {
        return Math.toIntExact(Math.round(seconds * 20));
    }

    /**
     * Returns a copy where {@code seconds} of immunity apply from {@code week} onwards.
     * Weeks must be strictly increasing so every week maps to exactly one duration.
     */
    public ImmunityTable from(int week,
                              double seconds) {
        var next = new TreeMap<>(this.seconds);
        next.put(week, seconds);
        return new ImmunityTable(next);
    }

    public double getSeconds(int week) {
        var entry = this.seconds.floorEntry(week);
        return entry != null ? entry.getValue() : 0;
    }

    public int getTicks(int week) {
        return toTicks(this.getSeconds(week));
    }
}
