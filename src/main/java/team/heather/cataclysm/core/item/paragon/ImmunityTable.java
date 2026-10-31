package team.heather.cataclysm.core.item.paragon;

import java.util.NavigableMap;
import java.util.TreeMap;

public final class ImmunityTable {

    public static final ImmunityTable PARAGON_BLESSING = immunityTable().from(1, 15).from(5, 7.5).from(6, 2);
    public static final ImmunityTable PARAGON_PEARL = immunityTable().from(1, 7).from(5, 3.5).from(6, 1.25);
    public static final ImmunityTable MIRAGE_BLESSING = immunityTable().from(1, 20);
    public static final ImmunityTable PARAGON_TOTEM = immunityTable(7.5);

    private final NavigableMap<Integer, Double> seconds;

    private ImmunityTable(NavigableMap<Integer, Double> seconds) {
        this.seconds = seconds;
    }

    public static ImmunityTable immunityTable() {
        return new ImmunityTable(new TreeMap<>());
    }

    public static ImmunityTable immunityTable(double seconds) {
        return immunityTable().from(Integer.MIN_VALUE, seconds);
    }

    public static int toTicks(double seconds) {
        return Math.toIntExact(Math.round(seconds * 20));
    }

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
