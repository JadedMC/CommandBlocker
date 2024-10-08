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
package net.jadedmc.commandblocker;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Keeps track of other plugins installed on the server that we may want to interface with.
 */
public class HookManager {
    private final boolean hasBetterReload;
    private final boolean hasPlaceholderAPI;

    /**
     * Creates the hook manager.
     * @param plugin Instance of the plugin.
     */
    public HookManager(@NotNull final Plugin plugin) {
        this.hasBetterReload = plugin.getServer().getPluginManager().isPluginEnabled("BetterReload");
        if(this.hasBetterReload) plugin.getLogger().info("BetterReload detected. Enabling hook...");

        this.hasPlaceholderAPI = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        if(this.hasBetterReload) plugin.getLogger().info("PlaceholderAPI detected. Enabling hook...");
    }

    /**
     * Get if the plugin should use BetterReload.
     * @return Whether the plugin should interface with BetterReload.
     */
    public boolean useBetterReload() {
        return this.hasBetterReload;
    }

    /**
     * Get if the plugin should use PlaceholderAPI.
     * @return Whether the plugin should interface with PlaceholderAPI.
     */
    public boolean usePlaceholderAPI() {
        return this.hasPlaceholderAPI;
    }
}