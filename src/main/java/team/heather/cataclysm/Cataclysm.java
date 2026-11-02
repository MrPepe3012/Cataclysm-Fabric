
package team.heather.cataclysm;

import team.heather.cataclysm.command.CataclysmCommand;
import team.heather.cataclysm.command.MortalityCommand;
import team.heather.cataclysm.command.Permission;
import team.heather.cataclysm.registry.item.CataclysmItems;
import team.heather.cataclysm.registry.item.PassiveItemEffects;
import team.heather.cataclysm.registry.item.lemegeton.Lemegeton;
import team.heather.cataclysm.armor.ArmorSetEffects;
import team.heather.cataclysm.registry.item.totem.TotemResurrection;
import team.heather.cataclysm.registry.item.totem.mortality.MortalityAftereffects;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.registry.block.CataclysmBlocks;

public final class Cataclysm implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(Cataclysm.class);
    public static final String MOD_ID = "cataclysm";

    @Getter @Setter private static int week = 1;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        CataclysmServer.initialize();
        CataclysmBlocks.initialize();

        CataclysmRegistry.bootstrapper()
            .include(CataclysmItems.class)
            .bootstrap();
        CataclysmItems.registerCreativeModeTab();
        Lemegeton.initialize(Cataclysm::getWeek);

        TotemResurrection.initialize();
        MortalityAftereffects.initialize();
        ArmorSetEffects.initialize();
        PassiveItemEffects.initialize();

        CataclysmCommand.registration()
            .add(Permission.ADMIN, MortalityCommand.ALIAS, MortalityCommand::new)
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
