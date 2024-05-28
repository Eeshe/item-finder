package me.eeshe.itemfinder.models.config;

import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.models.config.PenSound;

import java.util.ArrayList;
import java.util.List;

public class Sound extends PenSound {
    private static final List<PenSound> SOUNDS = new ArrayList<>();
    private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(ItemFinder.getInstance(), null, "sounds.yml");

    public static final Sound SEARCHING = new Sound("searching", true, org.bukkit.Sound.BLOCK_SCULK_SENSOR_CLICKING, 0.6F, 0.8F);
    public static final Sound ITEM_FOUND = new Sound("item-found", true, org.bukkit.Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.3F, 1F);

    public Sound(String path, boolean defaultEnabled, org.bukkit.Sound defaultSound, float defaultVolume, float defaultPitch) {
        super(path, defaultEnabled, defaultSound, defaultVolume, defaultPitch);
    }

    public Sound() {
    }

    @Override
    public List<PenSound> getSounds() {
        return SOUNDS;
    }

    @Override
    public ConfigWrapper getConfigWrapper() {
        return CONFIG_WRAPPER;
    }
}
