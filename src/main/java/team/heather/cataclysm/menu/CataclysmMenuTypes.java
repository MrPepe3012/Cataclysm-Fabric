package team.heather.cataclysm.menu;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.item.lemegeton.LemegetonMenu;
import team.heather.cataclysm.registry.CataclysmRegistry;

public interface CataclysmMenuTypes extends CataclysmRegistry<MenuType<?>> {

    MenuType<LemegetonMenu> LEMEGETON = register("lemegeton", LemegetonMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(String path,
                                                                          MenuType.MenuSupplier<T> menu) {
        return Registry.register(
                BuiltInRegistries.MENU,
                Cataclysm.createNamespacedKey(path),
                new MenuType<>(menu, FeatureFlags.VANILLA_SET)
        );
    }
}
