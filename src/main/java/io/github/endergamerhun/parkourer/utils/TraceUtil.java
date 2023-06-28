package io.github.endergamerhun.parkourer.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;

public class TraceUtil {
    public static Block traceBlock(Player player, int offset, boolean eye) {
        RayTraceResult result = trace(player, offset, eye);
        return result == null ? null : result.getHitBlock();
    }

    public static Material traceMaterial(Player player, int offset, boolean eye) {
        Block block = traceBlock(player, offset, eye);
        return block == null ? null : block.getType();
    }

    public static Vector traceLocation(Player player, int offset, boolean eye) {
        RayTraceResult result = trace(player, offset, eye);
        return result == null ? null : result.getHitPosition();
    }

    @Nullable
    public static RayTraceResult trace(Player player, int offset, boolean eye) {
        Location loc = eye ? player.getEyeLocation() : player.getLocation();
        Vector vec = loc.getDirection().multiply(offset);
        return player.getWorld().rayTraceBlocks(loc, vec, Math.abs(offset));
    }
}
