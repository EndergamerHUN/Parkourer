package io.github.endergamerhun.parkourer.utils;

import io.github.endergamerhun.parkourer.Parkourer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PluginUtil {

    private static final List<Listener> listeners = new ArrayList<>();
    static {
        Reflections reflections = new Reflections("io.github.endergamerhun.parkourer.ability");
        for (Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)) {
            if (clazz.isInterface()) continue;
            try {
                listeners.add(clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                     IllegalAccessException e) {
                log("Could not create new instance of class %s", clazz.getName());
                e.printStackTrace();
            }
        }
    }

    public static Parkourer getInstance() {
        return Parkourer.getInstance();
    }

    public static void log(String format, Object... objects) {
        String log = String.format(format, objects);
        Bukkit.getConsoleSender().sendMessage(log);
    }

    public static void reload(Parkourer plugin) {
        PluginManager manager = Bukkit.getPluginManager();
        HandlerList.unregisterAll(plugin);
        for (Listener listener: listeners) {
            manager.registerEvents(listener, plugin);
        }
    }
}
