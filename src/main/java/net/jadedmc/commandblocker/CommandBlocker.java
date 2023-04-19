package net.jadedmc.commandblocker;

import net.jadedmc.commandblocker.commands.CommandBlockerCMD;
import net.jadedmc.commandblocker.listeners.PlayerCommandPreprocessListener;
import net.jadedmc.commandblocker.listeners.PlayerCommandSendListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandBlocker extends JavaPlugin {
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);

        getCommand("commandblocker").setExecutor(new CommandBlockerCMD(this));
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandSendListener(this), this);

        // Enables bStats statistics tracking.
        new Metrics(this, 18230);
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}