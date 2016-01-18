package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagShort;

public class SerialShortArray implements EnbtSerializer<short[], NBTTagList> {

    @Override
    public EnbtSerializerData<NBTTagList> serialize(EnbtSerializerContext ctx, short[] object) {
        NBTTagList tag = new NBTTagList();
        for (int i = 0; i < object.length; i++) {
            tag.add(new NBTTagShort(object[i]));
        }
        return new EnbtSerializerData<>(tag);
    }

    @Override
    public short[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagList> tag) {
        short[] object = new short[tag.getTag().size()];
        for (int i = 0; i < object.length; i++) {
            object[i] = ((NBTTagShort) tag.getTag().g(i)).e();
        }
        return object;
    }

}
