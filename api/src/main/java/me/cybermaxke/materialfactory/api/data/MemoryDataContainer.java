package me.cybermaxke.materialfactory.api.data;

import java.util.Optional;

/**
 * The default implementation of {@link DataContainer} that can be instantiated
 * for any use.
 */
public class MemoryDataContainer extends MemoryDataView implements DataContainer {

    @Override
    public Optional<DataView> getParent() {
        return Optional.empty();
    }

    @Override
    public final DataContainer getContainer() {
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