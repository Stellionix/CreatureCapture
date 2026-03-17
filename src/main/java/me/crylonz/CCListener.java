package me.crylonz;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.crylonz.CreatureCapture.*;

public class CCListener implements Listener {

    private static final String CAPTURE_ARROW_METADATA = "creaturecapture.capturearrow";
    private final CreatureCapture plugin;

    public CCListener(CreatureCapture plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void addCustomEnchantToTable(PrepareItemEnchantEvent e) {

        double droprate = plugin.getConfig().getDouble("droprate");

        if (e.getEnchantmentBonus() >= 15) {
            if (randVal >= 100 - droprate && e.getItem().getEnchantments().size() == 0) {
                if (e.getItem().getType() == Material.BOW) {
                    e.setCancelled(false);
                    e.getOffers()[2] = new EnchantmentOffer(Enchantment.SILK_TOUCH, 1, 30);
                    isDisplay = true;
                } else {
                    isDisplay = false;
                }
            }
        }
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent e) {

        if (e.getBow() != null && e.getBow().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            if (e.getEntity() instanceof Player) {
                List<String> lores = Objects.requireNonNull(e.getBow().getItemMeta()).getLore();

                if (lores != null && lores.get(0).contains("/")) {
                    int lifeRemaining = Integer.parseInt(
                            ChatColor.stripColor(Objects.requireNonNull(lores).get(0)).split("/")[0]) - 1;

                    if (lifeRemaining == 0) {
                        Player p = (Player) e.getEntity();
                        p.getInventory().removeItem(p.getInventory().getItemInMainHand());
                    }

                    List<String> newLores = new ArrayList<>();

                    newLores.add(lifeRemaining + "/" + lores.get(0).split("/")[1]);

                    ItemMeta meta = e.getBow().getItemMeta();
                    meta.setLore(newLores);
                    e.getBow().setItemMeta(meta);
                }

                if (e.getProjectile() instanceof Projectile projectile) {
                    projectile.setMetadata(CAPTURE_ARROW_METADATA, new FixedMetadataValue(plugin, true));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {

        if (!spawnersCanBeModifiedByEgg) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.SPAWNER) {
                    e.setCancelled(true);
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                e.getItem() != null &&
                e.getItem().getType() == Material.POLAR_BEAR_SPAWN_EGG &&
                e.getItem().getItemMeta().getDisplayName().contains("Golem")
        ) {
            e.setCancelled(true);
            Location playerTargetBlock = e.getPlayer().getTargetBlock(null, 0).getLocation().add(0, 1, 0);
            e.getPlayer().getWorld().spawnEntity(playerTargetBlock, EntityType.IRON_GOLEM);
            ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
            ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();

            if (maxDurability != -1) {
                if (itemInMainHand.isSimilar(e.getItem())) {
                    itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
                }

                if (itemInOffHand.isSimilar(e.getItem())) {
                    itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent e) {

        randVal = Math.random() * 100;

        if (e.getItem().getType() == Material.BOW && e.getExpLevelCost() == 30 && isDisplay) {
            generateCaptureBow(e.getItem());
            isDisplay = false;
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile projectile) || !projectile.hasMetadata(CAPTURE_ARROW_METADATA)) {
            return;
        }

        Player player = getShooter(projectile);
        if (player == null || e.getEntity() instanceof Player) {
            return;
        }

        if (tryCapture(player, e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (!projectile.hasMetadata(CAPTURE_ARROW_METADATA)) {
            return;
        }

        Entity hitEntity = e.getHitEntity();
        if (hitEntity == null || hitEntity instanceof Player) {
            return;
        }

        Player player = getShooter(projectile);
        if (player != null) {
            tryCapture(player, hitEntity);
        }
    }

    private void sendCaptureMessage(Player player, EntityType entityType, boolean isNewCapture) {
        if (isNewCapture) {
            player.sendMessage(ChatColor.GOLD + "New creature discovered: " + ChatColor.GREEN + CreatureCapture.formatEntityTypeName(entityType.name()));
        } else {
            player.sendMessage(ChatColor.YELLOW + "Captured " + ChatColor.WHITE + CreatureCapture.formatEntityTypeName(entityType.name()));
        }
    }

    private Player getShooter(Projectile projectile) {
        ProjectileSource shooter = projectile.getShooter();
        if (shooter instanceof Player player && player.hasPermission("creaturecapture.capture")) {
            return player;
        }
        return null;
    }

    private boolean tryCapture(Player player, Entity target) {
        if (!target.isValid() || target.isDead()) {
            return false;
        }

        Boolean allowed = spawnableMobEggs.get(target.getType().toString());
        if (allowed == null || !allowed || chanceToCapture < Math.random() * 100) {
            return false;
        }

        ItemStack egg;
        try {
            if (Bukkit.getVersion().contains("1.12")) {
                egg = new ItemStack(Objects.requireNonNull(Material.getMaterial("MONSTER_EGG")), 1, target.getType().getTypeId());
            } else if (target.getType() == EntityType.IRON_GOLEM) {
                egg = createIronGolemEgg();
            } else {
                Material spawnEggMaterial = CreatureCapture.resolveSpawnEggMaterial(target.getType());
                if (spawnEggMaterial == null) {
                    return false;
                }
                egg = new ItemStack(spawnEggMaterial);
            }
        } catch (IllegalArgumentException ignored) {
            plugin.getLogger().severe(ignored.getMessage());
            if (target.getType() != EntityType.IRON_GOLEM) {
                return false;
            }
            egg = createIronGolemEgg();
        }

        player.getWorld().dropItem(target.getLocation(), egg);
        boolean isNewCapture = plugin.recordCapture(player.getUniqueId(), player.getName(), target.getType());
        target.remove();
        player.getWorld().playEffect(target.getLocation(), Effect.ENDER_SIGNAL, 10);
        sendCaptureMessage(player, target.getType(), isNewCapture);
        return true;
    }

    private ItemStack createIronGolemEgg() {
        ItemStack golemEgg = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG, 1);
        ItemMeta meta = golemEgg.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Iron Golem Spawn Egg");
        golemEgg.setItemMeta(meta);
        return golemEgg;
    }
}
