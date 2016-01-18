/*
 * Copyright (c) Cybermaxke - All Rights Reserved
 */
package me.cybermaxke.materialfactory.api.util;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Equivalence;
import com.google.common.cache.CacheBuilder;

public final class CacheBuilderHelper {

    private static Method keyEquivalenceMethod;
    private static Method valueEquivalenceMethod;

    /**
     * Sets a custom {@code Equivalence} strategy for comparing keys.
     *
     * <p>By default, the cache uses {@link Equivalence#identity} to determine key equality when
     * {@link CacheBuilder#weakKeys} is specified, and {@link Equivalence#equals} otherwise.
     * 
     * @param builder the builder to apply the equivalence to
     * @param equivalence the equivalence strategy to apply
     * @return the builder for chaining
     */
    public static <K, V> CacheBuilder<K, V> keyEquivalence(CacheBuilder<K, V> builder,
            Equivalence<K> equivalence) {
        try {
            if (keyEquivalenceMethod == null) {
                keyEquivalenceMethod = CacheBuilder.class.getDeclaredMethod(
                        "keyEquivalence", Equivalence.class);
                keyEquivalenceMethod.setAccessible(true);
            }
            keyEquivalenceMethod.invoke(builder, equivalence);
        } catch (IllegalStateException | NullPointerException e) {
            throw e;
        } catch (Throwable t) {
            Logger.getLogger(CacheBuilderHelper.class.getName()).log(Level.SEVERE,
                    "Unable to set the keyEquivalence of a CacheBuilder", t);
        }
        return builder;
    }

    /**
     * Sets a custom {@code Equivalence} strategy for comparing values.
     *
     * <p>By default, the cache uses {@link Equivalence#identity} to determine value equality when
     * {@link CacheBuilder#weakValues} or {@link CacheBuilder#softValues} is specified,
     * and {@link Equivalence#equals} otherwise.
     * 
     * @param builder the builder to apply the equivalence to
     * @param equivalence the equivalence strategy to apply
     * @return the builder for chaining
     */
    public static <K, V> CacheBuilder<K, V> valueEquivalence(CacheBuilder<K, V> builder,
            Equivalence<V> equivalence) {
        try {
            if (valueEquivalenceMethod == null) {
                valueEquivalenceMethod = CacheBuilder.class.getDeclaredMethod(
                        "valueEquivalence", Equivalence.class);
                valueEquivalenceMethod.setAccessible(true);
            }
            valueEquivalenceMethod.invoke(builder, equivalence);
        } catch (IllegalStateException | NullPointerException e) {
            throw e;
        } catch (Throwable t) {
            Logger.getLogger(CacheBuilderHelper.class.getName()).log(Level.SEVERE,
                    "Unable to set the valueEquivalence of a CacheBuilder", t);
        }
        return builder;
    }

    private CacheBuilderHelper() {
    }
}
