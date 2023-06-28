package io.github.endergamerhun.parkourer.ability;

import io.github.endergamerhun.parkourer.looping.PlayerLoop;
import io.github.endergamerhun.parkourer.utils.TaskUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Fly implements Listener {

    @EventHandler
    public void onSneakToggle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.isGliding()) return;
        if (event.getMaterial() != Material.BLAZE_POWDER) return;
        TaskUtil.run(new FlyLoop(player));
    }

    private static class FlyLoop extends PlayerLoop {
        public FlyLoop(Player player) {
            super(player);
        }

        @Override
        protected boolean loop() {
            if (player.isSneaking()) return false;
            player.setGliding(true);
            player.setVelocity(player.getLocation().getDirection().multiply(1));
            return true;
        }

        @Override
        protected void end() {
            player.setGliding(false);
        }
    }
}
