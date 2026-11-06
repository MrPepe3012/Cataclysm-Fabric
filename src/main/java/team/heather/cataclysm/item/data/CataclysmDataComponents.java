package team.heather.cataclysm.item.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import team.heather.cataclysm.Cataclysm;

public interface CataclysmDataComponents {

    DataComponentType<UpgradeTierComponent> UPGRADE_TIER = register("upgrade_tier", UpgradeTierComponent.CODEC);
    DataComponentType<TwistedPickaxeComponent> TWISTED_PICKAXE = register("twisted_pickaxe", TwistedPickaxeComponent.CODEC);
    DataComponentType<MirageMaceComponent> MIRAGE_MACE = register("mirage_mace", MirageMaceComponent.CODEC);
    DataComponentType<LemegetonComponent> LEMEGETON = register("lemegeton", LemegetonComponent.CODEC);

    static void initialize() {}

    private static <T> DataComponentType<T> register(String path, Codec<T> codec) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Cataclysm.resourceLocation(path),
            DataComponentType.<T>builder().persistent(codec).networkSynchronized(ByteBufCodecs.fromCodec(codec)).build());
    }
}
