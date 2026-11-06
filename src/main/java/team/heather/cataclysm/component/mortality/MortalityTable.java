package team.heather.cataclysm.component.mortality;

import team.heather.cataclysm.component.level.phase.Phase;

import java.util.Comparator;
import java.util.List;

public final class MortalityTable {

    public static final MortalityTable VANILLA = of(entry(Phase.PHASE_1, 0.01F), entry(Phase.PHASE_4, 0.02F), entry(Phase.PHASE_5, 0.10F), entry(Phase.PANTHEON, 1.0F));
    public static final MortalityTable ARCANE = of(entry(Phase.PHASE_3, 0.005F), entry(Phase.PHASE_4, 0.01F), entry(Phase.PHASE_5, 0.05F), entry(Phase.PANTHEON, 0.5F));
    public static final MortalityTable CALAMITY = of(entry(Phase.PHASE_4, 0.005F), entry(Phase.PHASE_5, 0.02F), entry(Phase.PANTHEON, 0.10F));
    public static final MortalityTable PARAGON = of();

    private final List<Entry> entries;

    private MortalityTable(Entry... entries) {
        this.entries = List.of(entries);
    }

    public float getValue(Phase phase) {
        return this.entries.stream()
                .filter(entry -> phase.isAtLeast(entry.fromPhase()))
                .max(Comparator.comparing(Entry::fromPhase))
                .map(Entry::mortality)
                .orElse(0.0F);
    }

    private static MortalityTable of(Entry... entries) {
        return new MortalityTable(entries);
    }

    private static Entry entry(Phase fromPhase, float mortality) {
        return new Entry(fromPhase, mortality);
    }

    public record Entry(Phase fromPhase, float mortality) {}
}
