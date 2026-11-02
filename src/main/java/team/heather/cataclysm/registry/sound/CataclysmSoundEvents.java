package team.heather.cataclysm.registry.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import team.heather.cataclysm.Cataclysm;

public interface CataclysmSoundEvents {

    SoundEvent RAGNAROK = register("ragnarok");

    private static SoundEvent register(String path) {
        var id = Cataclysm.createNamespacedKey(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
