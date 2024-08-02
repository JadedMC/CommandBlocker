/*
 * This file is part of CommandBlocker, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.commandblocker;

import net.jadedmc.commandblocker.commands.CommandBlockerCMD;
import net.jadedmc.commandblocker.listeners.PlayerCommandPreprocessListener;
import net.jadedmc.commandblocker.listeners.PlayerCommandSendListener;
import net.jadedmc.commandblocker.utils.ChatUtils;
import net.jadedmc.commandblocker.utils.VersionUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandBlockerPlugin extends JavaPlugin {
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

        // This event only exists on 1.13+.
        if(VersionUtils.getServerVersion() >= 13) {
            getServer().getPluginManager().registerEvents(new PlayerCommandSendListener(this), this);
        }

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

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}