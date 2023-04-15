package net.jadedmc.commandblocker.commands;

import net.jadedmc.commandblocker.CommandBlocker;
import net.jadedmc.commandblocker.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * This class runs the /commandblocker command, which is the main admin command for the plugin.
 * aliases:
 * - cb
 */
public class CommandBlockerCMD implements CommandExecutor {
    private final CommandBlocker plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public CommandBlockerCMD(CommandBlocker plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return If the command was successful.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Makes sure the sender has permission to use the command.
        if(!sender.hasPermission("commandblocker.admin")) {
            ChatUtils.chat(sender, plugin.getSettingsManager().getConfig().getString("Message"));
            return true;
        }

        // Makes sure the command has an argument.
        if(args.length == 0) {
            args = new String[]{"help"};
        }

        // Get the sub command used.
        String subCommand = args[0];

        // Reloads all plugin configuration files.
        if(subCommand.equalsIgnoreCase("reload")) {
            plugin.getSettingsManager().reload();
            ChatUtils.chat(sender, "<green><bold>CommandBlocker</bold> <dark_gray>» <green>Configuration files reloaded successfully!");
        }

        // Displays the plugin's current version.
        else if (subCommand.equalsIgnoreCase("version")) {
            ChatUtils.chat(sender, "<green><bold>CommandBlocker</bold> <dark_gray>» <green>Current version: <white>" + plugin.getDescription().getVersion());
        }

        // Displays the help menu.
        else {
            ChatUtils.chat(sender, "<green><bold>CommandBlocker Commands");
            ChatUtils.chat(sender, "<green>/cb reload <dark_gray>» <white>Reloads all configuration files.");
            ChatUtils.chat(sender, "<green>/cb version <dark_gray>» <white>Displays the plugin version.");
        }

        return true;
    }
}