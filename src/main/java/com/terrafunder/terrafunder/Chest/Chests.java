package com.terrafunder.terrafunder.Chest;

import com.terrafunder.terrafunder.Terrafunder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Chests {

    private Terrafunder main;


    String[] itemToAdd = {"ENDER_PEARL","EXP_BOTTLE","GOLDEN_APPLE","DIAMOND","SADDLE","BOOKSHELF","FERMENTED_SPIDER_EYE","SPECKLED_MELON","FLINT_AND_STEEL",
            "FLINT","SHEARS","TNT","DIAMOND_BOOTS","DIAMOND_LEGGINGS","RABBIT_FOOT","BLAZE_ROD","NETHER_STALK","GHAST_TEAR","MAGMA_CREAM",
            "DIAMOND_CHESTPLATE","DIAMOND_SWORD","BOW","DIAMOND_PICKAXE","DIAMOND_HELMET","GOLDEN_CARROT","GLOWSTONE_DUST","STRING","FEATHER"};


    public Chests(Terrafunder game){
        this.main = game;
    }


    public Location randomLocation(){
        Random rand = new Random();
        int x = negativeOrNot(rand.nextInt((int)this.main.WORLD.getWorldBorder().getSize() / 2));
        int z = negativeOrNot(rand.nextInt((int)this.main.WORLD.getWorldBorder().getSize() / 2));
        int y = this.main.WORLD.getHighestBlockYAt(x, z);
        return new Location(this.main.WORLD,x,y,z);
    }

    private int negativeOrNot(int number){
        Random rand = new Random();
        int random = rand.nextInt(1);
        return random == 0 ? number*-1 : number;
    }


    public void spawnChest(Location location, Set<String> items){
        location.getBlock().setType(Material.CHEST);
        Inventory inventory = ((Chest) location.getBlock().getState()).getInventory();
        for(String item : items){
            inventory.addItem(new ItemStack(Material.valueOf(item),1));
        }
        Bukkit.broadcastMessage(messageToBroadCast(items,location));
    }

    public Set<String> randomItem(){
        Random rand = new Random();
        Set<String> item = new HashSet<String>();
        for(int i = 0; i < rand.nextInt(17) + 3; i++){
            item.add(itemToAdd[rand.nextInt(itemToAdd.length)]);
        }
        return item;
    }

    public String messageToBroadCast(Set<String> item, Location loc){
        String message = "Un ";
        int x,y,z;
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();
        if(item.size() >= 13) message += "§c§lGROS §ecoffre est apparu en §fx §6> " + x + ", §fy §6> " + y + ", §fz §6> " + z;
        else if(item.size() > 9) message += "coffre §a§lMOYEN §eest apparu en §fx §6>" + x + ", §fy §6> " + y + ", §fz §6> " + z;
        else message += "§ecoffre est apparu en §fx §6> " + x + ", §fy §6> " + y + ", §fz §6> " + z;
        return message;
    }

}
