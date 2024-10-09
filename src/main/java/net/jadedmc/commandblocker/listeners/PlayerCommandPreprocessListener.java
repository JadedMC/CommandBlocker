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
package net.jadedmc.commandblocker.listeners;

import net.jadedmc.commandblocker.CommandBlockerPlugin;
import net.jadedmc.commandblocker.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listens to the PlaceCommandPreprocessEvent, which runs when a player goes to send a command.
 * We use this to block commands set in the config.yml.
 */
public class PlayerCommandPreprocessListener implements Listener {
    private final CommandBlockerPlugin plugin;

    /**
     * Creates the listener.
     * @param plugin Instance of the plugin.
     */
    public PlayerCommandPreprocessListener(@NotNull final CommandBlockerPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlaceCommandPreprocessEvent.
     */
    @EventHandler
    public void onCommandPreprocess(@NotNull final PlayerCommandPreprocessEvent event) {
        final String mode = plugin.getConfigManager().getConfig().getString("Mode");

        // Don't block commands if
        if(mode == null || mode.equalsIgnoreCase("HIDE")) {
            return;
        }

        // Allow players with bypass permission to use all commands.
        if(event.getPlayer().hasPermission("commandblocker.bypass")) {
            return;
        }

        final String command = event.getMessage().split(" ")[0];

        if(mode.equalsIgnoreCase("blacklist")) {
            for(final String blacklist : plugin.getConfigManager().getCommands()) {
                if(command.equalsIgnoreCase(blacklist)) {
                    ChatUtils.chat(event.getPlayer(), plugin.getConfigManager().getConfig().getString("Message"));
                    event.setCancelled(true);
                    break;
                }
            }
        }
        else if (mode.equalsIgnoreCase("whitelist")) {
            for(String whitelist : plugin.getConfigManager().getCommands()) {
                if(command.equalsIgnoreCase(whitelist)) {
                    return;
                }
            }

            ChatUtils.chat(event.getPlayer(), plugin.getConfigManager().getConfig().getString("Message"));
            event.setCancelled(true);
        }
    }
}
