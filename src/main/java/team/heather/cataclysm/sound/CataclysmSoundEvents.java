package team.heather.cataclysm.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import team.heather.cataclysm.Cataclysm;

public interface CataclysmSoundEvents {

    SoundEvent RAGNAROK = register("ragnarok");

    static void initialize() {}

    private static SoundEvent register(String path) {
        var resourceLocation = Cataclysm.resourceLocation(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }
}
