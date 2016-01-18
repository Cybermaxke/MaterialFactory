package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagFloat;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class SerialFloatArray implements EnbtSerializer<float[], NBTTagList> {

    @Override
    public EnbtSerializerData<NBTTagList> serialize(EnbtSerializerContext ctx, float[] object) {
        NBTTagList tag = new NBTTagList();
        for (int i = 0; i < object.length; i++) {
            tag.add(new NBTTagFloat(object[i]));
        }
        return new EnbtSerializerData<>(tag);
    }

    @Override
    public float[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagList> tag) {
        float[] object = new float[tag.getTag().size()];
        for (int i = 0; i < object.length; i++) {
            object[i] = ((NBTTagFloat) tag.getTag().g(i)).h();
        }
        return object;
    }

}
