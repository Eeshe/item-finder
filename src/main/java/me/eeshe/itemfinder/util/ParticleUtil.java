package me.eeshe.itemfinder.util;

import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.itemfinder.models.config.Particle;
import me.eeshe.penpenlib.models.Scheduler;
import me.eeshe.penpenlib.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.World;
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

    /**
     * Spawns the passed particle as circle at the passed location.
     *
     * @param center   Center of the circle.
     * @param radius   Radius of the circle.
     * @param amount   Amount of particles forming the circle.
     * @param particle Particle to spawn.
     */
    public static void spawnParticleCircle(Location center, double radius, int amount, Particle particle) {
        double increment = (2 * Math.PI) / amount;
        World world = center.getWorld();
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            particle.spawn(new Location(world, x, center.getY(), z));
        }
    }
}
