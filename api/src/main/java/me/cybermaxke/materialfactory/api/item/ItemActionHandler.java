package me.cybermaxke.materialfactory.api.item;

import me.cybermaxke.materialfactory.api.inventory.ExtendedItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface ItemActionHandler {

    /**
     * Called when a {@link Player} interacts with a {@link LivingEntity} with
     * the specified {@link ExtendedItemStack}. And returns whether a action
     * was successfully executed.
     *
     * @param itemStack the item stack
     * @param player the player
     * @param livingEntity the living entity
     * @return whether the action was successful
     */
    boolean onInteractWithEntity(ExtendedItemStack itemStack, Player player, LivingEntity livingEntity);

}
