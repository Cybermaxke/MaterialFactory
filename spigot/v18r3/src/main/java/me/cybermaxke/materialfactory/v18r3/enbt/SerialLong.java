package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagLong;

public class SerialLong implements EnbtSerializer<Long, NBTTagLong> {

    @Override
    public EnbtSerializerData<NBTTagLong> serialize(EnbtSerializerContext ctx, Long object) {
        return new EnbtSerializerData<>(new NBTTagLong(object));
    }

    @Override
    public Long deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagLong> tag) {
        return tag.getTag().c();
    }

}
