package io.github.endergamerhun.parkourer.looping;

import io.github.endergamerhun.parkourer.managers.ParkourPlayer;
import io.github.endergamerhun.parkourer.managers.PlayerManager;
import io.github.endergamerhun.parkourer.utils.TaskUtil;
import org.bukkit.entity.Player;

public abstract class PlayerLoop extends Loop {
    protected final ParkourPlayer parkourPlayer;
    protected final Player player;
    protected PlayerLoop(Player player) {
        this.player = player;
        this.parkourPlayer = PlayerManager.of(player);
    }

    public void run() {
        if (loop() && parkourPlayer.getPlayer().isOnline()) {
            first = false;
            TaskUtil.runLater(this);
        } else if (!first) {
            end();
        }
    }
}
