package com.terrafunder.terrafunder.Event;

import com.terrafunder.terrafunder.Command.StartCommand;
import com.terrafunder.terrafunder.Team.Teams;
import com.terrafunder.terrafunder.Terrafunder;
import com.terrafunder.terrafunder.Timer.TimerTasks;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayersEvent implements Listener {


    private Terrafunder main;

    public PlayersEvent(Terrafunder game){
        this.main = game;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(Teams.getTeamOf(player) != null) Teams.setColorForPlayer(player);
        event.setJoinMessage("§7[§3+§7] " + player.getDisplayName());
        this.main.boards.add(this.main.createBoard(player));

        if(!TimerTasks.RUN) {
            Material blockAtSpawn = this.main.WORLD.getBlockAt(this.main.WORLD.getSpawnLocation().getBlockX(),this.main.WORLD.getSpawnLocation().getBlockY()-1,this.main.WORLD.getSpawnLocation().getBlockZ()).getType();
            if(!(blockAtSpawn.equals(Material.GLASS))){
                // WAIT A BIT FOR THE CHUNCK TO BE LOADED
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        createSpawn();
                    }
                };
                task.runTaskLater(this.main, 20*1);
            }
            player.teleport(this.main.WORLD.getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);
            ItemStack chesteam = new ItemStack(Material.CHEST,1);
            ItemMeta _Chest = chesteam.getItemMeta();
            _Chest.setDisplayName("§6Sélecteur de team");
            chesteam.setItemMeta(_Chest);
            player.getInventory().clear();
            player.getInventory().addItem(chesteam);

        } else {
            if(!StartCommand.playersInTheParty.contains(player.getUniqueId()))
                player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage("§7[§4-§7] " + player.getDisplayName());
        this.main.removeBoardOf(player);
    }


    @EventHandler
    public void damageEvent(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))  return;
        if(this.main.CONFIG.getBoolean("Teams.FriendlyFire")) return;
        Player victim = (Player) event.getEntity();
        Player attacker = null;
        if(event.getDamager() instanceof Player) attacker = (Player) event.getDamager();
        else if(event.getDamager() instanceof Projectile) attacker = (Player) ((Projectile) event.getDamager()).getShooter();
        if(attacker == null) return;
        if(Teams.getTeamOf(victim).equals(Teams.getTeamOf(attacker)) || victim.equals(attacker)){
            attacker.sendMessage(Teams.getTeamOf(attacker).getColor()+"Pas de friendly fire, BAKA !");
            event.setCancelled(true);
        }
    }



    private void createSpawn(){

        Location spawn = this.main.WORLD.getSpawnLocation();

        String createCube = "fill " + (spawn.getBlockX() - 10) + " " + (spawn.getBlockY() - 1) + " " + (spawn.getBlockZ() - 10);
        createCube += " " + (spawn.getBlockX() + 10) + " " + (spawn.getBlockY() + 4) + " " + (spawn.getBlockZ() + 10);
        createCube += " minecraft:glass";

        String carveCube = "fill " + (spawn.getBlockX() - 9) + " " + (spawn.getBlockY()) + " " + (spawn.getBlockZ() - 9);
        carveCube += " " + (spawn.getBlockX() + 9) + " " + (spawn.getBlockY() + 3) + " " + (spawn.getBlockZ() + 9);
        carveCube += " minecraft:air";

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), createCube);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), carveCube);

    }
}
