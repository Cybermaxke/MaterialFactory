package me.cybermaxke.materialfactory.api.data;

import static me.cybermaxke.materialfactory.api.data.DataQuery.of;

import com.google.common.base.Objects;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Default implementation of a {@link DataView} being used in memory.
 */
public class MemoryDataView extends AbstractDataView {

    protected final Map<String, Object> map = Maps.newLinkedHashMap();

    protected MemoryDataView() {
        super();
    }

    protected MemoryDataView(DataView parent, DataQuery path) {
        super(parent, path);
    }

    @Override
    protected boolean containsKey(String key) {
        return this.map.containsKey(key);
    }

    @Override
    protected Object get(String key) {
        return this.map.get(key);
    }

    @Override
    protected void put(String key, Object object) {
        this.map.put(key, object);
    }

    @Override
    protected void remove(String key) {
        this.map.remove(key);
    }

    @Override
    protected DataView createSubView(DataQuery path) {
        return new MemoryDataView(this.parent, path);
    }

    @Override
    public Set<DataQuery> getKeys(boolean deep) {
        ImmutableSet.Builder<DataQuery> builder = ImmutableSet.builder();

        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            builder.add(of(entry.getKey()));
        }
        if (deep) {
            for (Map.Entry<String, Object> entry : this.map.entrySet()) {
                if (entry.getValue() instanceof DataView) {
                    for (DataQuery query : ((DataView) entry.getValue()).getKeys(true)) {
                        builder.add(of(entry.getKey()).then(query));
                    }
                }
            }
        }
        return builder.build();
    }

    @Override
    public DataContainer copy() {
        final DataContainer container = new MemoryDataContainer();
        for (DataQuery query : getKeys(false)) {
            container.set(query, get(query).get());
        }
        return container;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.map, this.path);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MemoryDataView other = (MemoryDataView) obj;

        return Objects.equal(this.map.entrySet(), other.map.entrySet())
               && Objects.equal(this.path, other.path);
    }

    @Override
    public String toString() {
        final MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        if (!this.path.toString().isEmpty()) {
            helper.add("path", this.path);
        }
        return helper.add("map", this.map).toString();
    }
}
