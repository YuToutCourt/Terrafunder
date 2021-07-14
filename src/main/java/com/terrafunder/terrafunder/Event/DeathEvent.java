package com.terrafunder.terrafunder.Event;

import com.terrafunder.terrafunder.Team.Teams;
import com.terrafunder.terrafunder.Terrafunder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    public static boolean defenderDeath = false;
    private Terrafunder main;

    public DeathEvent(Terrafunder game){
        this.main = game;
    }

    @EventHandler
    public void onDeath (PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Location deathLocation = victim.getLocation();
        victim.spigot().respawn();
        victim.setGameMode(GameMode.SPECTATOR);
        victim.teleport(deathLocation);
        this.main.WORLD.playSound(deathLocation, Sound.ZOMBIE_REMEDY, 1000.0F, 1.0F);

        if (!(killer instanceof Player)) {
            event.setDeathMessage("§c§l† " + Teams.getTeamOf(victim) + victim.getDisplayName() + " est mort PVE comme une merde, sa Team l'a bannie §c§l†");
        }
        else {
            event.setDeathMessage("§c§l† " + Teams.getTeamOf(victim) + victim.getDisplayName() + ChatColor.RED + " §là était tuer par " + Teams.getTeamOf(killer) + killer.getDisplayName() + " §c§l†");
            if(Teams.getTeamOf(victim).getName().equals("Defenseur") && !defenderDeath){
                defenderDeath = true;
                Bukkit.broadcastMessage("§c§l† §eUn §6défenseur §eest mort. Les §f§lattaquants §epeuvent prendre le bloc §ad'émeraude §esi on et minimum jours 4 §c§l†");
            }
        }

    }


}
