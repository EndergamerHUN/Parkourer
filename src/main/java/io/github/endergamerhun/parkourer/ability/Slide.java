package io.github.endergamerhun.parkourer.ability;

import io.github.endergamerhun.parkourer.managers.PlayerManager;
import io.github.endergamerhun.parkourer.looping.PlayerLoop;
import io.github.endergamerhun.parkourer.utils.MathUtil;
import io.github.endergamerhun.parkourer.utils.ParticleUtil;
import io.github.endergamerhun.parkourer.utils.TaskUtil;
import io.github.endergamerhun.parkourer.utils.TraceUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Slide implements Listener {

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        TaskUtil.run(new SlideLoop(event.getPlayer()));
    }

    private static class SlideLoop extends PlayerLoop {

        private SlideLoop(Player player) {
            super(player);
        }

        public boolean loop() {
            if (!player.isSneaking()) return false;
            Block block = TraceUtil.traceBlock(player,-1, false);
            if (block == null) return false;
            if (block.getType() != Material.CLAY) return false;

            if (!parkourPlayer.useStamina()) return false;

            player.setGravity(false);
            ParticleUtil.spawnParticle(Particle.ITEM_CRACK, player.getLocation(), 5, 0.2, 0.2, 0.2, 0, new ItemStack(Material.CLAY));
            player.playSound(player, Sound.BLOCK_AZALEA_HIT, 0.5f, 1f);
            Vector vel = player.getVelocity();
            vel.setY(-0.05f);
            player.setVelocity(vel);
            return true;
        }

        public void end() {
            player.setGravity(true);
            if (player.isSneaking()) return;
            if (!parkourPlayer.useStamina(20)) return;
            player.playSound(player, Sound.ITEM_BUNDLE_DROP_CONTENTS, 1f, 2f);
            double yaw = player.getLocation().getYaw();
            Vector vec = MathUtil.vectorFromYawAndPitch(yaw, -40);
            vec.multiply(0.6);
            player.setVelocity(vec);
        }
    }
}
