package team.heather.cataclysm.registry.component;

import com.mojang.serialization.Codec;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonState;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import team.heather.cataclysm.Cataclysm;

/** Persistent, network-synchronized item stack components. */
public final class CataclysmDataComponents {

    public static final int MIN_UPGRADE_TIER = 1;
    public static final int MAX_UPGRADE_TIER = 3;

    public static final DataComponentType<Integer> UPGRADE_TIER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Cataclysm.createNamespacedKey("upgrade_tier"),
            DataComponentType.<Integer>builder()
                    .persistent(Codec.intRange(MIN_UPGRADE_TIER, MAX_UPGRADE_TIER))
                    .networkSynchronized(ByteBufCodecs.fromCodec(Codec.intRange(MIN_UPGRADE_TIER, MAX_UPGRADE_TIER)))
                    .build());

    /** 0 is mode A (blue); 1 is mode B (red). */
    public static final DataComponentType<Integer> PICKAXE_MODE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE, Cataclysm.createNamespacedKey("pickaxe_mode"),
            DataComponentType.<Integer>builder().persistent(Codec.intRange(0, 1))
                    .networkSynchronized(ByteBufCodecs.fromCodec(Codec.intRange(0, 1))).build());

    /** 0 launches wind charges; 1 launches the wielder with the hook. */
    public static final DataComponentType<Integer> MACE_MODE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE, Cataclysm.createNamespacedKey("mace_mode"),
            DataComponentType.<Integer>builder().persistent(Codec.intRange(0, 1))
                    .networkSynchronized(ByteBufCodecs.fromCodec(Codec.intRange(0, 1))).build());

    public static final DataComponentType<LemegetonState> LEMEGETON = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE, Cataclysm.createNamespacedKey("lemegeton"),
            DataComponentType.<LemegetonState>builder().persistent(LemegetonState.CODEC)
                    .networkSynchronized(ByteBufCodecs.fromCodec(LemegetonState.CODEC)).build());

    private CataclysmDataComponents() {}
}
