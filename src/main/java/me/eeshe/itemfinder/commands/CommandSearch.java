package me.eeshe.itemfinder.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.itemfinder.events.ItemSearchEvent;
import me.eeshe.itemfinder.files.config.MainConfig;
import me.eeshe.itemfinder.models.SearchPlayer;
import me.eeshe.itemfinder.models.config.Message;
import me.eeshe.itemfinder.models.config.Particle;
import me.eeshe.itemfinder.models.config.Sound;
import me.eeshe.itemfinder.util.BlockOutlineUtil;
import me.eeshe.itemfinder.util.ParticleUtil;
import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;
import me.eeshe.penpenlib.models.Scheduler;
import me.eeshe.penpenlib.models.config.CommonMessage;
import me.eeshe.penpenlib.models.config.CommonSound;
import me.eeshe.penpenlib.util.StringUtil;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.*;
import java.util.function.Supplier;

public class CommandSearch extends PenCommand {
    private static final List<String> SEARCHABLE_ITEM_NAMES;

    static {
        MainConfig mainConfig = ItemFinder.getInstance().getMainConfig();

        SEARCHABLE_ITEM_NAMES = Arrays.stream(Material.values()).filter(material -> !mainConfig.isBlacklistedItem(material))
                .map(Material::name).toList();
    }

    private final Map<UUID, Scheduler.Task> searchingActionBarTasks = new HashMap<>();
    private final Map<UUID, Scheduler.Task> searchTasks = new HashMap<>();
    private final Map<UUID, Scheduler.Task> containerHighlightTasks = new HashMap<>();

    public CommandSearch(PenPenPlugin plugin, PenCommand parentCommand) {
        super(plugin, parentCommand);

        setName("search");
        setPermission("itemfinder.search");
        setInfoMessage(Message.SEARCH_COMMAND_INFO);
        setUsageMessage(Message.SEARCH_COMMAND_USAGE);
        setArgumentAmount(1);
        setPlayerCommand(true);
        setCompletions(Map.of(0, (sender, strings) -> SEARCHABLE_ITEM_NAMES));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String itemName = args[0];
        Material item = Material.matchMaterial(itemName);
        if (item == null) {
            CommonMessage.ITEM_NOT_FOUND.sendError(sender, Map.of("%item%", itemName));
            return;
        }
        Player player = (Player) sender;
        MainConfig mainConfig = ((ItemFinder) getPlugin()).getMainConfig();
        if (mainConfig.isBlacklistedItem(item)) {
            Message.BLACKLISTED_ITEM.sendError(player, Map.of("%item%", itemName));
            return;
        }
        if (mainConfig.isBlacklistedWorld(player.getWorld())) {
            Message.BLACKLISTED_WORLD.sendError(player);
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            List<String> applicableRegions = WorldGuard.getInstance().getPlatform().getRegionContainer()
                    .get(BukkitAdapter.adapt(player.getWorld()))
                    .getApplicableRegionsIDs(BukkitAdapter.asBlockVector(player.getLocation()));
            if (applicableRegions.stream().anyMatch(mainConfig::isBlacklistedRegion)) {
                Message.BLACKLISTED_REGION.sendError(player);
                return;
            }
        }
        SearchPlayer searchPlayer = SearchPlayer.fromPlayer(player);
        if (searchPlayer.hasSearchCooldown()) {
            Message.SEARCH_COOLDOWN.sendError(player,
                    Map.of("%cooldown%", StringUtil.formatMillis(searchPlayer.getRemainingSearchCooldownMillis())));
            return;
        }
        startInventorySearchTask(player, item);
        searchPlayer.setSearchCooldownMillis(mainConfig.getSearchCooldownMillis());
    }

    /**
     * Starts the task that will search all inventories around the passed location.
     *
     * @param player   Player responsible for the search task.
     * @param itemType Item to search.
     */
    private void startInventorySearchTask(Player player, Material itemType) {
        stopInventorySearchTask(player);
        startSearchActionBarTask(player, itemType);

        MainConfig mainConfig = ((ItemFinder) getPlugin()).getMainConfig();
        int searchRadiusBlocks = mainConfig.getSearchRadiusBlocks();
        int searchRadiusChunks = (int) Math.ceil(searchRadiusBlocks / 16D);
        Location location = player.getLocation();
        int centerX = location.getChunk().getX();
        int centerZ = location.getChunk().getZ();

        int xMax = centerX + searchRadiusChunks;
        int xMin = centerX - searchRadiusChunks;
        int zMax = centerZ + searchRadiusChunks;
        int zMin = centerZ - searchRadiusChunks;

        World world = location.getWorld();
        double searchRadiusBlocksSquared = Math.pow(searchRadiusBlocks, 2);
        boolean isGriefPreventionInstalled = Bukkit.getPluginManager().isPluginEnabled("GriefPrevention");
        Scheduler.Task searchTask = Scheduler.runTimer(getPlugin().getSpigotPlugin(), location, new Runnable() {
            private final Set<Inventory> nearInventories = new HashSet<>();
            private final int searchedChunksPerTick = mainConfig.getSearchedChunksPerTick();

            private int currentX = xMin;
            private int currentZ = zMin;

            @Override
            public void run() {
                int searchedChunks = 0;
                for (int x = currentX; x <= xMax; x++) {
                    for (int z = currentZ; z <= zMax; z++) {
                        if (searchedChunks >= searchedChunksPerTick) {
                            currentX = x;
                            currentZ = z;
                            return;
                        }
                        if (!world.isChunkGenerated(x, z)) continue;

                        searchedChunks += 1;
                        Chunk chunk = world.getChunkAt(x, z);
                        for (BlockState blockState : chunk.getTileEntities(block -> block.getState() instanceof Container, true)) {
                            if (blockState.getLocation().distanceSquared(location) > searchRadiusBlocksSquared) {
                                continue;
                            }
                            if (!canAccessContainer(player, blockState.getLocation())) continue;
                            nearInventories.add(((Container) blockState).getInventory());
                        }
                        for (Entity entity : chunk.getEntities()) {
                            if (entity instanceof Player) continue;
                            if (!(entity instanceof InventoryHolder inventoryHolder)) continue;
                            Location inventoryLocation = inventoryHolder.getInventory().getLocation();
                            if (inventoryLocation == null) continue;
                            if (inventoryLocation.distanceSquared(location) > searchRadiusBlocksSquared) continue;
                            if (!canAccessContainer(player, inventoryLocation)) continue;

                            nearInventories.add(inventoryHolder.getInventory());
                        }
                    }
                    currentZ = zMin;
                }
                stopInventorySearchTask(player);
                filterInventories(player, itemType, nearInventories);
                startContainerHighlightTask(player, nearInventories);
            }
        }, 0L, 1L);
        this.searchTasks.put(player.getUniqueId(), searchTask);
    }

    /**
     * Checks if a player can access a container at a given location.
     *
     * @param player   The player to check access for.
     * @param location The location of the container.
     * @return True if the player can access the container, false otherwise.
     */
    private boolean canAccessContainer(Player player, Location location) {
        if (!Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            return true;
        }
        GriefPrevention griefPrevention = GriefPrevention.instance;

        // Check if claims prevent theft is enabled in GriefPrevention config
        if (!griefPrevention.config_claims_preventTheft) {
            return true;
        }
        DataStore dataStore = griefPrevention.dataStore;
        Claim cachedClaim = dataStore.getPlayerData(player.getUniqueId()).lastClaim;
        Claim claim = dataStore.getClaimAt(location, false, cachedClaim);

        // If there is no claim at the location, the player can access the container
        if (claim == null) {
            return true;
        }
        Supplier<String> noContainersReason = claim.checkPermission(player, ClaimPermission.Inventory, null);

        // Return true if the player has permission, false otherwise
        return noContainersReason == null;
    }

    /**
     * Stops the passed player's search task.
     *
     * @param player Player whose search tasks will be stopped.
     */
    private void stopInventorySearchTask(Player player) {
        Scheduler.Task task = searchTasks.remove(player.getUniqueId());
        if (task == null) return;

        task.cancel();
    }

    /**
     * Starts the search action bar message for the passed Player.
     *
     * @param player   Player that is searching.
     * @param itemType Item that is being searched.
     */
    private void startSearchActionBarTask(Player player, Material itemType) {
        stopSearchActionBarTask(player);

        Scheduler.Task actionBarTask = Scheduler.runTimer(getPlugin().getSpigotPlugin(), () -> {
            if (!player.isOnline()) {
                stopSearchActionBarTask(player);
                return;
            }
            Message.SEARCHING_ACTION_BAR.send(player, true, null, Map.of("%item%", StringUtil.formatEnum(itemType)));
        }, 0L, 20L);
        Sound.SEARCHING.play(player);
        searchingActionBarTasks.put(player.getUniqueId(), actionBarTask);
    }

    /**
     * Calls the ItemSearchEvent to gather any third party added inventories and then filters the ones that don't contain
     * the searched item.
     *
     * @param player          Player searching for the item.
     * @param itemType        Item to search.
     * @param nearInventories Inventories to search in.
     */
    private void filterInventories(Player player, Material itemType, Set<Inventory> nearInventories) {
        ItemSearchEvent itemSearchEvent = new ItemSearchEvent(player, itemType, nearInventories);
        Bukkit.getPluginManager().callEvent(itemSearchEvent);

        nearInventories.removeIf(inventory -> {
            for (ItemStack item : inventory) {
                if (item == null) continue;
                if (item.getItemMeta() instanceof BlockStateMeta blockStateMeta) {
                    if (!(blockStateMeta.getBlockState() instanceof ShulkerBox shulkerBox)) continue;
                    if (!shulkerBox.getInventory().contains(itemType)) continue;

                    return false;
                }
                if (item.getType() != itemType) continue;

                return false;
            }
            // The inventory doesn't have the item
            return true;
        });
    }

    /**
     * Starts the task that will periodically highlight the containers corresponding to the passed Set of inventories.
     *
     * @param player      Player the containers will be highlighted for.
     * @param inventories Inventories whose containers will be highlighted.
     */
    private void startContainerHighlightTask(Player player, Set<Inventory> inventories) {
        stopContainerHighlightTask(player);
        stopSearchActionBarTask(player);
        if (inventories.isEmpty()) {
            Message.NO_ITEMS_FOUND.send(player, true, CommonSound.ERROR, new HashMap<>());
            return;
        }
        Particle outlineParticle = Particle.ITEM_FOUND_OUTLINE;
        Particle trailParticle = Particle.ITEM_FOUND_TRAIL;
        int effectIterations = ((ItemFinder) getPlugin()).getMainConfig().getItemFoundEffectIterations();
        Scheduler.Task containerHighlightTask = Scheduler.runTimer(getPlugin().getSpigotPlugin(), player.getLocation(), new Runnable() {
            private int iterations = 0;

            @Override
            public void run() {
                if (!player.isOnline()) return;
                if (iterations >= effectIterations) {
                    stopContainerHighlightTask(player);
                    return;
                }
                Location playerLocation = player.getLocation();
                for (Inventory inventory : inventories) {
                    Location inventoryLocation = inventory.getLocation();
                    if (inventoryLocation == null) continue;

                    Location headLocation = player.getEyeLocation();
                    headLocation.add(headLocation.getDirection().multiply(2));

                    if (inventoryLocation.getBlock().getState() instanceof Container) {
                        inventoryLocation.add(0.5, 0.5, 0.5);
                        BlockOutlineUtil.spawnBlockOutline(player, inventoryLocation.getBlock(), outlineParticle);
                    } else {
                        inventoryLocation.add(0, 1, 0);
                        ParticleUtil.spawnParticleCircle(inventoryLocation, 1, 20, outlineParticle);
                    }
                    ParticleUtil.spawnParticleLine(player, headLocation, inventoryLocation, trailParticle,
                            (int) inventoryLocation.distance(playerLocation), 1L);
                    Sound.ITEM_FOUND.play(player, inventoryLocation);
                }
                iterations += 1;
            }
        }, 0L, 20L);
        containerHighlightTasks.put(player.getUniqueId(), containerHighlightTask);
    }

    /**
     * Stops the container highlight task of the passed player.
     *
     * @param player Player whose container highlight task will be stopped.
     */
    private void stopContainerHighlightTask(Player player) {
        Scheduler.Task highlightTask = containerHighlightTasks.remove(player.getUniqueId());
        if (highlightTask == null) return;

        highlightTask.cancel();
    }

    /**
     * Stops the search action bar for the passed player.
     *
     * @param player Player to stop the action bar for.
     */
    private void stopSearchActionBarTask(Player player) {
        Scheduler.Task actionBarTask = searchingActionBarTasks.remove(player.getUniqueId());
        if (actionBarTask == null) return;

        actionBarTask.cancel();
    }
}
