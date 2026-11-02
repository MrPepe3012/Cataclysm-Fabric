package team.heather.cataclysm.registry.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import team.heather.cataclysm.registry.item.CataclysmItems;

public final class EquipmentEnchantments {
    private EquipmentEnchantments() {}

    public static int effectiveLevel(ItemStack stack, Holder<Enchantment> enchantment, int level) {
        if (enchantment.is(Enchantments.POWER) && stack.is(CataclysmItems.ARCANE_BOW)) {
            return powerLevel(level);
        }
        if (enchantment.is(Enchantments.RIPTIDE) && stack.is(CataclysmItems.ARCANE_TRIDENT)) {
            return riptideLevel(level);
        }
        if (enchantment.is(Enchantments.DENSITY) && stack.is(CataclysmItems.MIRAGE_MACE)) {
            return mirageDensityLevel(level);
        }
        if (enchantment.is(Enchantments.POWER) && stack.is(CataclysmItems.PALE_BOW)) {
            return palePowerLevel(level);
        }
        if (enchantment.is(Enchantments.BREACH) && (stack.is(CataclysmItems.PALE_SWORD) || stack.is(CataclysmItems.PALE_BOW))) {
            return paleBreachLevel(level);
        }
        return level;
    }

    public static int powerLevel(int level) {
        return level > 0 ? level * 2 : 0;
    }

    public static int riptideLevel(int level) {
        return level > 0 ? level + 2 : 0;
    }
    public static int mirageDensityLevel(int level) {
        return level > 0 ? level + 2 : 0;
    }

    public static int palePowerLevel(int level) {
        return level > 0 ? level * 4 : 0;
    }

    public static int paleBreachLevel(int level) {
        return level > 0 ? level + 3 : 0;
    }}
