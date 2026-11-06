package team.heather.cataclysm.item.feature.twisted;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.data.TwistedPickaxeComponent;

public final class TwistedProcessor {
    private TwistedProcessor() {}

    public static int level(ItemStack stack,
                            Holder<Enchantment> enchantment,
                            int stored) {
        if (!stack.is(CataclysmItems.TWISTED_PICKAXE)) return stored;
        var mode = stack.getOrDefault(CataclysmDataComponents.TWISTED_PICKAXE, TwistedPickaxeComponent.SILK_TOUCH);
        if (enchantment.is(Enchantments.FORTUNE)) {
            return mode.fortuneLevel();
        }
        if (enchantment.is(Enchantments.SILK_TOUCH)) {
            return mode.silkTouchLevel();
        }
        return stored;
    }

    /** Loot predicates inspect the enchantment component directly; enchant only a temporary copy. */
    public static ItemStack lootTool(ItemStack stack, HolderLookup.Provider registries) {
        if (!stack.is(CataclysmItems.TWISTED_PICKAXE)) {
            return stack;
        }
        var copy = stack.copy();
        var mode = stack.getOrDefault(CataclysmDataComponents.TWISTED_PICKAXE, TwistedPickaxeComponent.SILK_TOUCH);
        var enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        EnchantmentHelper.updateEnchantments(copy, values -> {
            values.set(enchantments.getOrThrow(Enchantments.FORTUNE), mode.fortuneLevel());
            values.set(enchantments.getOrThrow(Enchantments.SILK_TOUCH), mode.silkTouchLevel());
        });
        return copy;
    }
}
