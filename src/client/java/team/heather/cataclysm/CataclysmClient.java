package team.heather.cataclysm;

import net.fabricmc.api.ClientModInitializer;
import team.heather.cataclysm.item.LoreTooltip;
import team.heather.cataclysm.item.UpgradeTierProperty;
import team.heather.cataclysm.item.PickaxeModeProperty;

public class CataclysmClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LoreTooltip.register();
        UpgradeTierProperty.register();
        PickaxeModeProperty.register();
    }
}
