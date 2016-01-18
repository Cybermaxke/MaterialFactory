package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagByte;

public class SerialByte implements EnbtSerializer<Byte, NBTTagByte> {

    @Override
    public EnbtSerializerData<NBTTagByte> serialize(EnbtSerializerContext ctx, Byte object) {
        return new EnbtSerializerData<>(new NBTTagByte(object));
    }

    @Override
    public Byte deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagByte> tag) {
        return tag.getTag().f();
    }

}
