package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagIntArray;

public class SerialBoxedIntArray implements EnbtSerializer<Integer[], NBTTagIntArray> {

    @Override
    public EnbtSerializerData<NBTTagIntArray> serialize(EnbtSerializerContext ctx, Integer[] object) {
        int[] array = new int[object.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = object[i];
        }
        return new EnbtSerializerData<>(new NBTTagIntArray(array));
    }

    @Override
    public Integer[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagIntArray> tag) {
        int[] array = tag.getTag().c();
        Integer[] object = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            object[i] = array[i];
        }
        return object;
    }

}
