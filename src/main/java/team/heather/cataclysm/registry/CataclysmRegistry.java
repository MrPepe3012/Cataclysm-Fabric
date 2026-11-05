package team.heather.cataclysm.registry;

import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.block.CataclysmBlocks;
import team.heather.cataclysm.component.CataclysmDataComponents;
import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.menu.CataclysmMenuTypes;
import team.heather.cataclysm.sound.CataclysmSoundEvents;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

/** Entry point to every registry, as in {@code CataclysmRegistry.ITEMS.LEMEGETON}. They start in the order declared here. */
public interface CataclysmRegistry<T> {

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

    /** Every entry of this registry, in declaration order. Primitive constants are not entries. */
    @SuppressWarnings("unchecked")
    default List<T> contentOf() {
        return Arrays.stream(this.getClass().getInterfaces()[0].getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive())
                .map(field -> {
                    try {
                        return (T) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .toList();
    }

    /** A proxy gives the interface an instance without a class of its own; it only needs to run default methods. */
    private static <R extends CataclysmRegistry<?>> R initialize(Class<R> type) {
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
