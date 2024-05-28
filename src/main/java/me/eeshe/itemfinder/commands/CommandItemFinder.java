package me.eeshe.itemfinder.commands;

import me.eeshe.penpenlib.PenPenPlugin;
import me.eeshe.penpenlib.commands.PenCommand;

import java.util.List;

public class CommandItemFinder extends PenCommand {

    public CommandItemFinder(PenPenPlugin plugin) {
        super(plugin);

        setName("itemfinder");
        setPermission("itemfinder.base");
        setSubcommands(List.of(
                new CommandSearch(plugin, this),
                new CommandReload(plugin, this),
                new CommandHelp(plugin, this)
        ));
    }
}
