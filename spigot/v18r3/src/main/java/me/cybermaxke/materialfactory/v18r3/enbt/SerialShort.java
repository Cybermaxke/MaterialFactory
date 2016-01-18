package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagShort;

public class SerialShort implements EnbtSerializer<Short, NBTTagShort> {

    @Override
    public EnbtSerializerData<NBTTagShort> serialize(EnbtSerializerContext ctx, Short object) {
        return new EnbtSerializerData<>(new NBTTagShort(object));
    }

    @Override
    public Short deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagShort> tag) {
        return tag.getTag().e();
    }
}
