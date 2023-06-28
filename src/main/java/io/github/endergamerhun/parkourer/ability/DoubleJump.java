package io.github.endergamerhun.parkourer.ability;

import io.github.endergamerhun.parkourer.managers.ParkourPlayer;
import io.github.endergamerhun.parkourer.managers.PlayerManager;
import io.github.endergamerhun.parkourer.looping.PlayerLoop;
import io.github.endergamerhun.parkourer.utils.MathUtil;
import io.github.endergamerhun.parkourer.utils.ParticleUtil;
import io.github.endergamerhun.parkourer.utils.TaskUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class DoubleJump implements Listener {

    @EventHandler
    public void onFlightToggle(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!event.isFlying()) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        player.setFlying(false);
        TaskUtil.runLater(() -> player.setFlying(false));

        ParkourPlayer parkourPlayer = PlayerManager.of(player);
        if (!parkourPlayer.useStamina(20)) return;

        parkourPlayer.disableJump();
        TaskUtil.run(new JumpLoop(player));

        ParticleUtil.spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.2, 0, 0.2, 0);
        player.playSound(player, Sound.ENTITY_PIG_SADDLE, 1f, 1.4f);

        Vector vel = player.getVelocity();
        Vector boost = (vel.getX() == 0 && vel.getZ() == 0) ?
                new Vector(0, 0.55, 0) :
                MathUtil.vectorFromYawAndPitch(player.getLocation().getYaw(), -45).multiply(0.6);
        player.setVelocity(boost);
    }

    @EventHandler
    public void onGameModeSwitch(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        TaskUtil.runLater(() -> player.setAllowFlight(true));
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        ParkourPlayer parkourPlayer = PlayerManager.of(event.getPlayer());
        parkourPlayer.enableJump();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ParkourPlayer parkourPlayer = PlayerManager.of(event.getPlayer());
        parkourPlayer.enableJump();
    }

    private static class JumpLoop extends PlayerLoop {

        private JumpLoop(Player player) {
            super(player);
        }

        public boolean loop() {
            if (player.getAllowFlight()) return false;
            Location loc = player.getLocation();
            loc.add(new Vector(0, -0.1, 0));
            return loc.getBlock().getType() != Material.DIAMOND_BLOCK;
        }

        public void end() {
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            parkourPlayer.enableJump();
        }
    }
}
