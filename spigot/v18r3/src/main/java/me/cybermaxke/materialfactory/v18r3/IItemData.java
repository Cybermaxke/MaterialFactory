package me.cybermaxke.materialfactory.v18r3;

import java.util.Map;

import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.MapMaker;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import me.cybermaxke.materialfactory.api.ItemData;
import me.cybermaxke.materialfactory.api.data.DataContainer;
import me.cybermaxke.materialfactory.api.data.DataQuery;
import me.cybermaxke.materialfactory.v18r3.enbt.EnbtDataContainer;
import static me.cybermaxke.materialfactory.v18r3.SMaterialFactory.unhandledTagsField;

@SuppressWarnings("unchecked")
public interface IItemData extends ItemData {

    String extraDataKey = "cybm_ifd";
    Map<ItemMeta, NBTTagCompound> dataContainers = new MapMaker().weakKeys().makeMap();

    default Map<String, NBTBase> getUnhandledTags() {
        try {
            if (unhandledTagsField == null) {
                unhandledTagsField = Class.forName("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemMeta")
                        .getDeclaredField("unhandledTags");
                unhandledTagsField.setAccessible(true);
            }
            return (Map<String, NBTBase>) unhandledTagsField.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    default DataContainer getDataContainer() {
        if (dataContainers.containsKey(extraDataKey)) {
            return EnbtDataContainer.create(dataContainers.get(extraDataKey));
        }
        Map<String, NBTBase> map = this.getUnhandledTags();
        NBTTagCompound tag;
        if (map.containsKey(extraDataKey)) {
            // We need to clone the tag to make sure that it's not used multiple times
            tag = (NBTTagCompound) map.get(extraDataKey).clone();
        } else {
            // Create the new data compound
            tag = new NBTTagCompound();
        }
        dataContainers.put(this, tag);
        map.put(extraDataKey, tag);
        return EnbtDataContainer.create(tag);
    }

    @Override
    default void setDataContainer(DataContainer dataContainer) {
        EnbtDataContainer dataContainer0 = EnbtDataContainer.create();
        for (DataQuery query : dataContainer.getKeys(false)) {
            dataContainer0.set(query, dataContainer.get(query).get());
        }
        this.getUnhandledTags().put(extraDataKey, dataContainer0.getTag());
        dataContainers.put(this, dataContainer0.getTag());
    }

}
