package me.eeshe.itemfinder.models.config;

import me.eeshe.itemfinder.ItemFinder;
import me.eeshe.penpenlib.files.config.ConfigWrapper;
import me.eeshe.penpenlib.models.config.PenParticle;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class Particle extends PenParticle {
    private static final List<PenParticle> PARTICLES = new ArrayList<>();
    private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(ItemFinder.getInstance(), null, "particles.yml");

    public static final Particle ITEM_FOUND_OUTLINE = new Particle("item-found-outline", true, org.bukkit.Particle.REDSTONE, 0, 0, 0, 0., 0.01, new org.bukkit.Particle.DustOptions(Color.AQUA, 0.5F));
    public static final Particle ITEM_FOUND_TRAIL = new Particle("item-found-trail", true, org.bukkit.Particle.REDSTONE, 0, 0, 0, 0., 0.01, new org.bukkit.Particle.DustOptions(Color.AQUA, 0.5F));

    public Particle(String path, boolean defaultEnabled, org.bukkit.Particle defaultParticle, int defaultAmount,
                    double defaultXOffSet, double defaultYOffSet, double defaultZOffSet, double defaultExtra,
                    Object defaultData) {
        super(path, defaultEnabled, defaultParticle, defaultAmount, defaultXOffSet, defaultYOffSet, defaultZOffSet,
                defaultExtra, defaultData);
    }

    public Particle() {
    }

    @Override
    public List<PenParticle> getParticles() {
        return PARTICLES;
    }

    @Override
    public ConfigWrapper getConfigWrapper() {
        return CONFIG_WRAPPER;
    }
}
