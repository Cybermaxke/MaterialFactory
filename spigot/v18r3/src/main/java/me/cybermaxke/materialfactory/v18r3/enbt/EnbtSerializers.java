package me.cybermaxke.materialfactory.v18r3.enbt;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.server.v1_8_R3.IntHashMap;
import net.minecraft.server.v1_8_R3.NBTBase;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EnbtSerializers {

    private static final EnbtSerializerContext ctx = new EnbtSerializerContext();
    private static final Map<Class<?>, EnbtRegistryEntry> serializersByClass = Maps.newHashMap();
    private static final IntHashMap<EnbtRegistryEntry> serializersById = new IntHashMap<>();
    private static final SerialEnbtEntry enbtEntrySerial = new SerialEnbtEntry();

    static class EnbtRegistryEntry {

        private final EnbtSerializer serial;
        private final int type;
        private final boolean def;

        public EnbtRegistryEntry(EnbtSerializer<?,?> serial, int type, boolean def) {
            this.serial = serial;
            this.type = type;
            this.def = def;
        }
    }

    static {
        // Register the default mc types
        registerDefault(1, Byte.class, new SerialByte());
        registerDefault(2, Short.class, new SerialShort());
        registerDefault(3, Integer.class, new SerialInteger());
        registerDefault(4, Long.class, new SerialLong());
        registerDefault(5, Float.class, new SerialFloat());
        registerDefault(6, Double.class, new SerialDouble());
        registerDefault(7, byte[].class, new SerialByteArray());
        registerDefault(8, String.class, new SerialString());
        // We won't register the nbt tag compound, because we
        // need the null values to check whether a object a view is
        registerDefault(10, Collection.class, new SerialCollection());
        registerDefault(11, int[].class, new SerialIntArray());

        // Types that we provide
        register(19, EnbtEntry.class, enbtEntrySerial);
        register(20, Boolean.class, new SerialBoolean());
        register(21, boolean[].class, new SerialBooleanArray());
        register(22, Boolean[].class, new SerialBoxedBooleanArray());
        register(23, short[].class, new SerialShortArray());
        register(24, Short[].class, new SerialBoxedShortArray());
        register(25, float[].class, new SerialFloatArray());
        register(26, Float[].class, new SerialBoxedFloatArray());
        register(27, double[].class, new SerialDoubleArray());
        register(28, Double[].class, new SerialBoxedDoubleArray());
        register(29, long[].class, new SerialLongArray());
        register(30, Long[].class, new SerialBoxedLongArray());
        register(31, String[].class, new SerialStringArray());
        register(32, Byte[].class, new SerialBoxedByteArray());
        register(33, Integer[].class, new SerialBoxedIntArray());
        register(34, EnbtDataContainer.class, new SerialEnbtDataContainer());
        register(35, Map.class, new SerialMap());
    }

    private static <T> void registerDefault(int type, Class<T> objectType, EnbtSerializer<T, ?> serializer) {
        register(type, objectType, serializer, true);
    }

    private static <T> void register(int type, Class<T> objectType, EnbtSerializer<T, ?> serializer) {
        register(type, objectType, serializer, false);
    }

    private static <T> void register(int type, Class<T> objectType, EnbtSerializer<T, ?> serializer, boolean def) {
        EnbtRegistryEntry entry = new EnbtRegistryEntry(serializer, type, def);
        serializersByClass.put(objectType, entry);
        serializersById.a(type, entry);
    }

    /**
     * Serializes the object as a {@link EnbtEntry}.
     * 
     * @param object the object
     * @return the enbt entry
     */
    @Nullable
    public static EnbtEntry serializeAsEntry(Object object) {
        if (object instanceof EnbtEntry) {
            return (EnbtEntry) object;
        }
        EnbtRegistryEntry entry = serializersByClass.get(object.getClass());
        if (entry == null) {
            return null;
        }
        EnbtSerializerData data = entry.serial.serialize(ctx, object);
        return new EnbtEntry(data.getTag(), entry.def ? null : entry.type, data.getExtraData());
    }

    /**
     * Serializes the object as a {@link NBTBase}, this tag will
     * also contain enbt entry data.
     * 
     * @param object the object
     * @return the nbt base
     */
    @Nullable
    public static NBTBase serialize(Object object) {
        EnbtEntry entry = serializeAsEntry(object);
        if (entry == null) {
            return null;
        }
        return enbtEntrySerial.serialize(ctx, entry).getTag();
    }

    /**
     * Deserializes a object from the {@link EnbtEntry}.
     * 
     * @param entry the enbt entry
     * @return the object
     */
    @Nullable
    public static Object deserializeFromEntry(EnbtEntry entry) {
        EnbtRegistryEntry registryEntry = serializersById.get(entry.getType());
        if (registryEntry == null) {
            return null;
        }
        return registryEntry.serial.deserialize(ctx, new EnbtSerializerData(entry.getTag(),
                entry.getExtraData()));
    }

    /**
     * Deserializes a object from the {@link NBTBase}.
     * 
     * @param nbt the nbt base
     * @return the object
     */
    @Nullable
    public static Object deserialize(NBTBase nbt) {
        EnbtEntry entry = enbtEntrySerial.deserialize(ctx, new EnbtSerializerData(nbt));
        return deserializeFromEntry(entry);
    }

}
