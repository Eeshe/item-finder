package me.eeshe.itemfinder.util;

import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.itemfinder.models.config.Particle;
import me.eeshe.penpenlib.models.Scheduler;
import me.eeshe.penpenlib.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleUtil {

    /**
     * Spawns a particle line between the two locations.
     *
     * @param player              Player the particles will be spawned for.
     * @param source              Source of the line.
     * @param destination         Destination of the line.
     * @param particle            Particle to spawn.
     * @param points              Amount of particle points between the two locations.
     * @param delayIncrementTicks Delay in between each particle.
     */
    public static void spawnParticleLine(Player player, Location source, Location destination, Particle particle,
                                         int points, long delayIncrementTicks) {
        long delay = 0L;
        for (Location location : LocationUtil.calculateLocationsBetween(source, destination, points)) {
            Scheduler.runLater(ItemFinder.getInstance(), location, () -> particle.spawn(player, location), delay);
            delay += delayIncrementTicks;
        }
    }
}
