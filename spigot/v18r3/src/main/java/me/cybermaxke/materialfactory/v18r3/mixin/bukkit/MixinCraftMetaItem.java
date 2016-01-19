package me.cybermaxke.materialfactory.v18r3.mixin.bukkit;

import static me.cybermaxke.materialfactory.common.ItemFactoryConstants.CUSTOM_DATA;

import me.cybermaxke.materialfactory.api.data.DataContainer;
import me.cybermaxke.materialfactory.api.data.DataQuery;
import me.cybermaxke.materialfactory.v18r3.interfaces.IMixinItemMeta;
import me.cybermaxke.materialfactory.v18r3.enbt.EnbtDataContainer;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(targets = "org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaItem")
public abstract class MixinCraftMetaItem implements ItemMeta, Repairable, IMixinItemMeta {

    @Shadow(remap = false) private Map<String, NBTBase> unhandledTags;
    @Override public abstract MixinCraftMetaItem clone();
    @Shadow(remap = false) abstract void applyToItem(NBTTagCompound tagCompound);

    private DataContainer dataContainer;

    @Override
    public Map<String, NBTBase> getUnhandledTags() {
        return this.unhandledTags;
    }

    @Override
    public void applyDataToItem(NBTTagCompound tagCompound) {
        this.applyToItem(tagCompound);
    }

    @Override
    public DataContainer getDataContainer() {
        if (this.dataContainer != null) {
            return this.dataContainer;
        }
        NBTTagCompound tag;
        if (this.unhandledTags.containsKey(CUSTOM_DATA)) {
            // We need to clone the tag to make sure that it's not used multiple times
            tag = (NBTTagCompound) this.unhandledTags.get(CUSTOM_DATA).clone();
        } else {
            // Create the new data compound
            tag = new NBTTagCompound();
        }
        this.dataContainer = EnbtDataContainer.create(tag);
        this.unhandledTags.put(CUSTOM_DATA, tag);
        return this.dataContainer;
    }

    @Override
    public void setDataContainer(DataContainer dataContainer) {
        final EnbtDataContainer dataContainer0 = EnbtDataContainer.create();
        for (DataQuery query : dataContainer.getKeys(false)) {
            dataContainer0.set(query, dataContainer.get(query).get());
        }
        this.unhandledTags.put(CUSTOM_DATA, dataContainer0.getTag());
        this.dataContainer = dataContainer0;
    }

}
