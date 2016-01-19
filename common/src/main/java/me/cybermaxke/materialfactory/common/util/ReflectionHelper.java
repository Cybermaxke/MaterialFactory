package me.cybermaxke.materialfactory.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.annotation.Nullable;

public final class ReflectionHelper {

    public static void setField(Field field, @Nullable Object target, @Nullable Object object) throws Exception {
        int modifiers = field.getModifiers();

        if (Modifier.isFinal(modifiers)) {
            Field mfield = Field.class.getDeclaredField("modifiers");
            mfield.setAccessible(true);
            mfield.set(field, modifiers & ~Modifier.FINAL);
        }

        field.setAccessible(true);
        field.set(target, object);
    }

    private ReflectionHelper() {
    }

}
