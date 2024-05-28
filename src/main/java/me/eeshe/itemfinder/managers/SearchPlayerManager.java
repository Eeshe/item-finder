package me.eeshe.itemfinder.managers;

import me.eeshe.itemfinder.files.SearchPlayerFile;
import me.eeshe.itemfinder.models.SearchPlayer;
import me.eeshe.penpenlib.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SearchPlayerManager extends DataManager {
    private final Map<UUID, SearchPlayer> searchPlayers = new HashMap<>();

    public SearchPlayerManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        loadAll();
    }

    /**
     * Loads all the online SearchPlayers.
     */
    private void loadAll() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            fetch(online).register();
        }
    }

    /**
     * Fetches the SearchPlayer corresponding to the passed OfflinePlayer.
     *
     * @param player OfflinePlayer to search.
     * @return SearchPlayer corresponding to the passed OfflinePlayer.
     */
    public SearchPlayer fetch(OfflinePlayer player) {
        SearchPlayerFile searchPlayerFile = new SearchPlayerFile(player);
        FileConfiguration playerData = searchPlayerFile.getData();
        if (playerData.getKeys(true).isEmpty()) {
            return new SearchPlayer(player);
        }
        long searchCooldownMillis = playerData.getLong("search-cooldown");

        return new SearchPlayer(player, searchCooldownMillis);
    }

    @Override
    public void unload() {
        unloadAll();
    }

    /**
     * Unloads all the online SearchPlayers.
     */
    private void unloadAll() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            SearchPlayer.fromPlayer(online).unregister();
        }
    }

    /**
     * Saves the passed SearchPlayer.
     *
     * @param searchPlayer SearchPlayer to save.
     */
    public void save(SearchPlayer searchPlayer) {
        SearchPlayerFile searchPlayerFile = new SearchPlayerFile(searchPlayer);
        FileConfiguration playerData = searchPlayerFile.getData();

        playerData.set("search-cooldown", searchPlayer.getSearchCooldownMillis());

        searchPlayerFile.save();
    }

    public Map<UUID, SearchPlayer> getSearchPlayers() {
        return searchPlayers;
    }
}
