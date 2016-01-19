package me.cybermaxke.materialfactory.v18r3.interfaces;

import net.minecraft.server.v1_8_R3.NBTBase;

import java.util.Map;

public interface IMixinNBTTagCompound {

    Map<String, NBTBase> getBacking();
}
