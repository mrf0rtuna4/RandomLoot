package org.heypers.randomLoot;

import org.bukkit.plugin.java.JavaPlugin;
import org.heypers.randomLoot.listeners.BlockBreakListener;
import org.heypers.randomLoot.managers.LootManager;
import org.heypers.randomLoot.commands.RandomLootCommand;
import org.heypers.randomLoot.config.ConfigManager;

import java.util.Objects;

public class RandomLoot extends JavaPlugin {

    private LootManager lootManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        lootManager = new LootManager(this, configManager);
        lootManager.initializeLoot();

        getServer().getPluginManager().registerEvents(
                new BlockBreakListener(this, lootManager, configManager), this);

        Objects.requireNonNull(getCommand("randomloot")).setExecutor(new RandomLootCommand(this, configManager));

        getLogger().info("§a=== RandomLoot v1.0 успешно загружен! ===");
        getLogger().info("§eВсе предметы загружены: " + lootManager.getTotalItems());
        getLogger().info("§eВсе мобы загружены: " + lootManager.getTotalMobs());
    }

    @Override
    public void onDisable() {
        getLogger().info("§cRandomLoot отключен!");
    }

    public LootManager getLootManager() {
        return lootManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}