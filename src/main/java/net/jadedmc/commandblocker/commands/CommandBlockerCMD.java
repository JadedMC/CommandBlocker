package net.jadedmc.commandblocker.commands;

import net.jadedmc.commandblocker.CommandBlocker;
import net.jadedmc.commandblocker.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class runs the /commandblocker command, which is the main admin command for the plugin.
 * aliases:
 * - cb
 */
public class CommandBlockerCMD implements CommandExecutor, TabCompleter {
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
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            // Adds a command to the command list.
            case "add":
                // Makes sure the command is being used properly.
                if(args.length != 2) {
                    ChatUtils.chat(sender, "<red><bold>Usage</bold> <dark_gray>» <red>/cb add [command]");
                    return true;
                }

                // Gets the command being added to the list.
                String toBlock = args[1];

                // Makes sure the command starts with a '/'.
                if(!toBlock.startsWith("/")) {
                    toBlock = "/" + toBlock;
                }

                // Adds the command to the list.
                List<String> commands = plugin.getSettingsManager().getConfig().getStringList("Commands");
                commands.add(toBlock.toLowerCase());

                // Reloads the config file.
                plugin.getSettingsManager().getConfig().set("Commands", commands);
                plugin.getSettingsManager().save();
                plugin.getSettingsManager().reload();

                ChatUtils.chat(sender, "<green><bold>CommandBlocker</bold> <dark_gray>» <green>Added <white>" + toBlock.toLowerCase() + " <green>to the command list.");
                return true;

            case "mode":
                // Makes sure the command is being used properly.
                if(args.length != 2) {
                    ChatUtils.chat(sender, "<red><bold>Usage</bold> <dark_gray>» <red>/cb mode [mode]");
                    return true;
                }

                // Gets the intended mode to switch to.
                String mode = args[1].toUpperCase();
                List<String> validModes = Arrays.asList("BLACKLIST", "WHITELIST", "HIDE");

                // Makes sure the mode is valid.
                if(!validModes.contains(mode)) {
                    ChatUtils.chat(sender, "<red><bold>Error</bold> <dark_gray>» <red>Mode must be either <white><hover:show_text:\"Click to select BLACKLIST\"><click:suggest_command:\"/cb mode BLACKLIST\">BLACKLIST</click></hover><red>, <white><hover:show_text:\"Click to select WHITELIST\"><click:suggest_command:\"/cb mode WHITELIST\">WHITELIST</click></hover><red>, or <white><hover:show_text:\"Click to select HIDE\"><click:suggest_command:\"/cb mode HIDE\">HIDE</click></hover><red>.");
                    return true;
                }

                // Reloads the config file.
                plugin.getSettingsManager().getConfig().set("Mode", mode);
                plugin.getSettingsManager().save();
                plugin.getSettingsManager().reload();

                ChatUtils.chat(sender, "<green><bold>CommandBlocker</bold> <dark_gray>» <green>Changed the current mode to <white>" + mode + " <green>.");
                return true;

            // Reloads all plugin configuration files.
            case "reload":
                plugin.getSettingsManager().reload();
                ChatUtils.chat(sender, "<green><bold>CommandBlocker</bold> <dark_gray>» <green>Configuration files reloaded successfully!");
                return true;

            // Displays the plugin version.
            case "version":
                ChatUtils.chat(sender, "<green><bold>CommandBlocker</bold> <dark_gray>» <green>Current version: <white>" + plugin.getDescription().getVersion());
                return true;

            // Displays the help menu.
            default:
                ChatUtils.chat(sender, "<green><bold>CommandBlocker Commands");
                ChatUtils.chat(sender, "<green><click:suggest_command:\"/cb add \">/cb add [command]</click> <dark_gray>» <white>Add a command to the command list.");
                ChatUtils.chat(sender, "<green><click:suggest_command:\"/cb mode \">/cb mode [mode]</click> <dark_gray>» <white>Change the mode the plugin is set to.");
                ChatUtils.chat(sender, "<green><click:suggest_command:\"/cb reload\">/cb reload</click> <dark_gray>» <white>Reloads all configuration files.");
                ChatUtils.chat(sender, "<green><click:suggest_command:\"/cb version\">/cb version</click> <dark_gray>» <white>Displays the plugin version.");
                return true;
        }
    }

    /**
     * Processes command tab completion.
     * @param sender Command sender.
     * @param cmd Command.
     * @param label Command label.
     * @param args Arguments of the command.
     * @return Tab completion.
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        // Return an empty list if the player does not have permission.
        if(!sender.hasPermission("commandblocker.admin")) {
            return Collections.emptyList();
        }

        // Lists all sub commands if the player hasn't picked one yet.
        if(args.length < 2) {
            return Arrays.asList("add", "help", "mode", "reload", "version");
        }

        // Only check the first argument of each sub command.
        if(args.length == 2) {
            String subCommand = args[0].toLowerCase();

            // Displays tab complete for the mode sub command.
            if(subCommand.equals("mode")) {
                return Arrays.asList("BLACKLIST", "HIDE", "WHITELIST");
            }
        }

        // Otherwise, send an empty list.
        return Collections.emptyList();
    }
}