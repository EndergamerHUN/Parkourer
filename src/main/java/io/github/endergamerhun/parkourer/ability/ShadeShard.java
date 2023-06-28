package io.github.endergamerhun.parkourer.ability;

import io.github.endergamerhun.parkourer.managers.ParkourPlayer;
import io.github.endergamerhun.parkourer.managers.PlayerManager;
import io.github.endergamerhun.parkourer.utils.ParticleUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class ShadeShard implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getMaterial() != Material.FLINT) return;
        Player player = event.getPlayer();
        ParkourPlayer parkourPlayer = PlayerManager.of(player);
        if (!parkourPlayer.hasShard() || !parkourPlayer.hasStamina(30)) return;
        if (!parkourPlayer.useShard()) return;
        if (!parkourPlayer.useStamina(30)) return;

        ParticleUtil.spawnParticle(Particle.REDSTONE, player.getLocation(), 30, 0.2, 0, 0.2, 1, new Particle.DustOptions(Color.BLACK, 3));
        player.playSound(player, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 2f);

        Vector vec = player.getLocation().getDirection();
        player.setVelocity(vec.multiply(-0.8));
    }
    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() != Material.COAL_BLOCK) return;
        int i = 0;
        while (block.getType() == Material.COAL_BLOCK) {
            i++;
            block = block.getRelative(BlockFace.DOWN);
        }
        player.playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 1f, 2f);
        ParkourPlayer parkourPlayer = PlayerManager.of(player);
        parkourPlayer.setShard(i);
    }
}
