package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagFloat;

public class SerialFloat implements EnbtSerializer<Float, NBTTagFloat> {

    @Override
    public EnbtSerializerData<NBTTagFloat> serialize(EnbtSerializerContext ctx, Float object) {
        return new EnbtSerializerData<>(new NBTTagFloat(object));
    }

    @Override
    public Float deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagFloat> tag) {
        return tag.getTag().h();
    }

}
