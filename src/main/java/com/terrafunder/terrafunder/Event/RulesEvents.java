package com.terrafunder.terrafunder.Event;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;

public class RulesEvents implements Listener {

    public static boolean NOTCH_APPLE;
    public static boolean LEVEL_TWO_POTION;
    public static boolean STRENGHT_POTIONS;
    public static boolean PROJECTILES;
    public static boolean NETHER;
    public static boolean HORSE;
    public static boolean FIRE_ENCHANTS;

    @EventHandler
    public void onCraftNotchApple(CraftItemEvent event) {
        if(NOTCH_APPLE) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack itemCrafted = event.getRecipe().getResult();

        if(!itemCrafted.getType().equals(Material.GOLDEN_APPLE)) return;
        if(itemCrafted.getAmount() != 1) return; // 0 = golden apple, 1 = notch apple

        player.sendMessage("§cNotch apples have been disabled");
        event.setCancelled(true);
    }

    @EventHandler
    public void blockEmerald (CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        Material item = event.getRecipe().getResult().getType();
        if (item.equals(Material.EMERALD_BLOCK)) {
            player.sendMessage("§4§l> [SERVEUR] " + player.getDisplayName() + " Les blocs d'émeraude on était désactiver");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBrewPotion(BrewEvent event) {
        Material item = event.getContents().getIngredient().getType();
        // Level 2 potion
        if(item.equals(Material.GLOWSTONE_DUST)){
            if(LEVEL_TWO_POTION) return;
            event.setCancelled(true);
        }

        // Strenght potion
        if(item.equals(Material.BLAZE_POWDER)){
            if(STRENGHT_POTIONS) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if(PROJECTILES) return;

        if(!(event.getDamager() instanceof Projectile)) return; // only Projectiles
        Projectile projectile = (Projectile) event.getDamager();
        if(projectile.getType().equals(EntityType.ARROW)) return;
        if(projectile.getType().equals(EntityType.ENDER_PEARL)) return;

        if(!(projectile.getShooter() instanceof Player)) return; // only Player attackers
        Player attacker = (Player) projectile.getShooter();

        if(!(event.getEntity() instanceof Player)) return; // only Player victims

        event.setCancelled(true);
        attacker.sendMessage("§cProjectiles damages have been disabled");
    }

    @EventHandler
    public void onNetherPortal(PlayerPortalEvent event){
        if(NETHER) return;

        Player player = event.getPlayer();
        if(event.getCause() != PlayerPortalEvent.TeleportCause.NETHER_PORTAL) return;

        event.setCancelled(true);
        player.sendMessage("§cNether have been disabled");
    }

    @EventHandler
    public void onClickHorse(PlayerInteractEntityEvent event) {
        if(HORSE) return;

        Player player = event.getPlayer();
        if(!(event.getRightClicked() instanceof Horse)) return;

        player.sendMessage("§cHorse have been disabled");
        event.setCancelled(true);
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event){
        if(FIRE_ENCHANTS) return;

        event.getEnchantsToAdd().remove(Enchantment.FIRE_ASPECT);
        event.getEnchantsToAdd().remove(Enchantment.ARROW_FIRE);
    }

}
