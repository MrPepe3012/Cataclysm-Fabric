
package team.heather.cataclysm.util;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Bootstrapper {

    private final Set<Class<?>> classes = new LinkedHashSet<>();

    public Bootstrapper include(Class<?> type) {
        this.classes.add(type);
        return this;
    }

    public void bootstrap() {
        for (var type : this.classes) {
            try {
                Class.forName(type.getName(), true, type.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
}
