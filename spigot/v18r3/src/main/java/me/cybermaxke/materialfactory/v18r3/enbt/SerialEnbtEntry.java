package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class SerialEnbtEntry implements EnbtSerializer<EnbtEntry, NBTBase> {

    private static final String secret = "^$ù";

    @Override
    public EnbtSerializerData<NBTBase> serialize(EnbtSerializerContext ctx, EnbtEntry object) {
        Integer type = object.getExtendedType();
        int[] extraData = object.getExtraData();
        if (type != null || extraData.length > 0) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte(secret, (byte) object.getType());
            tag.set("v", object.getTag());
            if (extraData.length > 0) {
                tag.setIntArray("e", extraData);
            }
            return new EnbtSerializerData<>(tag);
        }
        return new EnbtSerializerData<>(object.getTag());
    }

    @Override
    public EnbtEntry deserialize(EnbtSerializerContext ctx, EnbtSerializerData<NBTBase> tag) {
        NBTBase base = tag.getTag();
        if (base instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) base;
            if (compound.hasKey(secret)) {
                int type = compound.getByte(secret) & 0xff;
                int[] extraData;
                if (compound.hasKey("e")) {
                    extraData = compound.getIntArray("e");
                } else {
                    extraData = new int[0];
                }
                return new EnbtEntry(compound.get("v"), type, extraData);
            }
        }
        return new EnbtEntry(base, null);
    }

}
