package me.cybermaxke.materialfactory.v18r3;

import com.google.common.collect.MapMaker;
import me.cybermaxke.materialfactory.api.ItemData;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface IItemData extends ItemData {

    String extraDataKey = "cybm_ifd";
    Map<ItemMeta, NBTTagCompound> dataContainers = new MapMaker().weakKeys().makeMap();

    Map<String, NBTBase> getUnhandledTags();

}
