package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagIntArray;

public class SerialIntArray implements EnbtSerializer<int[], NBTTagIntArray> {

    @Override
    public EnbtSerializerData<NBTTagIntArray> serialize(EnbtSerializerContext ctx, int[] object) {
        return new EnbtSerializerData<>(new NBTTagIntArray(object));
    }

    @Override
    public int[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagIntArray> tag) {
        return tag.getTag().c();
    }

}
