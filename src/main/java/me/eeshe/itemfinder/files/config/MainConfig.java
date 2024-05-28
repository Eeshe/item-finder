package me.eeshe.itemfinder.files.config;

import me.eeshe.penpenlib.files.config.ConfigWrapper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainConfig extends ConfigWrapper {
    private static final String ITEM_BLACKLIST_PATH = "item-blacklist";
    private static final String WORLD_BLACKLIST_PATH = "world-blacklist";
    private static final String REGION_BLACKLIST_PATH = "region-blacklist";

    private static final String SEARCH_RADIUS_PATH = "search-radius";
    private static final String SEARCHED_CHUNKS_PER_TICK_PATH = "searched-chunks-per-tick";
    private static final String ITEM_FOUND_EFFECTS_ITERATIONS_PATH = "item-found-effects-iterations";
    private static final String SEARCH_COOLDOWN_PATH = "search-cooldown";

    public MainConfig(Plugin plugin) {
        super(plugin, null, "config.yml");
    }

    @Override
    public void writeDefaults() {
        writeBlacklistDefaults();
        writeSearchDefaults();

        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
    }

    /**
     * Writes the default settings corresponding to blacklists.
     */
    private void writeBlacklistDefaults() {
        FileConfiguration config = getConfig();

        config.setComments(ITEM_BLACKLIST_PATH, List.of("Items that can't be searched."));
        config.addDefault(ITEM_BLACKLIST_PATH, List.of(
                Material.ENCHANTED_GOLDEN_APPLE.name()
        ));

        config.setComments(WORLD_BLACKLIST_PATH, List.of("Worlds where items can't be searched."));
        config.addDefault(WORLD_BLACKLIST_PATH, List.of(
                "world_the_end"
        ));

        config.setComments(REGION_BLACKLIST_PATH, List.of("WorldGuard regions where items can't be searched."));
        config.addDefault(REGION_BLACKLIST_PATH, List.of(
                "region1",
                "region2"
        ));
    }

    /**
     * Writes the default settings corresponding to item searches.
     */
    private void writeSearchDefaults() {
        FileConfiguration config = getConfig();

        config.setComments(SEARCH_RADIUS_PATH, List.of("Radius (in blocks) around the player where items will be searched."));
        config.addDefault(SEARCH_RADIUS_PATH, 10);

        config.setComments(SEARCHED_CHUNKS_PER_TICK_PATH, List.of("Amount of chunks that will be searched for items per tick.", "The higher the amount the fastest the search and the highest the impact on the server."));
        config.addDefault(SEARCHED_CHUNKS_PER_TICK_PATH, 9);

        config.setComments(ITEM_FOUND_EFFECTS_ITERATIONS_PATH, List.of("Amount of times the item found particles will be shown on chests with the searched item."));
        config.addDefault(ITEM_FOUND_EFFECTS_ITERATIONS_PATH, 5);

        config.setComments(SEARCH_COOLDOWN_PATH, List.of("Time (in seconds) between each item search."));
        config.addDefault(SEARCH_COOLDOWN_PATH, 5);
    }

    /**
     * Checks if the passed Material is configured in the item blacklist.
     *
     * @param itemType Material to check.
     * @return True if the passed Material is configured in the item blacklist.
     */
    public boolean isBlacklistedItem(Material itemType) {
        return getConfig().getStringList(ITEM_BLACKLIST_PATH).contains(itemType.name());
    }

    /**
     * Checks if the passed World is configured in the world blacklist.
     *
     * @param world World to check.
     * @return True if the passed World is configured in the world blacklist.
     */
    public boolean isBlacklistedWorld(World world) {
        if (world == null) return false;

        return getConfig().getStringList(WORLD_BLACKLIST_PATH).contains(world.getName());
    }

    /**
     * Checks if the passed region ID is configured in the region blacklist.
     *
     * @param regionId Region ID to check/
     * @return True if the passed region ID is configured in the region blacklist.
     */
    public boolean isBlacklistedRegion(String regionId) {
        return getConfig().getStringList(REGION_BLACKLIST_PATH).contains(regionId);
    }

    /**
     * Returns the configured item search radius in blocks.
     *
     * @return Configured item search radius in blocks.
     */
    public int getSearchRadiusBlocks() {
        return getConfig().getInt(SEARCH_RADIUS_PATH);
    }

    /**
     * Returns the configured amount of chunks to search each tick for items.
     *
     * @return Configured amount of chunks to search each tick for items.
     */
    public int getSearchedChunksPerTick() {
        return getConfig().getInt(SEARCHED_CHUNKS_PER_TICK_PATH);
    }

    /**
     * Returns the configured amount of times the effects will play once an item is found.
     *
     * @return Configured amount of times the effects will play once an item is found.
     */
    public int getItemFoundEffectIterations() {
        return getConfig().getInt(ITEM_FOUND_EFFECTS_ITERATIONS_PATH);
    }

    /**
     * Returns the configured search cooldown in milliseconds.
     *
     * @return Configured search cooldown in milliseconds.
     */
    public long getSearchCooldownMillis() {
        return TimeUnit.SECONDS.toMillis(getConfig().getLong(SEARCH_COOLDOWN_PATH));
    }
}
