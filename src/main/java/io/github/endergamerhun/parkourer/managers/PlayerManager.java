package io.github.endergamerhun.parkourer.managers;

import io.github.endergamerhun.parkourer.utils.TaskUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager implements Listener {
    private static final HashMap<UUID, ParkourPlayer> players = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        registerPlayer(event.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer().getUniqueId());
    }

    public static ParkourPlayer of(Player player) {
        UUID uuid = player.getUniqueId();
        if (!players.containsKey(uuid)) registerPlayer(player);
        return players.get(uuid);
    }
    private static void registerPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (players.containsKey(uuid)) return;
        players.put(uuid, new ParkourPlayer(player));
        TaskUtil.run(new ParkourPlayer.StaminaLoop(player));
    }
}
