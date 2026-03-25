package org.heypers.randomLoot.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ConfigManager {

    private final JavaPlugin plugin;
    private YamlConfiguration config;

    public boolean enableBlockLoot = true;
    public boolean enableMobLoot = true;
    public boolean enableMobSpawn = true;
    public boolean enableSpecialEvents = true;

    public double mobSpawnChance = 0.15;
    public double specialEventChance = 0.10;
    public double explosionEventChance = 0.05;
    public double effectEventChance = 0.08;
    public double particleEventChance = 0.12;

    public int minLootAmount = 1;
    public int maxLootAmount = 5;
    public int minLootCount = 1;
    public int maxLootCount = 3;

    public java.util.List<String> blockBlacklist;
    public java.util.List<String> blockWhitelist;

    public java.util.List<String> excludedItems;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.blockBlacklist = new java.util.ArrayList<>();
        this.blockWhitelist = new java.util.ArrayList<>();
        this.excludedItems = new java.util.ArrayList<>();
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(
                new File(plugin.getDataFolder(), "config.yml"));

        enableBlockLoot = config.getBoolean("settings.enable-block-loot", true);
        enableMobLoot = config.getBoolean("settings.enable-mob-loot", true);
        enableMobSpawn = config.getBoolean("settings.enable-mob-spawn", true);
        enableSpecialEvents = config.getBoolean("settings.enable-special-events", true);

        mobSpawnChance = config.getDouble("settings.mob-spawn-chance", 0.15);
        specialEventChance = config.getDouble("settings.special-event-chance", 0.10);
        explosionEventChance = config.getDouble("settings.explosion-event-chance", 0.05);
        effectEventChance = config.getDouble("settings.effect-event-chance", 0.08);
        particleEventChance = config.getDouble("settings.particle-event-chance", 0.12);

        minLootAmount = config.getInt("settings.min-loot-amount", 1);
        maxLootAmount = config.getInt("settings.max-loot-amount", 5);
        minLootCount = config.getInt("settings.min-loot-count", 1);
        maxLootCount = config.getInt("settings.max-loot-count", 3);

        blockBlacklist = config.getStringList("settings.block-blacklist");
        blockWhitelist = config.getStringList("settings.block-whitelist");
        excludedItems = config.getStringList("settings.excluded-items");

        plugin.getLogger().info("§aКонфиг загружен успешно!");
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}