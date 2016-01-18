package me.cybermaxke.materialfactory.v18r3.enbt;

import static me.cybermaxke.materialfactory.api.data.DataQuery.of;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import com.google.common.base.Equivalence;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

import me.cybermaxke.materialfactory.api.data.AbstractDataView;
import me.cybermaxke.materialfactory.api.data.DataContainer;
import me.cybermaxke.materialfactory.api.data.DataQuery;
import me.cybermaxke.materialfactory.api.data.DataView;
import me.cybermaxke.materialfactory.api.util.CacheBuilderHelper;

public class EnbtDataView extends AbstractDataView {

    private static Field nbtTagCompoundMap;
    private static final Map<NBTTagCompound, Set<CacheKey>> cacheKeys = new MapMaker().weakKeys().makeMap();
    private static final LoadingCache<CacheKey, EnbtDataView> cache =
            CacheBuilderHelper.keyEquivalence(CacheBuilder.newBuilder().weakKeys(), Equivalence.equals())
                    .build(new CacheLoader<CacheKey, EnbtDataView>() {
                @Override
                public EnbtDataView load(CacheKey key) throws Exception {
                    return new EnbtDataView(key.tag, key.parent, key.path);
                }
            });

    private static EnbtDataView getCachedView(DataView parent, DataQuery path, NBTTagCompound tag) {
        return getCachedView(new CacheKey(parent, path, tag));
    }

    private static boolean isCachedView(NBTTagCompound tag) {
        return cacheKeys.containsKey(tag);
    }

    private static EnbtDataView getCachedView(CacheKey key) {
        cacheKeys.computeIfAbsent(key.tag, key0 -> Sets.newConcurrentHashSet()).add(key);
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CacheKey {

        private final DataView parent;
        private final DataQuery path;
        private final NBTTagCompound tag;

        public CacheKey(DataView parent, DataQuery path, NBTTagCompound tag) {
            this.parent = parent;
            this.path = path;
            this.tag = tag;
        }

        @Override
        public int hashCode() {
            return Objects.hash(System.identityHashCode(this.tag), this.parent, this.path);
        }

        @Override
        public boolean equals(Object other) {
            if (other == null || other.getClass() != this.getClass()) {
                return false;
            }
            CacheKey o = (CacheKey) other;
            return o.parent.equals(this.parent) && this.tag == o.tag
                    && this.path.equals(o.path);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, NBTBase> getMapFrom(NBTTagCompound nbtTag) {
        try {
            if (nbtTagCompoundMap == null) {
                nbtTagCompoundMap = NBTTagCompound.class.getDeclaredField("map");
                nbtTagCompoundMap.setAccessible(true);
            }
            return (Map<String, NBTBase>) nbtTagCompoundMap.get(nbtTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private final NBTTagCompound tag;
    private final Map<String, NBTBase> map;

    EnbtDataView(NBTTagCompound tag) {
        this.map = getMapFrom(tag);
        this.tag = tag;
    }

    EnbtDataView(NBTTagCompound tag, DataView parent, DataQuery path) {
        super(parent, path);
        this.map = getMapFrom(tag);
        this.tag = tag;
    }

    /**
     * Gets the underlying tag of the data view.
     * 
     * @return the tag
     */
    public NBTTagCompound getTag() {
        return this.tag;
    }

    @Override
    public Set<DataQuery> getKeys(boolean deep) {
        ImmutableSet.Builder<DataQuery> builder = ImmutableSet.builder();

        for (Map.Entry<String, NBTBase> entry : this.map.entrySet()) {
            builder.add(of(entry.getKey()));
        }
        if (deep) {
            for (Map.Entry<String, NBTBase> entry : this.map.entrySet()) {
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
        final DataContainer container = EnbtDataContainer.create();
        for (DataQuery query : getKeys(false)) {
            container.set(query, get(query).get());
        }
        return container;
    }

    @Override
    protected boolean containsKey(String key) {
        return this.map.containsKey(key);
    }

    @Override
    protected Object get(String key) {
        NBTBase tag = this.map.get(key);
        Object object = null;
        // If there is already a view cached or if the tag returns a
        // null when deserializing -> DataView
        if (tag instanceof NBTTagCompound && (isCachedView((NBTTagCompound) tag) ||
                (object = EnbtSerializers.deserialize(tag)) == null)) {
            return getCachedView(this.parent, DataQuery.of(key), (NBTTagCompound) tag);
        }
        return object;
    }

    @Override
    protected void put(String key, Object object) {
        // Data views are special cases
        if (object instanceof DataView) {
            // We should be able to cast safely
            this.map.put(key, ((EnbtDataView) object).tag);
        } else {
            // Serialize the object
            this.map.put(key, EnbtSerializers.serialize(object));
        }
    }

    @Override
    protected void remove(String key) {
        this.map.remove(key);
    }

    @Override
    protected DataView createSubView(DataQuery path) {
        return getCachedView(this.parent, path, new NBTTagCompound());
    }

}
