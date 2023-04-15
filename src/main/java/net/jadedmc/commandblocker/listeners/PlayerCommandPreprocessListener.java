package net.jadedmc.commandblocker.listeners;

import net.jadedmc.commandblocker.CommandBlocker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {
    private final CommandBlocker plugin;

    public PlayerCommandPreprocessListener(CommandBlocker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        String mode = plugin.getSettingsManager().getConfig().getString("Mode");

        if(mode == null) {
            return;
        }

        String command = event.getMessage().split(" ")[0];

        if(mode.equalsIgnoreCase("blacklist")) {
            for(String whitelist : plugin.getSettingsManager().getConfig().getStringList("Commands")) {
                if(command.equalsIgnoreCase(whitelist)) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
        else if (mode.equalsIgnoreCase("whitelist")) {
            for(String whitelist : plugin.getSettingsManager().getConfig().getStringList("Commands")) {
                if(command.equalsIgnoreCase(whitelist)) {
                    continue;
                }

                event.setCancelled(true);
                break;
            }
        }
    }
}
