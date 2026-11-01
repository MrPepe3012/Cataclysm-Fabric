package team.heather.cataclysm.registry.item;

import net.minecraft.world.item.Item;
import team.heather.cataclysm.registry.component.CataclysmDataComponents;

import java.util.stream.IntStream;

public final class CataclysmUpgradeItem {

    private CataclysmUpgradeItem() {}

    public static Item create(Foundry item) {
        var range = IntStream.rangeClosed(CataclysmDataComponents.MIN_UPGRADE_TIER, CataclysmDataComponents.MAX_UPGRADE_TIER);
        return item.palette(Palette.LEMEGETON)
            .props(properties -> properties.component(CataclysmDataComponents.UPGRADE_TIER, CataclysmDataComponents.MIN_UPGRADE_TIER))
            .variants(CataclysmDataComponents.UPGRADE_TIER, range.boxed().toList())
            .build();
    }
}
