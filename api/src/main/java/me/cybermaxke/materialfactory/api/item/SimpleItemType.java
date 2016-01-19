package me.cybermaxke.materialfactory.api.item;

import static com.google.common.base.Preconditions.checkNotNull;

import me.cybermaxke.materialfactory.api.inventory.ExtendedItemStack;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;

import java.util.Optional;

import javax.annotation.Nullable;

/**
 * This is the default implementation that developers can use to create
 * custom item types.
 */
public class SimpleItemType implements ItemType {

    /**
     * The maximum stack size of the item type.
     */
    private int maxStackSize = 64;

    /**
     * The name that will be displayed on the client.
     */
    private BaseComponent name = new TranslatableComponent("missing-name");

    /**
     * The action handler that is attached to this item type.
     */
    private Optional<ItemActionHandler> actionHandler = Optional.empty();

    /**
     * The item that will be shown on the client.
     */
    private RepresentedItem viewedItem = new RepresentedItem(Material.STONE);

    /**
     * Sets the name that will be displayed on the client.
     *
     * @param name the name
     */
    public void setName(BaseComponent name) {
        this.name = checkNotNull(name, "name");
    }

    @Override
    public BaseComponent getName() {
        return this.name;
    }

    @Override
    public BaseComponent getNameFor(ExtendedItemStack itemStack) {
        return this.name;
    }

    /**
     * Sets the maximum stack size of this item type.
     *
     * @param maxStackSize the max stack size
     */
    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    /**
     * Sets the {@link ItemActionHandler} of this item type.
     *
     * @param actionHandler the action handler
     */
    public void setActionHandler(@Nullable ItemActionHandler actionHandler) {
        if (this.actionHandler.isPresent() == (actionHandler != null)) {
            this.actionHandler = Optional.ofNullable(actionHandler);
        }
    }

    @Override
    public Optional<ItemActionHandler> getActionHandler() {
        return this.actionHandler;
    }

    /**
     * Sets the {@link RepresentedItem} that will be shown on the client.
     *
     * @param viewedItem the viewed item
     */
    public void setViewedItem(RepresentedItem viewedItem) {
        this.viewedItem = checkNotNull(viewedItem, "viewedItem");
    }

    @Override
    public RepresentedItem getViewedItem() {
        return this.viewedItem;
    }

}
