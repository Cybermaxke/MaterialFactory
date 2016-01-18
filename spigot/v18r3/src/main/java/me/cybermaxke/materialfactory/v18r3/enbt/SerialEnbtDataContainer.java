package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class SerialEnbtDataContainer implements EnbtSerializer<EnbtDataContainer, NBTTagCompound> {

    @Override
    public EnbtSerializerData<NBTTagCompound> serialize(EnbtSerializerContext ctx, EnbtDataContainer object) {
        return new EnbtSerializerData<>(((EnbtDataContainer) object).getTag());
    }

    @Override
    public EnbtDataContainer deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTTagCompound> tag) {
        return EnbtDataContainer.create(tag.getTag());
    }

}
