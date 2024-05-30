# ItemFinder

ItemFinder is a plugin created to ease the search for items within all near containers.

## Features

- Search for specific items stored in containers within a configured radius.
- Highly configurable.
- Folia support.

## Commands and Permissions

| **Command**                 | **Description**                                         | **Permission**      |
|-----------------------------|---------------------------------------------------------|---------------------|
| `/itemfinder`               | Base command of the plugin.                             | `itemfinder.base`   |
| `/itemfinder search <Item>` | Searches the specified item within all near containers. | `itemfinder.search` |
| `/itemfinder reload`        | Reloads the configurations of the plugin.               | `itemfinder.reload` |
| `/itemfinder help`          | Displays a list of the commands of the plugin.          | `itemfinder.help`   |

## Configurations

| ***Configuration**            | **Description**                                                                                                                                    | **Example**                                     |
|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|
| item-blacklist                | List of items that can't be searched through the `/itemfinder search` command.                                                                     | item-blacklist: <br/>- ENCHANTED_GOLDEN_APPLE   |
| world-blacklist               | List of worlds the `/itemfinder search` command can't be used in.                                                                                  | world-blacklist: <br/>- world_the_end           |
| region-blacklist              | List of WorldGuard regions the `/itemfinder search` command can't be used in.                                                                      | region-blacklist: <br/>- region1 <br/>- region2 |
| search-radius                 | Radius (in blocks) around the player where items will be searched.                                                                                 | search-radius: 10                               |
| searched-chunks-per-tick      | Amount of chunks that will be searched for items per tick.  The higher the amount the fastest the search and the highest the impact on the server. | searched-chunks-per-tick: 15                    |
| item-found-effects-iterations | Amount of times the item found particles will be shown on containers with the searched item.                                                       | item-found-effects-iterations: 5                |
| search-cooldown               | Time (in seconds) between each item search.                                                                                                        | search-cooldown: 5                              |

Messages, sounds and particles from the plugin are also configurable. For instructions for how to configure this,
check the [PenPenLib](https://github.com/Eeshe/pen-pen-lib) guide.

## Demonstration

https://github.com/Eeshe/item-finder/assets/75171906/a7d31383-0312-4fd1-9b41-b72301b02c31

## Dependencies

- [PenPenLib](https://github.com/Eeshe/pen-pen-lib).

## Soft Dependencies

- [WorldGuard](https://dev.bukkit.org/projects/worldguard): Used to check for blacklisted search regions.

## Installation

1. Download the latest version of [PenPenLib](https://github.com/Eeshe/pen-pen-lib) and ItemFinder JAR files.
2. Drop the JAR files into the `plugins` folder of your Spigot/Paper server.
3. Restart the server.

## Compatibility

ItemFinder is compatible with Minecraft versions 1.19.4 and later. Compatibility with older versions is not currently
planned, as Folia's oldest supported version is 1.19.4.

## Contributing

Contributions to ItemFinder are welcome! If you encounter any issues or have suggestions for improvements, please open
an issue on the GitHub repository. To contribute code:

1. Fork the repository.
2. Create a new branch for your feature/fix.
3. Commit your changes.
4. Open a pull request and assign me as the reviewer.

## License

ItemFinder is licensed under the [MIT License](LICENSE).
