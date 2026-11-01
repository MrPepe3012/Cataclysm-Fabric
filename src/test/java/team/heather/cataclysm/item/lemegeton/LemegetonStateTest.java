package team.heather.cataclysm.item.lemegeton;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.SharedConstants;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import team.heather.cataclysm.registry.item.lemegeton.Lemegeton;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonItem;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonState;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonUpgrade;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LemegetonStateTest {
    @BeforeAll
    static void bootstrap() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void stateRoundTripsWithoutPlayerIdentity() {
        var state = new LemegetonState(Map.of(LemegetonUpgrade.SPEED, 2, LemegetonUpgrade.HEALTH_BLESSING, 3), 14500, 3);
        var json = LemegetonState.CODEC.encodeStart(JsonOps.INSTANCE, state).getOrThrow();
        assertEquals(state, LemegetonState.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow());
        assertEquals(5, state.totalLevels());
        assertFalse(json.toString().contains("player"));
        assertEquals(500, state.remainingCooldown(14000));
        assertEquals(0, state.remainingCooldown(15000));
    }

    @Test
    void stateSurvivesNetworkSerialization() {
        var state = LemegetonState.EMPTY.upgrade(LemegetonUpgrade.CLEANSING).withCooldown(8000).withBonusSlots(2);
        var codec = ByteBufCodecs.fromCodec(LemegetonState.CODEC);
        var buffer = Unpooled.buffer();
        try {
            codec.encode(buffer, state);
            assertEquals(state, codec.decode(buffer));
        } finally {
            buffer.release();
        }
    }

    @Test
    void upgradingOneBookLeavesOtherBooksAndItsPreviousStateUntouched() {
        var original = LemegetonState.EMPTY;
        var first = original.upgrade(LemegetonUpgrade.REGENERATION).withCooldown(9000);
        var second = original.upgrade(LemegetonUpgrade.SPEED);
        assertFalse(original.hasUpgrades());
        assertEquals(0, second.level(LemegetonUpgrade.REGENERATION));
        assertEquals(0, first.level(LemegetonUpgrade.SPEED));
        assertEquals(0, second.cooldownUntil());
        assertThrows(UnsupportedOperationException.class, () -> first.upgrades().clear());
    }

    @ParameterizedTest
    @CsvSource({"1,2,1", "2,4,2", "3,6,2", "4,8,3", "8,16,3"})
    void weeklyLimitAndPaymentTierFollowTheReference(int week, int capacity, int tier) {
        assertEquals(capacity, Lemegeton.capacity(week, LemegetonState.EMPTY));
        assertEquals(tier, Lemegeton.requiredTier(week));
    }

    @Test
    void incursionSlotsAreOwnedByTheBookAndCapacityDoesNotOverflow() {
        assertEquals(7, Lemegeton.capacity(2, LemegetonState.EMPTY.withBonusSlots(3)));
        assertEquals(4, Lemegeton.capacity(2, LemegetonState.EMPTY));
        assertEquals(Integer.MAX_VALUE, Lemegeton.capacity(Integer.MAX_VALUE,
                LemegetonState.EMPTY.withBonusSlots(Integer.MAX_VALUE)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"upgrades\":{\"jump_boost\":2}}", "{\"upgrades\":{\"cleansing\":3}}",
            "{\"upgrades\":{\"speed\":0}}", "{\"upgrades\":{\"unknown\":1}}", "{\"cooldown_until\":-1}", "{\"bonus_slots\":-1}"})
    void malformedComponentsAreRejected(String json) {
        assertTrue(LemegetonState.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json)).isError());
    }

    @Test
    void upgradesStopAtTheirOwnMaxLevelAndKeepTheExistingCooldown() {
        var state = LemegetonState.EMPTY.withCooldown(4000).upgrade(LemegetonUpgrade.CLEANSING)
                .upgrade(LemegetonUpgrade.CLEANSING);
        assertEquals(4000, state.cooldownUntil());
        assertThrows(IllegalStateException.class, () -> state.upgrade(LemegetonUpgrade.CLEANSING));
        assertThrows(IllegalArgumentException.class, () -> new LemegetonState(Map.of(LemegetonUpgrade.JUMP_BOOST, 2), 0, 0));
    }

    @Test
    void effectAmplifiersAndCatalogueSlotsMatchTheReference() {
        assertEquals(14, LemegetonUpgrade.values().length);
        assertEquals(2, LemegetonUpgrade.SPEED.effectLevel(1));
        assertEquals(4, LemegetonUpgrade.SPEED.effectLevel(2));
        assertEquals(7, LemegetonUpgrade.HEALTH_BLESSING.effectLevel(3));
        assertEquals(14, java.util.Arrays.stream(LemegetonUpgrade.values()).map(LemegetonUpgrade::slot).distinct().count());
        assertEquals(600, LemegetonItem.EFFECT_DURATION_TICKS);
        assertEquals(6000, LemegetonItem.COOLDOWN_TICKS);
    }
}
