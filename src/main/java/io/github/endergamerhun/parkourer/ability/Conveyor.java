package io.github.endergamerhun.parkourer.ability;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class Conveyor implements Listener {

    @EventHandler
    public void onStep(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() != Material.MAGENTA_GLAZED_TERRACOTTA) return;
        if (!(block.getBlockData() instanceof Directional directional)) return;
        Vector vec = directional.getFacing().getDirection().multiply(-0.2);
        if (player.isSneaking()) vec.multiply(0.2);
        Vector vel = player.getVelocity();
        if (vec.getX() == 0) vel.setZ(vec.getZ());
        else vel.setX(vec.getX());
        player.setVelocity(vel);
        player.playSound(player, Sound.ENTITY_IRON_GOLEM_STEP, 2f, 0f);
    }
}
