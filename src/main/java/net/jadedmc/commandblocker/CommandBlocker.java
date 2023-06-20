package net.jadedmc.commandblocker;

import net.jadedmc.commandblocker.commands.CommandBlockerCMD;
import net.jadedmc.commandblocker.listeners.PlayerCommandPreprocessListener;
import net.jadedmc.commandblocker.listeners.PlayerCommandSendListener;
import net.jadedmc.commandblocker.utils.ChatUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandBlocker extends JavaPlugin {
    private BukkitAudiences adventure;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Initialize an audiences instance for the plugin
        this.adventure = BukkitAudiences.create(this);
        ChatUtils.setAdventure(adventure);

        settingsManager = new SettingsManager(this);

        getCommand("commandblocker").setExecutor(new CommandBlockerCMD(this));
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandSendListener(this), this);

        // Enables bStats statistics tracking.
        new Metrics(this, 18230);
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}