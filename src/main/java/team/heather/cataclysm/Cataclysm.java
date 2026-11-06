package team.heather.cataclysm;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.heather.cataclysm.command.CataclysmCommand;
import team.heather.cataclysm.command.ItemCooldownCommand;
import team.heather.cataclysm.command.LemegetonCommand;
import team.heather.cataclysm.command.MortalityCommand;
import team.heather.cataclysm.command.PhaseCommand;
import team.heather.cataclysm.command.RagnarokCommand;
import team.heather.cataclysm.effect.CataclysmMobEffects;
import team.heather.cataclysm.effect.MobEffectProcessor;
import team.heather.cataclysm.component.mortality.MortalityProcessor;
import team.heather.cataclysm.block.CataclysmBlocks;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.behavior.PassiveItemProcessor;
import team.heather.cataclysm.item.behavior.PersistentItemCooldowns;
import team.heather.cataclysm.item.feature.armor.ArmorSetProcessor;
import team.heather.cataclysm.item.feature.lemegeton.LemegetonProcessor;
import team.heather.cataclysm.item.feature.totem.TotemProcessor;
import team.heather.cataclysm.menu.CataclysmMenus;
import team.heather.cataclysm.network.payload.CataclysmPayloads;
import team.heather.cataclysm.server.ServerReceiver;
import team.heather.cataclysm.server.CataclysmServer;
import team.heather.cataclysm.sound.CataclysmSoundEvents;

public final class Cataclysm implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(Cataclysm.class);
    public static final String MOD_ID = "cataclysm";

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        CataclysmServer.initialize();

        CataclysmSoundEvents.initialize();
        CataclysmMobEffects.initialize();
        CataclysmDataComponents.initialize();
        CataclysmBlocks.initialize();
        CataclysmItems.initialize();
        CataclysmMenus.initialize();
        CataclysmPayloads.initialize();
        ServerReceiver.initialize();

        MobEffectProcessor.initialize();
        LemegetonProcessor.initialize();
        TotemProcessor.initialize();
        MortalityProcessor.initialize();
        ArmorSetProcessor.initialize();
        PassiveItemProcessor.initialize();
        PersistentItemCooldowns.initialize();

        CataclysmCommand.registration()
            .add(4, "mortality", MortalityCommand::new)
            .add(4, "lemegeton", LemegetonCommand::new)
            .add(4, "cooldown", ItemCooldownCommand::new)
            .add(4, "phase", PhaseCommand::new)
            .add(4, "ragnarok", RagnarokCommand::new)
            .done();

        LOGGER.info("Done.");
    }

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
