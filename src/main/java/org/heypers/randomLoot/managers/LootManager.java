package org.heypers.randomLoot.managers;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.heypers.randomLoot.config.ConfigManager;
import java.util.*;

public class LootManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final List<Material> validItems = new ArrayList<>();
    private final List<EntityType> validMobs = new ArrayList<>();
    private final Random random = new Random();

    public LootManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void initializeLoot() {
        loadAllItems();
        loadAllMobs();

        plugin.getLogger().info("§eЗагружено предметов: " + validItems.size());
        plugin.getLogger().info("§eЗагружено мобов: " + validMobs.size());
    }

    private void loadAllItems() {
        validItems.clear();

        for (Material material : Material.values()) {
            if (!material.isItem()) continue;

            if (material == Material.AIR || material == Material.WATER ||
                    material == Material.LAVA) continue;

            if (configManager.excludedItems.contains(material.toString())) continue;

            validItems.add(material);
        }

        validItems.sort(Comparator.comparing(Material::toString));
    }

    private void loadAllMobs() {
        validMobs.clear();

        Set<EntityType> excludedMobs = new HashSet<>(Arrays.asList(
                EntityType.ARMOR_STAND,
                EntityType.AREA_EFFECT_CLOUD,
                EntityType.ARROW,
                EntityType.SPECTRAL_ARROW,
                EntityType.TRIDENT,
                EntityType.EVOKER_FANGS,
                EntityType.EXPERIENCE_ORB,
                EntityType.ITEM,
                EntityType.ITEM_FRAME,
                EntityType.GLOW_ITEM_FRAME,
                EntityType.PAINTING,
                EntityType.FISHING_BOBBER,
                EntityType.LEASH_KNOT,
                EntityType.WITHER,
                EntityType.ENDER_DRAGON,
                EntityType.UNKNOWN
        ));

        for (EntityType type : EntityType.values()) {
            if (type.isAlive() && !excludedMobs.contains(type)) {
                try {
                    Class<?> entityClass = Class.forName("org.bukkit.entity." + type);
                    if (!entityClass.isInterface()) {
                        validMobs.add(type);
                    }
                } catch (ClassNotFoundException ignored) {
                    // skip sous
                }
            }
        }

        validMobs.sort(Comparator.comparing(EntityType::toString));
    }

    public ItemStack getRandomItem() {
        if (validItems.isEmpty()) {
            return new ItemStack(Material.DIAMOND);
        }

        Material material = validItems.get(random.nextInt(validItems.size()));
        int amount = random.nextInt(configManager.maxLootAmount - configManager.minLootAmount + 1)
                + configManager.minLootAmount;

        return new ItemStack(material, amount);
    }

    public List<ItemStack> getRandomLoot() {
        List<ItemStack> loot = new ArrayList<>();
        int count = random.nextInt(configManager.maxLootCount - configManager.minLootCount + 1)
                + configManager.minLootCount;

        for (int i = 0; i < count; i++) {
            loot.add(getRandomItem());
        }

        return loot;
    }

    public EntityType getRandomMob() {
        if (validMobs.isEmpty()) {
            return EntityType.ZOMBIE;
        }

        return validMobs.get(random.nextInt(validMobs.size()));
    }

    public boolean shouldSpawnMob() {
        return configManager.enableMobSpawn &&
                random.nextDouble() < configManager.mobSpawnChance;
    }

    public SpecialEventType getSpecialEventType() {
        if (!configManager.enableSpecialEvents) {
            return SpecialEventType.NONE;
        }

        double rand = random.nextDouble();

        if (rand < configManager.explosionEventChance) {
            return SpecialEventType.EXPLOSION;
        } else if (rand < configManager.explosionEventChance + configManager.effectEventChance) {
            return SpecialEventType.EFFECT;
        } else if (rand < configManager.explosionEventChance + configManager.effectEventChance
                + configManager.particleEventChance) {
            return SpecialEventType.PARTICLE;
        } else if (rand < configManager.specialEventChance) {
            return SpecialEventType.LIGHTNING;
        }

        return SpecialEventType.NONE;
    }

    public int getTotalItems() {
        return validItems.size();
    }

    public int getTotalMobs() {
        return validMobs.size();
    }

    public enum SpecialEventType {
        NONE,
        EXPLOSION,
        EFFECT,
        PARTICLE,
        LIGHTNING
    }
}
