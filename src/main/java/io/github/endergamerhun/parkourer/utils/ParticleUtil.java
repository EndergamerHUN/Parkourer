package io.github.endergamerhun.parkourer.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleUtil {

    public static void spawnParticle(Particle part, Location loc, int count, double dx, double dy, double dz, double extra) {
        World world = loc.getWorld();
        assert world != null;
        world.spawnParticle(part, loc, count, dx, dy, dz, extra);
    }

    public static <T> void spawnParticle(Particle part, Location loc, int count, double dx, double dy, double dz, double extra, T data) {
        World world = loc.getWorld();
        assert world != null;
        world.spawnParticle(part, loc, count, dx, dy, dz, extra, data);
    }
}
