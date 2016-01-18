package me.cybermaxke.materialfactory.api.data;

/**
 * Represents a data structure that contains data. A DataContainer is
 * an object that can be considered a root {@link DataView}.
 */
public interface DataContainer extends DataView {

    @Override
    DataContainer set(DataQuery path, Object value);

    @Override
    DataContainer remove(DataQuery path);
}
