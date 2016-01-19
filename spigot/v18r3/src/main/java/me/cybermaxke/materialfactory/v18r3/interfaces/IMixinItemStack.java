package me.cybermaxke.materialfactory.v18r3.interfaces;

import me.cybermaxke.materialfactory.api.inventory.ExtendedItemStack;
import me.cybermaxke.materialfactory.api.item.ItemType;

public interface IMixinItemStack extends ExtendedItemStack {

    ItemType getIItemType();

    void setIItemType(ItemType itemType);

}
