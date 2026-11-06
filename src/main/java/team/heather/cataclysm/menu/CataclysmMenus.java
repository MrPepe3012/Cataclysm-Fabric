package team.heather.cataclysm.menu;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.menu.lemegeton.LemegetonMenu;

import java.util.function.IntFunction;

public interface CataclysmMenus {

    MenuType<LemegetonMenu> LEMEGETON = register("lemegeton", (id, inventory) -> new LemegetonMenu(id));

    static void initialize() {
        // Loading this interface is what registers every menu.
    }

    /** Returns whether the menu actually opened. */
    static boolean open(ServerPlayer player, String titleKey, IntFunction<AbstractContainerMenu> menu) {
        var title = Component.translatable(titleKey);
        var provider = new SimpleMenuProvider((id, inventory, owner) -> menu.apply(id), title);
        return player.openMenu(provider).isPresent();
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String path, MenuType.MenuSupplier<T> menu) {
        return Registry.register(
                BuiltInRegistries.MENU,
                Cataclysm.resourceLocation(path),
                new MenuType<>(menu, FeatureFlags.VANILLA_SET)
        );
    }
}
