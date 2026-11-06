package team.heather.cataclysm.block;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HeavyCoreBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import team.heather.cataclysm.Cataclysm;

import java.util.function.Function;

public interface CataclysmBlocks {

    SoundType ARCANE_CORE_SOUND = new SoundType(1, 0.9F, SoundEvents.HEAVY_CORE_BREAK, SoundEvents.HEAVY_CORE_STEP, SoundEvents.HEAVY_CORE_PLACE, SoundEvents.HEAVY_CORE_HIT, SoundEvents.HEAVY_CORE_FALL);

    Block ARCANE_CORE = register("arcane_core", HeavyCoreBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.HEAVY_CORE).sound(ARCANE_CORE_SOUND));
    Head GOLEM_HEAD = registerHead("golem");

    static void initialize() {}

    private static Block register(String path,
                                  Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties) {
        var key = ResourceKey.create(Registries.BLOCK, Cataclysm.resourceLocation(path));
        return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
    }

    private static Head registerHead(String name) {
        var id = Cataclysm.resourceLocation(name).toString();
        SkullBlock.Type type = () -> id;
        SkullBlock.Type.TYPES.put(id, type);

        var block = register(name + "_head", properties -> new SkullBlock(type, properties), headProperties());
        var wallBlock = register(name + "_wall_head", properties -> new WallSkullBlock(type, properties), headProperties());
        BlockEntityType.SKULL.addSupportedBlock(block);
        BlockEntityType.SKULL.addSupportedBlock(wallBlock);

        return new Head(type, block, wallBlock);
    }

    private static BlockBehaviour.Properties headProperties() {
        return BlockBehaviour.Properties.of()
            .strength(1)
            .noOcclusion()
            .sound(SoundType.STONE)
            .instrument(NoteBlockInstrument.CUSTOM_HEAD);
    }

    record Head(SkullBlock.Type type,
                Block block,
                Block wallBlock) {

        public Item toItem(Item.Properties properties) {
            return new StandingAndWallBlockItem(this.block, this.wallBlock, Direction.DOWN, properties);
        }
    }
}
