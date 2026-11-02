package team.heather.cataclysm.registry.item.twisted;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.registry.item.CataclysmItems;

public final class TwistedMining {
    private TwistedMining() {}

    public static int level(ItemStack stack, Holder<Enchantment> enchantment, int stored) {
        if (!stack.is(CataclysmItems.TWISTED_PICKAXE)) {
            return stored;
        }
        boolean silk = stack.getOrDefault(CataclysmDataComponents.PICKAXE_MODE, 0) == 1;
        if (enchantment.is(Enchantments.FORTUNE)) {
            return silk ? 0 : 5;
        }
        if (enchantment.is(Enchantments.SILK_TOUCH)) {
            return silk ? 1 : 0;
        }
        return stored;
    }

    /** Loot predicates inspect the enchantment component directly; enchant only a temporary copy. */
    public static ItemStack lootTool(ItemStack stack, HolderLookup.Provider registries) {
        if (!stack.is(CataclysmItems.TWISTED_PICKAXE)) {
            return stack;
        }
        var copy = stack.copy();
        boolean silk = stack.getOrDefault(CataclysmDataComponents.PICKAXE_MODE, 0) == 1;
        var enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        EnchantmentHelper.updateEnchantments(copy, values -> {
            values.set(enchantments.getOrThrow(Enchantments.FORTUNE), silk ? 0 : 5);
            values.set(enchantments.getOrThrow(Enchantments.SILK_TOUCH), silk ? 1 : 0);
        });
        return copy;
    }
}
