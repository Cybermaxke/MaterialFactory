package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagByteArray;

public class SerialBoxedBooleanArray implements EnbtSerializer<Boolean[], NBTTagByteArray> {

    @Override
    public EnbtSerializerData<NBTTagByteArray> serialize(EnbtSerializerContext ctx, Boolean[] object) {
        byte[] array = new byte[object.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (object[i] ? 1 : 0);
        }
        return new EnbtSerializerData<>(new NBTTagByteArray(array));
    }

    @Override
    public Boolean[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagByteArray> tag) {
        byte[] array = tag.getTag().c();
        Boolean[] object = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            object[i] = array[i] != 0;
        }
        return object;
    }

}
