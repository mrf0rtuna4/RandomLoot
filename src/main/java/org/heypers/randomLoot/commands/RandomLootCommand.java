package org.heypers.randomLoot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.heypers.randomLoot.config.ConfigManager;
import org.jetbrains.annotations.NotNull;


public class RandomLootCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    public RandomLootCommand(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("randomloot.admin")) {
            sender.sendMessage("§cНет прав на выполнение этой команды!");
            return false;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        return switch (subCommand) {
            case "toggle" -> handleToggle(sender, args);
            case "status" -> handleStatus(sender);
            case "reload" -> handleReload(sender);
            case "help" -> sendHelp(sender);
            default -> {
                sender.sendMessage("§cНеизвестная команда! Используй /randomloot help");
                yield false;
            }
        };
    }

    private boolean handleToggle(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользование: /randomloot toggle <block|mob|event|spawn>");
            return false;
        }

        String target = args[1].toLowerCase();

        switch (target) {
            case "block":
                configManager.enableBlockLoot = !configManager.enableBlockLoot;
                sender.sendMessage("§aЛут из блоков: " +
                        (configManager.enableBlockLoot ? "§aВКЛЮЧЁН" : "§cОТКЛЮЧЕН"));
                break;
            case "mob":
                configManager.enableMobLoot = !configManager.enableMobLoot;
                sender.sendMessage("§aЛут из мобов: " +
                        (configManager.enableMobLoot ? "§aВКЛЮЧЁН" : "§cОТКЛЮЧЕН"));
                break;
            case "event":
                configManager.enableSpecialEvents = !configManager.enableSpecialEvents;
                sender.sendMessage("§aСпециальные события: " +
                        (configManager.enableSpecialEvents ? "§aВКЛЮЧЕНЫ" : "§cОТКЛЮЧЕНЫ"));
                break;
            case "spawn":
                configManager.enableMobSpawn = !configManager.enableMobSpawn;
                sender.sendMessage("§aСпавн мобов: " +
                        (configManager.enableMobSpawn ? "§aВКЛЮЧЕН" : "§cОТКЛЮЧЕН"));
                break;
            default:
                sender.sendMessage("§cНеизвестный параметр!");
                return false;
        }

        return true;
    }

    private boolean handleStatus(CommandSender sender) {
        sender.sendMessage("§6===== RandomLoot Status =====");
        sender.sendMessage("§aЛут из блоков: " +
                (configManager.enableBlockLoot ? "§aВКЛЮЧЁН" : "§cОТКЛЮЧЕН"));
        sender.sendMessage("§aЛут из мобов: " +
                (configManager.enableMobLoot ? "§aВКЛЮЧЁН" : "§cОТКЛЮЧЕН"));
        sender.sendMessage("§aСпециальные события: " +
                (configManager.enableSpecialEvents ? "§aВКЛЮЧЕНЫ" : "§cОТКЛЮЧЕНЫ"));
        sender.sendMessage("§aСпавн мобов: " +
                (configManager.enableMobSpawn ? "§aВКЛЮЧЕН" : "§cОТКЛЮЧЕН"));
        sender.sendMessage("");
        sender.sendMessage("§eВероятности:");
        sender.sendMessage("§6• Спавн моба: " + String.format("%.1f%%",
                configManager.mobSpawnChance * 100));
        sender.sendMessage("§6• Спец. событие: " + String.format("%.1f%%",
                configManager.specialEventChance * 100));
        sender.sendMessage("");
        sender.sendMessage("§eКоличество лута:");
        sender.sendMessage("§6• От " + configManager.minLootCount + " до " +
                configManager.maxLootCount + " предметов");
        sender.sendMessage("§6• Каждый предмет: " + configManager.minLootAmount +
                " - " + configManager.maxLootAmount + " штук");

        return true;
    }

    private boolean handleReload(CommandSender sender) {
        configManager.loadConfig();
        sender.sendMessage("§aКонфиг перезагружен!");
        return true;
    }

    private boolean sendHelp(CommandSender sender) {
        sender.sendMessage("§6===== RandomLoot Help =====");
        sender.sendMessage("§e/randomloot toggle <block|mob|event|spawn> §7- Переключить функцию");
        sender.sendMessage("§e/randomloot status §7- Показать статус");
        sender.sendMessage("§e/randomloot reload §7- Перезагрузить конфиг");
        sender.sendMessage("§e/randomloot help §7- Эта справка");
        return true;
    }
}