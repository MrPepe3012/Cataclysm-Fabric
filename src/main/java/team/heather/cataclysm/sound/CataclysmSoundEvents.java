package team.heather.cataclysm.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.registry.CataclysmRegistry;

public interface CataclysmSoundEvents extends CataclysmRegistry {

    SoundEvent RAGNAROK = register("ragnarok");

    private static SoundEvent register(String path) {
        var resourceLocation = Cataclysm.createNamespacedKey(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }
}
