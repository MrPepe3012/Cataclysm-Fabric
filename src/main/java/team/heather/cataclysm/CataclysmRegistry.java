
package team.heather.cataclysm;

import team.heather.cataclysm.util.Bootstrapper;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public interface CataclysmRegistry {

    static Bootstrapper bootstrapper() {
        return new Bootstrapper();
    }

    static <T> List<T> contentOf(Class<?> registry,
                                 Class<T> type) {
        return Arrays.stream(registry.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && type.isAssignableFrom(field.getType()))
                .map(field -> {
                    try {
                        return type.cast(field.get(null));
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .toList();
    }
}
