package io.github.endergamerhun.parkourer.utils;

import org.bukkit.Bukkit;

public class TaskUtil {
    public static void runLater(Runnable task) {
        runLater(task, 1);
    }

    public static void runLater(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(PluginUtil.getInstance(), task, delay);
    }

    public static void run(Runnable task) {
        Bukkit.getScheduler().runTask(PluginUtil.getInstance(), task);
    }
}
