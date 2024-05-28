package me.eeshe.itemfinder.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Called once an ItemSearch is completed. Can be used to add third party containers from other plugins.
 */
public class ItemSearchEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * Player searching for items.
     */
    private final Player player;
    /**
     * Item that is being search.
     */
    private final Material searchedItem;
    /**
     * Inventories found with the searched item.
     */
    private Set<Inventory> foundInventories;

    public ItemSearchEvent(Player player, Material searchedItem, Set<Inventory> foundInventories) {
        this.player = player;
        this.searchedItem = searchedItem;
        this.foundInventories = foundInventories;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public Material getSearchedItem() {
        return searchedItem;
    }

    public Set<Inventory> getFoundInventories() {
        return foundInventories;
    }

    public void setFoundInventories(Set<Inventory> foundInventories) {
        this.foundInventories = foundInventories;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
