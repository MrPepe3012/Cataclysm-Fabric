package team.heather.cataclysm.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HeavyCoreBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.CataclysmRegistry;

import java.util.function.Function;

public interface CataclysmBlocks extends CataclysmRegistry {

    Block ARCANE_CORE = register("arcane_core", HeavyCoreBlock::new, BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .instrument(NoteBlockInstrument.SNARE)
        .sound(withPitch(SoundType.HEAVY_CORE, 0.9F))
        .strength(10)
        .pushReaction(PushReaction.NORMAL)
        .explosionResistance(1200));

    Block GOLEM_HEAD = register("golem_head", properties -> new SkullBlock(GolemSkullType.GOLEM, properties));
    Block GOLEM_WALL_HEAD = register("golem_wall_head", properties -> new WallSkullBlock(GolemSkullType.GOLEM, properties));

    @Override
    default void onBootstrap() {
        BlockEntityType.SKULL.addSupportedBlock(GOLEM_HEAD);
        BlockEntityType.SKULL.addSupportedBlock(GOLEM_WALL_HEAD);
    }

    private static Block register(String path,
                                  Function<BlockBehaviour.Properties, Block> factory) {
        return register(path, factory, BlockBehaviour.Properties.of()
            .strength(1)
            .noOcclusion()
            .sound(SoundType.STONE)
            .instrument(NoteBlockInstrument.CUSTOM_HEAD));
    }

    private static Block register(String path,
                                  Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties) {
        var key = ResourceKey.create(Registries.BLOCK, Cataclysm.createNamespacedKey(path));
        return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
    }

    private static SoundType withPitch(SoundType sound,
                                       float multiplier) {
        return new SoundType(
                sound.getVolume(),
                sound.getPitch() * multiplier,
                sound.getBreakSound(),
                sound.getStepSound(),
                sound.getPlaceSound(),
                sound.getHitSound(),
                sound.getFallSound()
        );
    }
}
