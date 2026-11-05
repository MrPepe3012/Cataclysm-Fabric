package team.heather.cataclysm.client;

import net.fabricmc.api.ClientModInitializer;
import team.heather.cataclysm.client.armor.ResourcePackArmorRenderer;
import team.heather.cataclysm.client.item.UpgradeTierProperty;
import team.heather.cataclysm.client.item.PickaxeModeProperty;
import team.heather.cataclysm.client.item.CustomNameProperty;
import team.heather.cataclysm.client.item.MaceModeProperty;
import team.heather.cataclysm.client.screen.lemegeton.LemegetonScreen;

public class CataclysmClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        UpgradeTierProperty.register();
        PickaxeModeProperty.register();
        CustomNameProperty.register();
        MaceModeProperty.register();
        ResourcePackArmorRenderer.register();
        LemegetonScreen.register();
    }
}
