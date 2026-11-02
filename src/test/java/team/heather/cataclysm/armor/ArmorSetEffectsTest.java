package team.heather.cataclysm.armor;

import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArmorSetEffectsTest {
    @BeforeAll
    static void bootstrap() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void repeatedTicksAndSetChangesNeverStackTheHealthBonus() {
        var health = new AttributeInstance(Attributes.MAX_HEALTH, ignored -> {});
        health.setBaseValue(20);
        assertTrue(ArmorSetEffects.updateMaxHealth(health, true));
        for (int tick = 0; tick < 100; tick++) {
            assertFalse(ArmorSetEffects.updateMaxHealth(health, true));
        }
        assertEquals(24, health.getValue());
        assertEquals(1, health.getModifiers().size());
        assertTrue(health.getPermanentModifiers().isEmpty());
    }

    @Test
    void removingAnIncompleteSetPreservesOtherHealthModifiers() {
        var health = new AttributeInstance(Attributes.MAX_HEALTH, ignored -> {});
        health.setBaseValue(20);
        var other = new AttributeModifier(ResourceLocation.fromNamespaceAndPath("test", "other_health"),
                6, AttributeModifier.Operation.ADD_VALUE);
        health.addPermanentModifier(other);
        ArmorSetEffects.updateMaxHealth(health, true);
        assertEquals(30, health.getValue());
        assertTrue(ArmorSetEffects.updateMaxHealth(health, false));
        assertEquals(26, health.getValue());
        assertTrue(health.hasModifier(other.id()));
        assertFalse(ArmorSetEffects.updateMaxHealth(health, false));
    }

    @Test
    void anIncompleteSetGrantsNoHealthBonus() {
        var health = new AttributeInstance(Attributes.MAX_HEALTH, ignored -> {});
        health.setBaseValue(20);
        assertFalse(ArmorSetEffects.updateMaxHealth(health, false));
        assertEquals(20, health.getValue());
    }
}
