package com.terrafunder.terrafunder.Event;

import com.terrafunder.terrafunder.Team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class WinEvent implements Listener {

    @EventHandler
    public void winEmeraldBlock(BlockPlaceEvent event){
        Teams team = Teams.getTeamOf(event.getPlayer());
        Material blockPlaced = event.getBlockPlaced().getType();
        Location locationBlock = event.getBlockPlaced().getLocation();
        Location locationUnder = new Location(locationBlock.getWorld(), locationBlock.getX(), locationBlock.getY()-1, locationBlock.getZ());
        Material blockUnder = locationUnder.getBlock().getType();
        if(blockPlaced.equals(Material.EMERALD_BLOCK) && blockUnder.equals(Material.BEDROCK) && !team.getName().equals("Defenseur") && team != null){
            Bukkit.broadcastMessage("§c§l> [SERVEUR] VICTOIRE DE LA TEAM : " + team.getColor() + team.getName());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(Teams.nbAttacker() == 0){
            Bukkit.broadcastMessage("§c§l> [SERVEUR] LES " + Teams.getColorTeamDef() + "DEFENSEURS §cONT GAGNER !");
        }
    }
}
