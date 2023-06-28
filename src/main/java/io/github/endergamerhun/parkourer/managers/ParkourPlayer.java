package io.github.endergamerhun.parkourer.managers;

import io.github.endergamerhun.parkourer.looping.PlayerLoop;
import io.github.endergamerhun.parkourer.utils.MathUtil;
import io.github.endergamerhun.parkourer.utils.TaskUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ParkourPlayer {

    public void update() {
        float target = (float) stamina /maxStamina;
        player.sendExperienceChange(target, shards);

        if (target < 0.2f) player.spawnParticle(Particle.DRIP_WATER, player.getEyeLocation(), 5, 0.2, 0, 0.2);
    }

    // CheckPoint
    private Location checkpoint;
    public void setCheckpoint(Location location) {
        checkpoint = location;
    }
    public Location getCheckpoint() {
        return checkpoint;
    }
    public void reset() {
        stamina = maxStamina;
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 2f);
        player.teleport(checkpoint);
        shards = 0;
        update();
    }

    // Stamina
    private final int maxStamina = 100;

    private int stamina = maxStamina;
    public boolean hasStamina() {
        return hasStamina(0);
    }
    public int getStamina() {
        return stamina;
    }
    public void setStamina(int i) {
        stamina = i;
    }
    public boolean hasStamina(int i) {
        return stamina > i;
    }
    public void changeStamina(int i) {
        stamina = MathUtil.clamp(0, stamina+i, maxStamina);
        update();
    }
    public boolean useStamina() {
        return useStamina(1);
    }
    public boolean useStamina(int i) {
        if (!hasStamina(i)) return false;
        changeStamina(-i);
        return true;
    }

    // Double Jump
    public void disableJump() {
        player.setAllowFlight(false);
        player.setFlying(false);
        TaskUtil.runLater(() -> player.setFlying(false));
    }
    public void enableJump() {
        player.setAllowFlight(true);
        player.setFlying(false);
    }

    // Shade shard
    private int shards = 0;
    public void setShard(int i) {
        shards = i;
        update();
    }
    public boolean hasShard() {
        return shards > 0;
    }
    public boolean useShard() {
        if (!hasShard()) return false;
        shards--;
        update();
        return true;
    }

    // Crawl
    public boolean crawling = false;

    // Setup
    public ParkourPlayer(Player player) {
        this.player = player;
        checkpoint = player.getLocation();
        update();
    }
    private final Player player;
    public Player getPlayer() {
        return player;
    }

    public static final class StaminaLoop extends PlayerLoop {

        StaminaLoop(Player player) {
            super(player);
        }

        protected boolean loop() {
            if (player.getLocation().add(0, -0.2, 0).getBlock().getType().isSolid()) parkourPlayer.changeStamina(2);
            return true;
        }

        protected void end() {

        }
    }
}
