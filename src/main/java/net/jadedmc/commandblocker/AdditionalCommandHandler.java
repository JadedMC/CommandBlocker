/*
 * This file is part of CommandBlocker, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 */
package net.jadedmc.commandblocker;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Registers AdditionalCommands on the server's CommandMap so the client
 * recognizes them in tab-complete and doesn't show "Unknown command" error.
 */
public class AdditionalCommandHandler {
    private static final String FALLBACK_PREFIX = "commandblocker";

    private final CommandBlockerPlugin plugin;
    private final List<AdditionalCommand> registered = new ArrayList<>();
    private final CommandMap commandMap;

    public AdditionalCommandHandler(@NotNull final CommandBlockerPlugin plugin) {
        this.plugin = plugin;
        this.commandMap = resolveCommandMap();
        registerAll();
    }

    private CommandMap resolveCommandMap() {
        try {
            final Field field = findField(Bukkit.getServer().getClass(), "commandMap");
            if(field == null) {
                plugin.getLogger().warning("Could not locate commandMap field. AdditionalCommands will not work.");
                return null;
            }
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (final IllegalAccessException exception) {
            plugin.getLogger().warning("Could not access CommandMap: " + exception.getMessage());
            return null;
        }
    }

    public void registerAll() {
        if(commandMap == null) return;

        for(final String name : plugin.getConfigManager().getAdditionalCommands()) {
            registerCommand(name);
        }

        refreshOnlinePlayers();
    }

    private void registerCommand(@NotNull final String rawName) {
        final String name = rawName.toLowerCase().trim();
        if(name.isEmpty()) return;
        if(commandMap.getCommand(name) != null) return;

        final AdditionalCommand cmd = new AdditionalCommand(name);
        commandMap.register(FALLBACK_PREFIX, cmd);
        registered.add(cmd);
    }

    public void unregisterAll() {
        if(commandMap == null || registered.isEmpty()) return;

        try {
            final Field knownCommandsField = findField(commandMap.getClass(), "knownCommands");
            if(knownCommandsField == null) {
                plugin.getLogger().warning("Could not find knownCommands field. AdditionalCommands may linger until restart.");
                return;
            }
            knownCommandsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

            for(final AdditionalCommand cmd : registered) {
                final String name = cmd.getName();
                if(knownCommands.get(name) == cmd) {
                    knownCommands.remove(name);
                }
                if(knownCommands.get(FALLBACK_PREFIX + ":" + name) == cmd) {
                    knownCommands.remove(FALLBACK_PREFIX + ":" + name);
                }
                cmd.unregister(commandMap);
            }
            registered.clear();
            refreshOnlinePlayers();
        } catch (final IllegalAccessException exception) {
            plugin.getLogger().warning("Could not unregister AdditionalCommands: " + exception.getMessage());
        }
    }

    private Field findField(@NotNull Class<?> type, @NotNull final String name) {
        while(type != null && type != Object.class) {
            try {
                return type.getDeclaredField(name);
            } catch (final NoSuchFieldException ignored) {
                type = type.getSuperclass();
            }
        }
        return null;
    }

    private void refreshOnlinePlayers() {
        for(final Player player : Bukkit.getOnlinePlayers()) {
            player.updateCommands();
        }
    }

    public void reload() {
        unregisterAll();
        registerAll();
    }

    /**
     * Marker subclass so we can reliably identify commands registered by this plugin.
     */
    private static final class AdditionalCommand extends Command {
        private AdditionalCommand(@NotNull final String name) {
            super(name);
        }

        @Override
        public boolean execute(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String[] args) {
            return true;
        }
    }
}
