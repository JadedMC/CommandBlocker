package net.jadedmc.commandblocker.utils;

import org.bukkit.Bukkit;

/**
 * A collection of utilities related to the version of the server.
 */
public class VersionUtils {

    /**
     * Return the major Minecraft version the server is running.
     * Does not work for minor versions.
     * @return Server's major version.
     */
    public static int getServerVersion() {
        String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        return Integer.parseInt(version.split("\\.")[1]);
    }
}
