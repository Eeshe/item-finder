package me.eeshe.itemfinder.listeners;

import me.eeshe.itemfinder.managers.SearchPlayerManager;
import me.eeshe.itemfinder.models.SearchPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionHandler implements Listener {
    private final SearchPlayerManager searchPlayerManager;

    public PlayerConnectionHandler(SearchPlayerManager searchPlayerManager) {
        this.searchPlayerManager = searchPlayerManager;
    }

    /**
     * Listens when a player joins the server and registers its SearchPlayer instance.
     *
     * @param event PlayerJoinEvent.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        searchPlayerManager.fetch(event.getPlayer()).register();
    }

    /**
     * Listens when a player quits the server and unregisters its SearchPlayer instance.
     *
     * @param event PlayerQuitEvent.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SearchPlayer.fromPlayer(event.getPlayer()).unregister();
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        System.out.println(event.getRightClicked());
    }
}
