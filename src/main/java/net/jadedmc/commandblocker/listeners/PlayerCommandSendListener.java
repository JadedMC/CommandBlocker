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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to the PlayerCommandSendEvent, which runs every time the server sends the command list to the player.
 *
 */
public class PlayerCommandSendListener implements Listener {
    private final CommandBlockerPlugin plugin;

    /**
     * Creates the listener.
     * @param plugin Instance of the plugin.
     */
    public PlayerCommandSendListener(@NotNull final CommandBlockerPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlayerCommandSendEvent.
     */
    @EventHandler
    public void onCommandSend(@NotNull final PlayerCommandSendEvent event) {
        final String mode = plugin.getSettingsManager().getConfig().getString("Mode");

        // Don't do anything if no mode was set.
        if(mode == null) {
            return;
        }

        // Prevent hiding commands if the player has the bypass permission.
        if(event.getPlayer().hasPermission("commandblocker.bypass")) {
            return;
        }

        final List<String> commands = plugin.getSettingsManager().getConfig().getStringList("Commands");
        final List<String> tablist = new ArrayList<>(event.getCommands());

        // Hides all commands with a ':' in them if enabled.
        if(plugin.getSettingsManager().getConfig().getBoolean("HideColonCommands")) {
            for(final String command : tablist) {
                if(command.contains(":")) {
                    event.getCommands().remove(command);
                }
            }
        }

        // If in whitelist mode, removes all non-whitelisted commands.
        if(mode.equalsIgnoreCase("WHITELIST")) {
            event.getCommands().removeIf(commands::contains);

            for(final String command : tablist) {
                if(commands.contains("/" + command.toLowerCase())) {
                    continue;
                }

                event.getCommands().remove(command);
            }
        }

        // If in blacklist or hide mode, removes all blacklisted commands.
        if(mode.equalsIgnoreCase("BLACKLIST") || mode.equalsIgnoreCase("HIDE")) {
            for(final String command : commands) {
                event.getCommands().remove(command.replace("/", ""));
            }
        }
    }
}
