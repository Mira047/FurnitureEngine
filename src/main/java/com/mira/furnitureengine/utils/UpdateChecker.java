package com.mira.furnitureengine.utils;

import com.mira.furnitureengine.FurnitureEngine;
import org.bukkit.Bukkit;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public record UpdateChecker(int resourceId) {
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(FurnitureEngine.getInstance(), () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                FurnitureEngine.getInstance().getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}