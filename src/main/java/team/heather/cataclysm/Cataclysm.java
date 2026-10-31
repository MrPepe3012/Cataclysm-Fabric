
package team.heather.cataclysm;

import team.heather.cataclysm.command.CataclysmCommand;
import team.heather.cataclysm.command.MortalityCommand;
import team.heather.cataclysm.command.Permission;
import team.heather.cataclysm.core.CataclysmRegistry;
import team.heather.cataclysm.core.item.CataclysmItems;
import team.heather.cataclysm.core.item.armor.ArmorSetBonus;
import team.heather.cataclysm.core.item.armor.PaleArmorEffects;
import team.heather.cataclysm.core.item.totem.TotemResurrection;
import team.heather.cataclysm.core.item.totem.mortality.MortalityAftereffects;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Cataclysm implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(Cataclysm.class);
    public static final String MOD_ID = "cataclysm";

    @Getter @Setter private static int week;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        CataclysmServer.initialize();

        CataclysmRegistry.bootstrapper()
                .include(CataclysmItems.class)
                .bootstrap();
        CataclysmItems.registerCreativeModeTab();

        TotemResurrection.initialize();
        MortalityAftereffects.initialize();
        ArmorSetBonus.initialize();
        PaleArmorEffects.initialize();

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
