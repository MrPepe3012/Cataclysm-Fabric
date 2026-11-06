package team.heather.cataclysm.component;

import com.mojang.serialization.Codec;
import team.heather.cataclysm.component.data.MirageMaceMode;
import team.heather.cataclysm.component.data.TwistedPickaxeMode;
import team.heather.cataclysm.component.data.lemegeton.LemegetonState;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.CataclysmRegistry;

/** Persistent, network-synchronized item stack components. */
public interface CataclysmDataComponents extends CataclysmRegistry {

    int MIN_UPGRADE_TIER = 1;
    int MAX_UPGRADE_TIER = 3;

    DataComponentType<Integer> UPGRADE_TIER = register("upgrade_tier", Codec.intRange(MIN_UPGRADE_TIER, MAX_UPGRADE_TIER));
    DataComponentType<TwistedPickaxeMode> PICKAXE_MODE = register("pickaxe_mode", TwistedPickaxeMode.CODEC);
    DataComponentType<MirageMaceMode> MACE_MODE = register("mace_mode", MirageMaceMode.CODEC);
    DataComponentType<LemegetonState> LEMEGETON = register("lemegeton", LemegetonState.CODEC);

    private static <T> DataComponentType<T> register(String path,
                                                     Codec<T> codec) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Cataclysm.createNamespacedKey(path),
            DataComponentType.<T>builder().persistent(codec).networkSynchronized(ByteBufCodecs.fromCodec(codec)).build());
    }
}
