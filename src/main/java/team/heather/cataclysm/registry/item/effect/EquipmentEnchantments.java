package team.heather.cataclysm.registry.item.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import team.heather.cataclysm.registry.item.CataclysmItems;

/** Items that behave as if they carried a stronger enchantment than the one they hold. */
public final class EquipmentEnchantments {

    private EquipmentEnchantments() {}

    public static int effectiveLevel(ItemStack stack, Holder<Enchantment> enchantment, int level) {
        if (level <= 0) {
            return 0;
        }
        if (enchantment.is(Enchantments.POWER)) {
            if (stack.is(CataclysmItems.ARCANE_BOW)) {
                return level * 2;
            }
            if (stack.is(CataclysmItems.PALE_BOW)) {
                return level * 4;
            }
        }
        if (enchantment.is(Enchantments.RIPTIDE) && stack.is(CataclysmItems.ARCANE_TRIDENT)) {
            return level + 2;
        }
        if (enchantment.is(Enchantments.DENSITY) && stack.is(CataclysmItems.MIRAGE_MACE)) {
            return level + 2;
        }
        if (enchantment.is(Enchantments.BREACH) && (stack.is(CataclysmItems.PALE_SWORD) || stack.is(CataclysmItems.PALE_BOW))) {
            return level + 3;
        }
        return level;
    }
}
