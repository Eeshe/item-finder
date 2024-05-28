package me.eeshe.itemfinder;

import me.eeshe.itemfinder.commands.CommandItemFinder;
import me.eeshe.itemfinder.files.config.MainConfig;
import me.eeshe.itemfinder.listeners.PlayerConnectionHandler;
import me.eeshe.itemfinder.managers.SearchPlayerManager;
import me.eeshe.itemfinder.models.config.Message;
import me.eeshe.itemfinder.models.config.Particle;
import me.eeshe.itemfinder.models.config.Sound;
import me.eeshe.penpenlib.PenPenLib;
import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ItemFinder extends JavaPlugin implements PenPenPlugin {
    private final List<ConfigWrapper> configFiles = new ArrayList<>();
    private final List<DataManager> dataManagers = new ArrayList<>();

    private MainConfig mainConfig;

    private SearchPlayerManager searchPlayerManager;

    /**
     * Creates and returns a static instance of the Plugin's main class.
     *
     * @return Instance of the main class of the plugin.
     */
    public static ItemFinder getInstance() {
        return ItemFinder.getPlugin(ItemFinder.class);
    }

    @Override
    public void onEnable() {
        setupFiles();
        registerManagers();
        registerCommands();
        registerListeners();
        for (DataManager dataManager : dataManagers) {
            dataManager.onEnable();
        }
    }

    /**
     * Creates and configures all the config files of the plugin.
     */
    public void setupFiles() {
        configFiles.clear();

        this.mainConfig = new MainConfig(this);
        Message message = new Message();
        Sound sound = new Sound();
        Particle particle = new Particle();
        configFiles.addAll(List.of(
                mainConfig,
                message.getConfigWrapper(),
                sound.getConfigWrapper(),
                particle.getConfigWrapper()
        ));
        message.register();
        sound.register();
        particle.register();
        for (ConfigWrapper configFile : configFiles) {
            configFile.writeDefaults();
        }
    }

    /**
     * Registers all the needed managers in order for the plugin to work.
     */
    private void registerManagers() {
        this.searchPlayerManager = new SearchPlayerManager(this);
        dataManagers.addAll(List.of(
                searchPlayerManager
        ));
    }

    /**
     * Registers all the commands, subcommands, CommandExecutors and TabCompleters regarding the plugin.
     */
    private void registerCommands() {
        if (!(Bukkit.getPluginManager().getPlugin("PenPenLib") instanceof PenPenLib penPenLib)) return;

        penPenLib.registerCommands(List.of(
                new CommandItemFinder(this)
        ));
    }

    /**
     * Registers all the event listeners the plugin might need.
     */
    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerConnectionHandler(searchPlayerManager), this);
    }

    @Override
    public void onDisable() {
        for (DataManager dataManager : dataManagers) {
            dataManager.unload();
        }
    }

    @Override
    public void reload() {
        for (ConfigWrapper configFile : configFiles) {
            configFile.reloadConfig();
        }
        setupFiles();
        for (DataManager dataManager : dataManagers) {
            dataManager.reload();
        }
    }

    @Override
    public Plugin getSpigotPlugin() {
        return this;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public SearchPlayerManager getSearchPlayerManager() {
        return searchPlayerManager;
    }
}
