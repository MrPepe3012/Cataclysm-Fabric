package team.heather.cataclysm;

import net.fabricmc.api.ClientModInitializer;
import team.heather.cataclysm.armor.ResourcePackArmorRenderer;
import team.heather.cataclysm.item.LoreTooltip;
import team.heather.cataclysm.item.UpgradeTierProperty;
import team.heather.cataclysm.item.PickaxeModeProperty;
import team.heather.cataclysm.item.CustomNameProperty;
import team.heather.cataclysm.item.MaceModeProperty;

public class CataclysmClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LoreTooltip.register();
        UpgradeTierProperty.register();
        PickaxeModeProperty.register();
        CustomNameProperty.register();
        MaceModeProperty.register();
        ResourcePackArmorRenderer.register();
    }
}
