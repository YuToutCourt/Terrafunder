package com.terrafunder.terrafunder.Event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CutCleanEvent implements Listener {

    @EventHandler
    public void onBreakOre(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material mat = block.getType();
        if(!(mat.equals(Material.GOLD_ORE) || mat.equals(Material.IRON_ORE))) return;
        event.setCancelled(true);
        block.setType(Material.AIR);
        block.getState().update();
        if(mat.equals(Material.GOLD_ORE)) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
            player.giveExp(4);
        }
        if(mat.equals(Material.IRON_ORE)) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
            player.giveExp(2);
        }

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        for(ItemStack item : event.getDrops()) {
            switch (item.getType()) {
                case RAW_CHICKEN:
                    item.setType(Material.COOKED_CHICKEN);
                    continue;
                case RAW_BEEF:
                    item.setType(Material.COOKED_BEEF);
                    continue;
                case MUTTON:
                    item.setType(Material.COOKED_MUTTON);
                    continue;
                case RAW_FISH:
                    item.setType(Material.COOKED_FISH);
                    continue;
                case RABBIT:
                    item.setType(Material.COOKED_RABBIT);
                    continue;
                default:
                    continue;
            }
        }
    }
}
