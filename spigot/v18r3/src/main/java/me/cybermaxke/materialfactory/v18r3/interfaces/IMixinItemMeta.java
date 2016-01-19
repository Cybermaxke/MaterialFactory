package me.cybermaxke.materialfactory.v18r3.interfaces;

import me.cybermaxke.materialfactory.api.inventory.ItemData;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface IMixinItemMeta extends ItemData {

    /**
     * Gets the unhandled tags from the item meta.
     *
     * @return the unhandled tags
     */
    Map<String, NBTBase> getUnhandledTags();

    void applyDataToItem(NBTTagCompound tagCompound);
}
