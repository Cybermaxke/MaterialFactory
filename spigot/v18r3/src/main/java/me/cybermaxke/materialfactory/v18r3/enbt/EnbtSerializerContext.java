package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTBase;

public class EnbtSerializerContext {

    /**
     * Serializes the object as a {@link EnbtEntry}.
     * 
     * @param object the object
     * @return the enbt entry
     */
    public EnbtEntry serializeAsEntry(Object object) {
        return EnbtSerializers.serializeAsEntry(object);
    }

    /**
     * Serializes the object as a {@link NBTBase}, this tag will
     * also contain enbt entry data.
     * 
     * @param object the object
     * @return the nbt base
     */
    public NBTBase serialize(Object object) {
        return EnbtSerializers.serialize(object);
    }

    /**
     * Deserializes a object from the {@link EnbtEntry}.
     * 
     * @param entry the enbt entry
     * @return the object
     */
    public Object deserializeFromEntry(EnbtEntry entry) {
        return EnbtSerializers.deserializeFromEntry(entry);
    }

    /**
     * Deserializes a object from the {@link NBTBase}.
     * 
     * @param nbt the nbt base
     * @return the object
     */
    public Object deserialize(NBTBase nbt) {
        return EnbtSerializers.deserialize(nbt);
    }

}
