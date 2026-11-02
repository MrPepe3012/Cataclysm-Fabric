package team.heather.cataclysm.item.arcane;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import team.heather.cataclysm.registry.item.EquipmentEnchantments;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArcaneEnchantmentsTest {
    @ParameterizedTest
    @CsvSource({"0,0", "1,2", "2,4", "3,6", "4,8", "5,10"})
    void powerUsesDoubleTheAppliedLevel(int applied, int effective) {
        assertEquals(effective, EquipmentEnchantments.powerLevel(applied));
    }

    @ParameterizedTest
    @CsvSource({"0,0", "1,3", "2,4", "3,5"})
    void riptideAddsTwoLevelsOnlyWhenEnchanted(int applied, int effective) {
        assertEquals(effective, EquipmentEnchantments.riptideLevel(applied));
    }
}
