package io.github.endergamerhun.parkourer;

import io.github.endergamerhun.parkourer.utils.PluginUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class Parkourer extends JavaPlugin {

    private static Parkourer INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        PluginUtil.reload(this);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Parkourer getInstance() {
        return INSTANCE;
    }
}
