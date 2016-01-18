package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagByteArray;

public class SerialBoxedByteArray implements EnbtSerializer<Byte[], NBTTagByteArray> {

    @Override
    public EnbtSerializerData<NBTTagByteArray> serialize(EnbtSerializerContext ctx, Byte[] object) {
        byte[] array = new byte[object.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = object[i];
        }
        return new EnbtSerializerData<>(new NBTTagByteArray(array));
    }

    @Override
    public Byte[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagByteArray> tag) {
        byte[] array = tag.getTag().c();
        Byte[] object = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            object[i] = array[i];
        }
        return object;
    }

}
