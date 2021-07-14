package com.terrafunder.terrafunder.Event;

import com.terrafunder.terrafunder.Team.Teams;
import com.terrafunder.terrafunder.Timer.TimerTasks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEmerald implements Listener {

    @EventHandler
    public void moveEmeraldBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();
        Teams team = Teams.getTeamOf(player);
        if (block.equals(Material.EMERALD_BLOCK)){
            if (team.getName().equals("Defenseur")){
                Bukkit.broadcastMessage("§c§lLa team des " + team.getColor() + " Défenseur "+ "§c§l ont pris le bloc §ad'émeraude");
            }
            else if (!team.getName().equals("Defenseur") && TimerTasks.day < 4){
                player.sendMessage(team.getColor()+"§c§l Attendez le jours 4");
                event.setCancelled(true);
            }
            else if (!team.getName().equals("Defenseur") && !DeathEvent.defenderDeath){
                player.sendMessage(team.getColor() + "§c§l Vous devez tuer au moins 1 défenseur");
                event.setCancelled(true);
            }
            else {
                Bukkit.broadcastMessage("§c§lLa team " + team.getColor() + team.getName() + "§c§l ont pris le bloc §ad'émeraude");

            }

        }
    }

}
