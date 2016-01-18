package me.cybermaxke.materialfactory.v18r3.mixin.bukkit;

import static me.cybermaxke.materialfactory.v18r3.SMaterialFactory.unhandledTagsField;

import me.cybermaxke.materialfactory.api.ItemData;
import me.cybermaxke.materialfactory.api.data.DataContainer;
import me.cybermaxke.materialfactory.api.data.DataQuery;
import me.cybermaxke.materialfactory.v18r3.IItemData;
import me.cybermaxke.materialfactory.v18r3.enbt.EnbtDataContainer;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(targets = "org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaItem")
public abstract class MixinCraftMetaItem implements ItemMeta, IItemData {

    @Shadow(remap = false) private Map<String, NBTBase> unhandledTags;

    @Override
    public Map<String, NBTBase> getUnhandledTags() {
        return this.unhandledTags;
    }

    @Redirect(method = "applyToItem(Lnet/minecraft/server/v1_8_R3/NBTTagCompound;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/Map/Entry;getValue()Lnet/minecraft/server/v1_8_R3/NBTBase;"), remap = false)
    private NBTBase onApplyToItemStack(Map.Entry<String, NBTBase> entry) {
        return entry.getValue().clone();
    }

    @Override
    public DataContainer getDataContainer() {
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
    public void setDataContainer(DataContainer dataContainer) {
        EnbtDataContainer dataContainer0 = EnbtDataContainer.create();
        for (DataQuery query : dataContainer.getKeys(false)) {
            dataContainer0.set(query, dataContainer.get(query).get());
        }
        this.getUnhandledTags().put(extraDataKey, dataContainer0.getTag());
        dataContainers.put(this, dataContainer0.getTag());
    }

}
