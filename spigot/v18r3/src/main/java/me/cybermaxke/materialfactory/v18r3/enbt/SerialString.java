package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagString;

public class SerialString implements EnbtSerializer<String, NBTTagString> {

    @Override
    public EnbtSerializerData<NBTTagString> serialize(EnbtSerializerContext ctx, String object) {
        return new EnbtSerializerData<>(new NBTTagString(object));
    }

    @Override
    public String deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagString> tag) {
        return tag.getTag().a_();
    }

}
