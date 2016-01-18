package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class SerialStringArray implements EnbtSerializer<String[], NBTTagList> {

    @Override
    public EnbtSerializerData<NBTTagList> serialize(EnbtSerializerContext ctx, String[] object) {
        NBTTagList tag = new NBTTagList();
        for (int i = 0; i < object.length; i++) {
            tag.add(new NBTTagString(object[i]));
        }
        return new EnbtSerializerData<>(tag);
    }

    @Override
    public String[] deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagList> tag) {
        String[] object = new String[tag.getTag().size()];
        for (int i = 0; i < object.length; i++) {
            object[i] = tag.getTag().getString(i);
        }
        return object;
    }

}
