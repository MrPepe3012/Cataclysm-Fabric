package team.heather.cataclysm.registry.block;

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

import java.util.function.Function;

public final class CataclysmBlocks {
    private static final float ARCANE_CORE_PITCH_MULTIPLIER = 0.9F;
    private static final SoundType ARCANE_CORE_SOUND = new SoundType(SoundType.HEAVY_CORE.getVolume(),
            SoundType.HEAVY_CORE.getPitch() * ARCANE_CORE_PITCH_MULTIPLIER,
            SoundType.HEAVY_CORE.getBreakSound(), SoundType.HEAVY_CORE.getStepSound(),
            SoundType.HEAVY_CORE.getPlaceSound(), SoundType.HEAVY_CORE.getHitSound(), SoundType.HEAVY_CORE.getFallSound());

    public static final Block ARCANE_CORE = register("arcane_core", HeavyCoreBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.SNARE)
                    .sound(ARCANE_CORE_SOUND).strength(10).pushReaction(PushReaction.NORMAL).explosionResistance(1200));
    public static final Block GOLEM_HEAD = register("golem_head",
            properties -> new SkullBlock(GolemSkullType.GOLEM, properties));
    public static final Block GOLEM_WALL_HEAD = register("golem_wall_head",
            properties -> new WallSkullBlock(GolemSkullType.GOLEM, properties));

    private CataclysmBlocks() {}

    private static Block register(String path, Function<BlockBehaviour.Properties, Block> factory) {
        return register(path, factory, BlockBehaviour.Properties.of()
                .strength(1).noOcclusion().sound(SoundType.STONE).instrument(NoteBlockInstrument.CUSTOM_HEAD));
    }

    private static Block register(String path, Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties) {
        var key = ResourceKey.create(Registries.BLOCK, Cataclysm.createNamespacedKey(path));
        return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
    }

    public static void initialize() {
        BlockEntityType.SKULL.addSupportedBlock(GOLEM_HEAD);
        BlockEntityType.SKULL.addSupportedBlock(GOLEM_WALL_HEAD);
    }
}
