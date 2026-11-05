package team.heather.cataclysm.client.screen.lemegeton;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

/** Red sparks that rise from a seal the moment a pact is made. */
final class Embers {

    private static final int COUNT = 16;
    private static final float LIFE_MILLIS = 900;
    private static final float SPREAD = 14;
    private static final float DRIFT = 10;
    private static final float RISE = 26;

    private final RandomSource random = RandomSource.create();
    private final List<Ember> embers = new ArrayList<>();

    void burst(int x, int y) {
        long now = Util.getMillis();
        for (int i = 0; i < COUNT; i++) {
            this.embers.add(new Ember(
                x + this.spread(SPREAD),
                y + this.spread(SPREAD),
                this.spread(DRIFT),
                RISE * (1 + this.random.nextFloat()),
                now + this.random.nextInt(150)));
        }
    }

    void render(GuiGraphics graphics) {
        long now = Util.getMillis();
        this.embers.removeIf(ember -> now - ember.bornAt > LIFE_MILLIS);
        for (var ember : this.embers) {
            float age = Math.max(0, (now - ember.bornAt) / LIFE_MILLIS);
            int x = Math.round(ember.x + ember.drift * age);
            int y = Math.round(ember.y - ember.rise * age);
            graphics.fill(x, y, x + 2, y + 2, LemegetonTheme.faded(LemegetonTheme.BRIGHT_RUBRIC, 1 - age));
        }
    }

    private float spread(float range) {
        return (this.random.nextFloat() - 0.5F) * range;
    }

    private record Ember(float x, float y, float drift, float rise, long bornAt) {}
}
