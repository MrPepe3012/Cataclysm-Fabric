package team.heather.cataclysm.registry;

import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.block.CataclysmBlocks;
import team.heather.cataclysm.component.CataclysmDataComponents;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.menu.CataclysmMenuTypes;
import team.heather.cataclysm.sound.CataclysmSoundEvents;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/** Entry point to every registry, as in {@code CataclysmRegistry.ITEMS.LEMEGETON}. They start in the order declared here. */
public interface CataclysmRegistry {

    CataclysmSoundEvents SOUNDS = initialize(CataclysmSoundEvents.class);
    CataclysmDataComponents DATA_COMPONENTS = initialize(CataclysmDataComponents.class);
    CataclysmBlocks BLOCKS = initialize(CataclysmBlocks.class);
    CataclysmItems ITEMS = initialize(CataclysmItems.class);
    CataclysmMenuTypes MENUS = initialize(CataclysmMenuTypes.class);

    /** Loads this interface, which starts every registry above. */
    static void bootstrap() {
        Cataclysm.LOGGER.info("All registries are ready.");
    }

    /** Runs right after the registry's own entries are registered. */
    default void onBootstrap() {}

    /** A proxy gives the interface an instance without a class of its own; it only needs to run default methods. */
    private static <R extends CataclysmRegistry> R initialize(Class<R> type) {
        Cataclysm.LOGGER.info("Initializing {}...", type.getSimpleName());
        try {
            Class.forName(type.getName(), true, type.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
        var registry = type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, InvocationHandler::invokeDefault));
        registry.onBootstrap();
        Cataclysm.LOGGER.info("Initialized {}.", type.getSimpleName());
        return registry;
    }
}
