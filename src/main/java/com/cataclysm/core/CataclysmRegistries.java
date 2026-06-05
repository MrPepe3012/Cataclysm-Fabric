package com.cataclysm.core;

import com.cataclysm.effect.CataclysmMobEffects;

import java.util.Set;

public final class CataclysmRegistries {
    private static final Set<Class<?>> LOADERS = Set.of(
            CataclysmMobEffects.class
    );

    private CataclysmRegistries() {}

    public static void bootstrap() {
        LOADERS.forEach(clazz -> {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError(e);
            }
        });
    }
}