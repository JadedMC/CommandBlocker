package net.jadedmc.commandblocker.listeners;

import net.jadedmc.commandblocker.CommandBlocker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommandSendListener implements Listener {
    private final CommandBlocker plugin;

    public PlayerCommandSendListener(CommandBlocker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        String mode = plugin.getSettingsManager().getConfig().getString("Mode");

        if(mode == null) {
            return;
        }

        if(event.getPlayer().hasPermission("commandblocker.bypass")) {
            return;
        }

        List<String> commands = plugin.getSettingsManager().getConfig().getStringList("Commands");
        List<String> tablist = new ArrayList<>(event.getCommands());

        // Hides all commands with a ':' in them if enabled.
        if(plugin.getSettingsManager().getConfig().getBoolean("HideColonCommands")) {
            for(String command : tablist) {
                if(command.contains(":")) {
                    event.getCommands().remove(command);
                }
            }
        }

        // If in whitelist mode, removes all non-whitelisted commands.
        if(mode.equalsIgnoreCase("WHITELIST")) {
            event.getCommands().removeIf(commands::contains);

            for(String command : tablist) {
                if(commands.contains("/" + command.toLowerCase())) {
                    continue;
                }

                event.getCommands().remove(command);
            }
        }

        // If in blacklist or hide mode, removes all blacklisted commands.
        if(mode.equalsIgnoreCase("BLACKLIST") || mode.equalsIgnoreCase("HIDE")) {
            for(String command : commands) {
                event.getCommands().remove(command.replace("/", ""));
            }
        }
    }
}
