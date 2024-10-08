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
package net.jadedmc.commandblocker.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.commandblocker.CommandBlockerPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of chat-related utility methods.
 */
public class ChatUtils {
    private static BukkitAudiences adventure;
    private static CommandBlockerPlugin plugin;

    /**
     * Creates an instance of adventure using an instance of the plugin.
     * Called when the plugin is enabled.
     * @param pl Instance of the plugin.
     */
    public static void initialize(@NotNull final CommandBlockerPlugin pl) {
        plugin = pl;
        adventure = BukkitAudiences.create(pl);
    }

    /**
     * Sets the instance of adventure to null when called.
     * Called when the plugin is disabled to prevent potential memory leaks.
     */
    public static void disable() {
        if(adventure != null) {
            adventure.close();
            adventure = null;
        }
    }

    /**
     * A quick way to send a CommandSender a colored message.
     * @param sender CommandSender to send message to.
     * @param message The message being sent.
     */
    public static void chat(@NotNull final CommandSender sender, @NotNull final String message) {
        adventure.sender(sender).sendMessage(translate(message));
    }

    /**
     * A quick way to send a Player a colored message.
     * Supports PlaceholderAPI placeholders if installed.
     * @param player Player to send message to.
     * @param message The message being sent.
     */
    public static void chat(@NotNull final Player player, @NotNull String message) {
        // Translates placeholders if needed.
        if(plugin.getHookManager().usePlaceholderAPI()) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        // Sends the message to the player.
        adventure.sender(player).sendMessage(translate(message));
    }

    /**
     * Translates a String to a colorful String using methods in the BungeeCord API.
     * @param message Message to translate.
     * @return Translated Message.
     */
    public static Component translate(@NotNull final String message) {
        return MiniMessage.miniMessage().deserialize(replaceLegacy(message));
    }

    /**
     * Replaces the legacy color codes used in a message with their MiniMessage counterparts.
     * @param message Message to replace color codes in.
     * @return Message with the color codes replaced.
     */
    public static String replaceLegacy(@NotNull String message) {

        // If the version is 1.16 or greater, check for hex color codes.
        if(VersionUtils.getServerVersion() >= 16) {
            Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String color = message.substring(matcher.start() + 1, matcher.end());
                message = message.replace("&" + color, "<reset><color:" + color + ">");
                matcher = pattern.matcher(message);
            }
        }

        // Then replace legacy color codes.
        return message
                .replace("\\n", "<newline>")
                .replace("§", "&")
                .replace("&0", "<reset><black>")
                .replace("&1", "<reset><dark_blue>")
                .replace("&2", "<reset><dark_green>")
                .replace("&3", "<reset><dark_aqua>")
                .replace("&4", "<reset><dark_red>")
                .replace("&5", "<reset><dark_purple>")
                .replace("&6", "<reset><gold>")
                .replace("&7", "<reset><gray>")
                .replace("&8", "<reset><dark_gray>")
                .replace("&9", "<reset><blue>")
                .replace("&a", "<reset><green>")
                .replace("&b", "<reset><aqua>")
                .replace("&c", "<reset><red>")
                .replace("&d", "<reset><light_purple>")
                .replace("&e", "<reset><yellow>")
                .replace("&f", "<reset><white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<u>")
                .replace("&o", "<i>")
                .replace("&r", "<reset>");
    }
}