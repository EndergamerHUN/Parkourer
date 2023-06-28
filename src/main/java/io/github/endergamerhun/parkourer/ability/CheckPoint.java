package io.github.endergamerhun.parkourer.ability;

import io.github.endergamerhun.parkourer.looping.PlayerLoop;
import io.github.endergamerhun.parkourer.managers.ParkourPlayer;
import io.github.endergamerhun.parkourer.managers.PlayerManager;
import io.github.endergamerhun.parkourer.utils.ParticleUtil;
import io.github.endergamerhun.parkourer.utils.TaskUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CheckPoint implements Listener {

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        if (loc.getBlock().getRelative(BlockFace.DOWN).getType() != Material.GOLD_BLOCK) return;
        ParkourPlayer parkourPlayer = PlayerManager.of(player);
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_STEP, 1f, 0.5f);
        ParticleUtil.spawnParticle(Particle.END_ROD, player.getLocation(), 15, 0.3, 0.3, 0.3, 0);
        parkourPlayer.setCheckpoint(loc);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;
        if (event.getMaterial() != Material.GUNPOWDER) return;
        Player player = event.getPlayer();
        ParkourPlayer parkourPlayer = PlayerManager.of(player);
        parkourPlayer.reset();
    }

    @EventHandler
    public void onSlotSwitch(PlayerSwapHandItemsEvent event) {
        if (event.getMainHandItem() == null || event.getMainHandItem().getType() != Material.GUNPOWDER) return;
        TaskUtil.run(new CheckPointLoop(event.getPlayer()));
    }

    @EventHandler
    public void onDimensionChange(PlayerChangedWorldEvent event) {
        ParkourPlayer player = PlayerManager.of(event.getPlayer());
        player.reset();
    }

    private static class CheckPointLoop extends PlayerLoop {

        protected CheckPointLoop(Player player) {
            super(player);
        }

        @Override
        protected boolean loop() {
            player.spawnParticle(Particle.DRAGON_BREATH, parkourPlayer.getCheckpoint(), 10, 0.2, 0, 0.2);
            return player.getItemInUse() != null && player.getItemInUse().getType() == Material.GUNPOWDER;
        }

        @Override
        protected void end() {

        }
    }
}
