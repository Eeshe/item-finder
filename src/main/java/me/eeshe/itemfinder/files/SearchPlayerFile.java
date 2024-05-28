package me.eeshe.itemfinder.files;

import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.itemfinder.models.SearchPlayer;
import me.eeshe.penpenlib.files.StorageDataFile;
import org.bukkit.OfflinePlayer;

public class SearchPlayerFile extends StorageDataFile {

    public SearchPlayerFile(SearchPlayer searchPlayer) {
        super(ItemFinder.getInstance().getDataFolder() + "/player_data/" + searchPlayer.getUuid() + ".yml");
    }

    public SearchPlayerFile(OfflinePlayer player) {
        super(ItemFinder.getInstance().getDataFolder() + "/player_data/" + player.getUniqueId() + ".yml");
    }
}
