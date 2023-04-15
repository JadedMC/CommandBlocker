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

        String command = event.getMessage().split(" ")[0];

        switch (mode.toLowerCase()) {
            case "whitelist" -> {

                for(String whitelist : plugin.getSettingsManager().getConfig().getStringList("Commands")) {
                    if(command.equalsIgnoreCase(whitelist)) {
                        continue;
                    }

                    event.setCancelled(true);
                    break;
                }
            }
            case "blacklist" -> {
                for(String whitelist : plugin.getSettingsManager().getConfig().getStringList("Commands")) {
                    if(command.equalsIgnoreCase(whitelist)) {
                        event.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }
}
