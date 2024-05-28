package me.eeshe.itemfinder.models.config;

import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.models.config.CommonMessage;
import me.eeshe.penpenlib.models.config.PenMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message extends PenMessage {
    private static final List<PenMessage> MESSAGES = new ArrayList<>();
    private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(ItemFinder.getInstance(), null, "messages.yml");

    public static final CommonMessage HELP_COMMAND_INFO = new CommonMessage("help-command-info", "Displays this list.");
    public static final CommonMessage HELP_COMMAND_USAGE = new CommonMessage("help-command-usage", "/itemfinder help");
    public static final CommonMessage RELOAD_COMMAND_INFO = new CommonMessage("reload-command-info", "Reloads the plugin's configuration file.");
    public static final CommonMessage RELOAD_COMMAND_USAGE = new CommonMessage("reload-command-usage", "/itemfinder reload");
    public static final Message SEARCH_COMMAND_INFO = new Message("search-command-info", "Searches the specified item in the containers near you.");
    public static final Message SEARCH_COMMAND_USAGE = new Message("search-command-usage", "/itemfinder search <Item>");
    public static final Message BLACKLISTED_ITEM = new Message("blacklisted-item", "&cYou can't search for &l%item%&c.");
    public static final Message BLACKLISTED_WORLD = new Message("blacklisted-world", "&cYou can't search items in this world.");
    public static final Message BLACKLISTED_REGION = new Message("blacklisted-region", "&cYou can't search items in this area.");
    public static final Message SEARCH_COOLDOWN = new Message("search-cooldown", "&cSearch cooldown: &l%cooldown%&c.");
    public static final Message SEARCHING_ACTION_BAR = new Message("searching-action-bar", "&7Searching for &l%item%&7...");
    public static final Message NO_ITEMS_FOUND = new Message("no-items-found", "&cNo items found");
    private static final Map<String, PenMessage> PLACEHOLDERS = new HashMap<>();

    public Message(String path, String defaultValue) {
        super(path, defaultValue);
    }

    public Message() {
    }

    @Override
    public List<PenMessage> getMessages() {
        return MESSAGES;
    }

    @Override
    public Map<String, PenMessage> getPlaceholders() {
        return PLACEHOLDERS;
    }

    @Override
    public ConfigWrapper getConfigWrapper() {
        return CONFIG_WRAPPER;
    }
}
