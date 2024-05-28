package me.eeshe.itemfinder.models;

import me.eeshe.itemfinder.ItemFinder;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SearchPlayer {
    private final UUID uuid;
    private long searchCooldownMillis;

    public SearchPlayer(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
    }

    public SearchPlayer(OfflinePlayer player, long searchCooldownMillis) {
        this.uuid = player.getUniqueId();
        this.searchCooldownMillis = searchCooldownMillis;
    }

    /**
     * Searches the SearchPlayer corresponding to the passed player.
     *
     * @param player Player to search.
     * @return SearchPlayer corresponding to the passed player.
     */
    public static SearchPlayer fromPlayer(Player player) {
        return ItemFinder.getInstance().getSearchPlayerManager().getSearchPlayers().get(player.getUniqueId());
    }

    /**
     * Registers the SearchPlayer in the SearchPlayerManager class.
     */
    public void register() {
        ItemFinder.getInstance().getSearchPlayerManager().getSearchPlayers().put(uuid, this);
    }

    /**
     * Unregisters the SearchPlayer from the SearchPlayerManager class.
     */
    public void unregister() {
        ItemFinder.getInstance().getSearchPlayerManager().getSearchPlayers().remove(uuid);
    }

    /**
     * Saves the SearchPlayer data.
     */
    private void saveData() {
        ItemFinder.getInstance().getSearchPlayerManager().save(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getSearchCooldownMillis() {
        return searchCooldownMillis;
    }

    public void setSearchCooldownMillis(long searchCooldownMillis) {
        this.searchCooldownMillis = System.currentTimeMillis() + searchCooldownMillis;
        saveData();
    }

    public boolean hasSearchCooldown() {
        return getRemainingSearchCooldownMillis() > 0;
    }

    public long getRemainingSearchCooldownMillis() {
        return searchCooldownMillis - System.currentTimeMillis();
    }
}
