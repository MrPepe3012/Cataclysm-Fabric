package com.cataclysm.item.totem.mortality;

import java.util.Comparator;
import java.util.List;

public final class MortalityTable {
    public static final MortalityTable VANILLA = of(entry(1, 1.0F), entry(4, 2.0F), entry(5, 10.0F), entry(6, 100.0F));
    public static final MortalityTable ARCANE = of(entry(3, 0.5F), entry(4, 1.0F), entry(5, 5.0F), entry(6, 50.0F));
    public static final MortalityTable CALAMITY = of(entry(4, 0.5F), entry(5, 2.0F), entry(6, 10.0F));
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

    public static MortalityTable of(Entry... entries) {
        return new MortalityTable(entries);
    }

    public static Entry entry(int fromWeek, float value) {
        return new Entry(fromWeek, value);
    }
}