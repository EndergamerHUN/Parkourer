package io.github.endergamerhun.parkourer.ability;

import io.github.endergamerhun.parkourer.looping.PlayerLoop;
import io.github.endergamerhun.parkourer.managers.ParkourPlayer;
import io.github.endergamerhun.parkourer.managers.PlayerManager;
import io.github.endergamerhun.parkourer.utils.MathUtil;
import io.github.endergamerhun.parkourer.utils.TaskUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class Crawl implements Listener {

    private static final Vector down = new Vector(0, -1, 0);

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;
        TaskUtil.run(new CrawlLoop(event.getPlayer()));
    }

    @EventHandler
    public void onGlideStateChange(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ParkourPlayer parkourPlayer = PlayerManager.of(player);
        event.setCancelled(parkourPlayer.crawling);
    }

    private static class CrawlLoop extends PlayerLoop {
        protected CrawlLoop(Player player) {
            super(player);
        }

        private int counter = 0;
        @Override
        protected boolean loop() {
            if (parkourPlayer.crawling) return false;
            if (!player.isSneaking()) return false;
            if (!player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) return false;
            float angle = down.angle(player.getLocation().getDirection());
            if (angle > 0.2f) return false;
            counter++;
            String title = ChatColor.GREEN + "|".repeat(counter) + ChatColor.WHITE + "|".repeat(20-counter);
            player.sendTitle("", title, 0, 2, 0);
            if (counter == 20) {
                TaskUtil.run(new CrawlingLoop(player));
                return false;
            }
            return true;
        }

        @Override
        protected void end() {

        }
    }
    private static class CrawlingLoop extends PlayerLoop {

        protected CrawlingLoop(Player player) {
            super(player);
        }

        @Override
        protected boolean loop() {
            if (!player.isSneaking() || player.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) return false;
            player.setGliding(true);
            parkourPlayer.crawling = true;
            Vector vec = MathUtil.vectorFromYawAndPitch(player.getLocation().getYaw(), 90);
            player.setVelocity(vec.multiply(0.4));
            return true;
        }

        @Override
        protected void end() {
            parkourPlayer.crawling = false;
            player.setGliding(false);
        }
    }
}
