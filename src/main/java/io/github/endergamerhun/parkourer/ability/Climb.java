package io.github.endergamerhun.parkourer.ability;

import io.github.endergamerhun.parkourer.managers.ParkourPlayer;
import io.github.endergamerhun.parkourer.managers.PlayerManager;
import io.github.endergamerhun.parkourer.looping.PlayerLoop;
import io.github.endergamerhun.parkourer.utils.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class Climb implements Listener {

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        TaskUtil.run(new ClimbLoop(player));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        Block block = TraceUtil.traceBlock(player, 1, true);
        if (block == null) return;
        if (block.getType() != Material.BLACKSTONE) return;
        if (block.getRelative(BlockFace.UP).getType() == Material.BLACKSTONE) return;
        if (block.getRelative(BlockFace.DOWN).getType() != Material.BLACKSTONE) return;

        ParkourPlayer parkourPlayer = PlayerManager.of(player);
        if (!parkourPlayer.useStamina(20)) return;

        event.setCancelled(true);
        player.setSneaking(false);
        player.playSound(player, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1f, 1.5f);
        Vector vel = player.getVelocity();
        vel.setY(0.6);
        player.setVelocity(vel);
    }

    private static class ClimbLoop extends PlayerLoop {

        private ClimbLoop(Player player) {
            super(player);
        }

        public boolean loop() {
            if (!player.isSneaking()) return false;
            Block block = TraceUtil.traceBlock(player, 1, true);
            if (block == null) return false;
            if (block.getType() != Material.BLACKSTONE) return false;
            if (block.getRelative(BlockFace.DOWN).getType() != Material.BLACKSTONE) return false;

            if (!parkourPlayer.useStamina()) return false;

            player.setGravity(false);
            ParticleUtil.spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 5, 0.2, 0, 0.2, 0.1);
            player.playSound(player, Sound.BLOCK_LADDER_PLACE, 0.5f, 1f);
            float angle = MathUtil.clamp(-45f, player.getLocation().getPitch(), 20f);
            Vector vel = player.getVelocity();
            vel.setY(-angle/45f/7f);
            player.setVelocity(vel);
            return true;
        }

        public void end() {
            parkourPlayer.getPlayer().setGravity(true);
        }
    }
}
