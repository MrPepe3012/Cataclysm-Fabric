package com.cataclysm.registry.item.totem.mortality;

import java.util.Comparator;
import java.util.List;

public final class MortalityTable {
    public static final MortalityTable VANILLA = of(entry(1, 0.01F), entry(4, 0.02F), entry(5, 0.10F), entry(6, 1.0F));
    public static final MortalityTable ARCANE = of(entry(3, 0.005F), entry(4, 0.01F), entry(5, 0.05F), entry(6, 0.5F));
    public static final MortalityTable CALAMITY = of(entry(4, 0.005F), entry(5, 0.02F), entry(6, 0.10F));
    public static final MortalityTable PARAGON = of();

    private final List<Entry> entries;

    private MortalityTable(Entry... entries) {
        this.entries = List.of(entries);
    }

    public float getValue(int week) {
        return this.entries.stream()
                .filter(entry -> week >= entry.fromWeek())
                .max(Comparator.comparingInt(Entry::fromWeek))
                .or(() -> this.entries.stream().min(Comparator.comparingInt(Entry::fromWeek)))
                .map(Entry::value)
                .orElse(0.0F);
    }

    public record Entry(int fromWeek, float value) {}

    private static MortalityTable of(Entry... entries) {
        return new MortalityTable(entries);
    }

    private static Entry entry(int fromWeek, float value) {
        return new Entry(fromWeek, value);
    }
}