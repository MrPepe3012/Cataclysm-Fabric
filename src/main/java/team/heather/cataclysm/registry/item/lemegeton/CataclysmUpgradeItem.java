package team.heather.cataclysm.registry.item.lemegeton;

import team.heather.cataclysm.registry.component.CataclysmDataComponents;
import team.heather.cataclysm.builder.ItemFactory;
import team.heather.cataclysm.builder.ItemFamily;

import java.util.stream.IntStream;

public final class CataclysmUpgradeItem {

    private CataclysmUpgradeItem() {}

    public static ItemFactory create(ItemFactory item) {
        var range = IntStream.rangeClosed(CataclysmDataComponents.MIN_UPGRADE_TIER, CataclysmDataComponents.MAX_UPGRADE_TIER);
        return item.family(ItemFamily.LEMEGETON)
            .props(properties -> properties.component(CataclysmDataComponents.UPGRADE_TIER, CataclysmDataComponents.MIN_UPGRADE_TIER))
            .variants(CataclysmDataComponents.UPGRADE_TIER, range.boxed().toList());
    }
}
