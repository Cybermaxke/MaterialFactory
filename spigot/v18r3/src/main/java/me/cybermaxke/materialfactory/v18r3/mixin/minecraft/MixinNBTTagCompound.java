package me.cybermaxke.materialfactory.v18r3.mixin.minecraft;

import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinNBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(NBTTagCompound.class)
public abstract class MixinNBTTagCompound extends NBTBase implements IMixinNBTTagCompound {

    @Shadow(remap = false) private Map<String, NBTBase> map;

    @Override
    public Map<String, NBTBase> getBacking() {
        return this.map;
    }
}
