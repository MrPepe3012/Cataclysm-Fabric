package team.heather.cataclysm.core.item.material;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

/** Cataclysm tool materials, built fluently with the same API as {@link CataclysmArmorMaterial}. */
public final class CataclysmToolMaterial {

    public static final ToolMaterial PALE = of()
            .durability(500)
            .enchantability(12)
            .repair(ItemTags.IRON_TOOL_MATERIALS)
            .incorrectBlocks(BlockTags.INCORRECT_FOR_IRON_TOOL)
            .speed(6.0F)
            .damage(2.5F)
            .build();

    private int durability;
    private int enchantability;
    private TagKey<Item> repair;
    private TagKey<Block> incorrectBlocks;
    private float speed;
    private float damage;

    private CataclysmToolMaterial() {}

    public static CataclysmToolMaterial of() {
        return new CataclysmToolMaterial();
    }

    public CataclysmToolMaterial durability(int durability) {
        this.durability = durability;
        return this;
    }

    public CataclysmToolMaterial enchantability(int enchantability) {
        this.enchantability = enchantability;
        return this;
    }

    public CataclysmToolMaterial repair(TagKey<Item> repair) {
        this.repair = Objects.requireNonNull(repair);
        return this;
    }

    /** Blocks this tier cannot harvest, e.g. {@link BlockTags#INCORRECT_FOR_IRON_TOOL}. */
    public CataclysmToolMaterial incorrectBlocks(TagKey<Block> incorrectBlocks) {
        this.incorrectBlocks = Objects.requireNonNull(incorrectBlocks);
        return this;
    }

    public CataclysmToolMaterial speed(float speed) {
        this.speed = speed;
        return this;
    }

    /** Attack damage bonus added on top of each tool's base damage. */
    public CataclysmToolMaterial damage(float damage) {
        this.damage = damage;
        return this;
    }

    public ToolMaterial build() {
        return new ToolMaterial(
                Objects.requireNonNull(this.incorrectBlocks),
                this.durability,
                this.speed,
                this.damage,
                this.enchantability,
                Objects.requireNonNull(this.repair));
    }
}
