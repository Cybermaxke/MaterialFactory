package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class SerialDoubleArray implements EnbtSerializer<double[], NBTTagList> {

    @Override
    public EnbtSerializerData<NBTTagList> serialize(EnbtSerializerContext ctx, double[] object) {
        NBTTagList tag = new NBTTagList();
        for (int i = 0; i < object.length; i++) {
            tag.add(new NBTTagDouble(object[i]));
        }
        return new EnbtSerializerData<>(tag);
    }

    @Override
    public double[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagList> tag) {
        double[] object = new double[tag.getTag().size()];
        for (int i = 0; i < object.length; i++) {
            object[i] = ((NBTTagDouble) tag.getTag().g(i)).g();
        }
        return object;
    }

}
