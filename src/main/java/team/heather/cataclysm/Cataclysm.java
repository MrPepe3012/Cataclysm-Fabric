
package team.heather.cataclysm;

import team.heather.cataclysm.command.CataclysmCommand;
import team.heather.cataclysm.command.MortalityCommand;
import team.heather.cataclysm.item.effect.PassiveItemEffects;
import team.heather.cataclysm.item.effect.PersistentItemCooldowns;
import team.heather.cataclysm.item.lemegeton.LemegetonEffects;
import team.heather.cataclysm.item.armor.ArmorSetEffects;
import team.heather.cataclysm.item.totem.TotemEffects;
import team.heather.cataclysm.component.cardinal.mortality.MortalityEffects;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.heather.cataclysm.registry.CataclysmRegistry;

public final class Cataclysm implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(Cataclysm.class);
    public static final String MOD_ID = "cataclysm";

    @Getter @Setter private static int week = 1;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        CataclysmServer.initialize();

        CataclysmRegistry.bootstrap();

        LemegetonEffects.initialize(Cataclysm::getWeek);
        TotemEffects.initialize();
        MortalityEffects.initialize();
        ArmorSetEffects.initialize();
        PassiveItemEffects.initialize();
        PersistentItemCooldowns.initialize();

        CataclysmCommand.registration()
            .add(4, MortalityCommand.ALIAS, MortalityCommand::new)
            .done();

        LOGGER.info("Done.");
    }

    public static MinecraftServer minecraftServer() {
        return CataclysmServer.getInstance().getServer();
    }

    public static ResourceLocation createNamespacedKey(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
