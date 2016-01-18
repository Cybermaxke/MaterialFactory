package me.cybermaxke.materialfactory.v18r3.enbt;

import net.minecraft.server.v1_8_R3.NBTBase;

public interface EnbtSerializer<T, N extends NBTBase> {

    EnbtSerializerData<N> serialize(EnbtSerializerContext ctx, T object);

    T deserialize(EnbtSerializerContext ctx, EnbtSerializerData<N> tag);
}
