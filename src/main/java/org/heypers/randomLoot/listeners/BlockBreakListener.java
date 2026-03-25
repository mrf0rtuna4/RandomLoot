package org.heypers.randomLoot.listeners;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.heypers.randomLoot.config.ConfigManager;
import org.heypers.randomLoot.managers.LootManager;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BlockBreakListener implements Listener {

    private final JavaPlugin plugin;
    private final LootManager lootManager;
    private final Random random = new Random();
    private final ConfigManager configManager;

    public BlockBreakListener(JavaPlugin plugin, LootManager lootManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.lootManager = lootManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();

        if (isBlockBlacklisted(block)) {
            return;
        }

        if (!configManager.blockWhitelist.isEmpty() &&
                !isBlockWhitelisted(block)) {
            return;
        }

        event.setDropItems(false);

        Location blockLocation = block.getLocation().add(0.5, 0.5, 0.5);

        if (configManager.enableBlockLoot) {
            List<ItemStack> loot = lootManager.getRandomLoot();
            for (ItemStack item : loot) {
                block.getWorld().dropItemNaturally(blockLocation, item);
            }
        }

        if (lootManager.shouldSpawnMob()) {
            spawnRandomMob(blockLocation);
        }

        handleSpecialEvent(blockLocation);
    }

    private boolean isBlockBlacklisted(Block block) {
        String blockName = block.getType().toString();
        return configManager.blockBlacklist.contains(blockName);
    }

    private boolean isBlockWhitelisted(Block block) {
        String blockName = block.getType().toString();
        return configManager.blockWhitelist.contains(blockName);
    }

    private void spawnRandomMob(Location location) {
        try {
            EntityType mobType = lootManager.getRandomMob();
            Entity mob = location.getWorld().spawnEntity(location, mobType);

            if (mob instanceof LivingEntity livingMob) {

                double health = 5.0 + random.nextDouble() * 10.0;
                double maxHealth = Math.min(health, Objects.requireNonNull(livingMob.getAttribute(
                        Attribute.MAX_HEALTH)).getValue());
                livingMob.setHealth(Math.min(health, maxHealth));

                location.getWorld().spawnParticle(
                        Particle.EXPLOSION,
                        location,
                        10,
                        0.5,
                        0.5,
                        0.5,
                        0.1
                );
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Ошибка при спавне моба: " + e.getMessage());
        }
    }

    private void handleSpecialEvent(Location location) {
        LootManager.SpecialEventType eventType = lootManager.getSpecialEventType();

        switch (eventType) {
            case EXPLOSION:
                handleExplosion(location);
                break;
            case EFFECT:
                handleEffect(location);
                break;
            case PARTICLE:
                handleParticle(location);
                break;
            case LIGHTNING:
                handleLightning(location);
                break;
            case NONE:
            default:
                break;
        }
    }

    private void handleExplosion(Location location) {
        float explosionPower = 1.0f + random.nextFloat() * 1.5f;
        location.getWorld().createExplosion(
                location.getX(),
                location.getY(),
                location.getZ(),
                explosionPower,
                false,
                false
        );
    }

    private void handleEffect(Location location) {
        PotionEffectType[] effects = {
                PotionEffectType.SPEED,
                PotionEffectType.HASTE,
                PotionEffectType.STRENGTH,
                PotionEffectType.JUMP_BOOST,
                PotionEffectType.GLOWING,
                PotionEffectType.SLOWNESS,
                PotionEffectType.WEAKNESS
        };

        PotionEffectType effect = effects[random.nextInt(effects.length)];
        int duration = 100 + random.nextInt(200);
        int amplifier = random.nextInt(3);

        for (Entity entity : location.getWorld().getNearbyEntities(location, 10, 10, 10)) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(effect, duration, amplifier)
                );
            }
        }
    }

    private void handleParticle(Location location) {
        Particle[] particles = {
                Particle.ENCHANT,
                Particle.DRAGON_BREATH,
                Particle.END_ROD,
                Particle.FIREWORK,
                Particle.DRIPPING_WATER,
                Particle.SMOKE,
                Particle.CLOUD,
                Particle.PORTAL
        };

        Particle particle = particles[random.nextInt(particles.length)];
        location.getWorld().spawnParticle(
                particle,
                location,
                30 + random.nextInt(20),
                0.5,
                0.5,
                0.5,
                0.05
        );
    }

    private void handleLightning(Location location) {
        location.getWorld().strikeLightning(location);
    }
}
