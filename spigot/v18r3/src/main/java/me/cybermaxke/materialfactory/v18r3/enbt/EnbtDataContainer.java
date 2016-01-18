/*
 * Copyright (c) Cybermaxke - All Rights Reserved
 */
package me.cybermaxke.materialfactory.v18r3.enbt;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.cybermaxke.materialfactory.api.data.DataContainer;
import me.cybermaxke.materialfactory.api.data.DataQuery;
import me.cybermaxke.materialfactory.api.data.DataView;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public final class EnbtDataContainer extends EnbtDataView implements DataContainer {

    private static final LoadingCache<NBTTagCompound, EnbtDataContainer> cache =
            CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<NBTTagCompound, EnbtDataContainer>() {
                @Override
                public EnbtDataContainer load(NBTTagCompound key) throws Exception {
                    return new EnbtDataContainer(key);
                }
            });

    public static EnbtDataContainer create() {
        return create(new NBTTagCompound());
    }

    public static EnbtDataContainer create(NBTTagCompound tag) {
        try {
            return cache.get(tag);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private EnbtDataContainer(NBTTagCompound tag) {
        super(tag);
    }

    @Override
    public Optional<DataView> getParent() {
        return Optional.empty();
    }

    @Override
    public DataContainer getContainer() {
        return this;
    }

    @Override
    public DataContainer set(DataQuery path, Object value) {
        return (DataContainer) super.set(path, value);
    }

    @Override
    public DataContainer remove(DataQuery path) {
        return (DataContainer) super.remove(path);
    }
}
