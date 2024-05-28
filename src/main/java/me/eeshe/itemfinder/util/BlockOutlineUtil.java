package me.eeshe.itemfinder.util;

import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.itemfinder.models.config.Particle;
import me.eeshe.penpenlib.models.Scheduler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockOutlineUtil {

    /**
     * Spawns the outline particles around the passed block.
     *
     * @param player   Player the particles will be spawned for.
     * @param block    Block the particles will be around of.
     * @param particle Particle to spawn.
     */
    public static void spawnBlockOutline(Player player, Block block, Particle particle) {
        final double density = 20;
        final double increment = 1 / density;
        final Location location = block.getLocation();
        long delay = 0L;
        for (double x = 0; x <= 1; x += increment) {
            spawnParticle(player, location.clone().add(x, 0, 0), delay, particle);
            spawnParticle(player, location.clone().add(x, 0, 1), delay, particle);
            spawnParticle(player, location.clone().add(x, 1, 0), delay, particle);
            spawnParticle(player, location.clone().add(x, 1, 1), delay, particle);
            delay += 1L;
        }
        delay = 0L;
        for (double y = 0; y <= 1; y += increment) {
            spawnParticle(player, location.clone().add(0, y, 0), delay, particle);
            spawnParticle(player, location.clone().add(0, y, 1), delay, particle);
            spawnParticle(player, location.clone().add(1, y, 0), delay, particle);
            spawnParticle(player, location.clone().add(1, y, 1), delay, particle);
            delay += 1L;
        }
        delay = 0L;
        for (double z = 0; z <= 1; z += increment) {
            spawnParticle(player, location.clone().add(0, 0, z), delay, particle);
            spawnParticle(player, location.clone().add(0, 1, z), delay, particle);
            spawnParticle(player, location.clone().add(1, 0, z), delay, particle);
            spawnParticle(player, location.clone().add(1, 1, z), delay, particle);
            delay += 1L;
        }
    }

    /**
     * Spawns the timber particle in the passed location for the passed player.
     *
     * @param player   Player the particle will be spawned for.
     * @param location Location the particle will be spawned in.
     * @param delay    Delay the particle will be spawned with.
     * @param particle Particle to spawn.
     */
    private static void spawnParticle(Player player, Location location, long delay, Particle particle) {
        Scheduler.runLater(ItemFinder.getInstance(), location, () -> particle.spawn(player, location), delay);
    }
}
