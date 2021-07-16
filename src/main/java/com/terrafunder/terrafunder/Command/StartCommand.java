package com.terrafunder.terrafunder.Command;

import com.terrafunder.terrafunder.Team.Teams;
import com.terrafunder.terrafunder.Terrafunder;
import com.terrafunder.terrafunder.Timer.TimerTasks;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Random;

public class StartCommand implements CommandExecutor {

    public static List<UUID> playersInTheParty = new ArrayList<>();

    private Terrafunder main;

    public StartCommand(Terrafunder game){
        this.main = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if("start".equalsIgnoreCase(command.getName()) && sender.isOp()){
            return start();
        }
        return false;
    }


    public boolean start(){

        this.main.WORLD.setDifficulty(Difficulty.PEACEFUL);
        this.main.WORLD.setTime(0);
        int borderSize = (int) this.main.WORLD.getWorldBorder().getSize() / 2;
        this.main.WORLD.setGameRuleValue("doFireTick", this.main.CONFIG.getString("World.EnableFireSpreading"));
        int invincibilityDuration = this.main.CONFIG.getInt("Invicibility");

        if(!this.main.CONFIG.getBoolean("NaturalRegeneration"))
            this.main.WORLD.setGameRuleValue("naturalRegeneration", "false");

        for(Player player : Bukkit.getOnlinePlayers()){

            playersInTheParty.add(player.getUniqueId());

            for(PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            // reset achievements
            for(Achievement a : Achievement.values()) {
                if(player.hasAchievement(a)) player.removeAchievement(a);
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*invincibilityDuration, 255));
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setExp(0);
            Inventory inv = player.getInventory();
            inv.clear();
            inv.addItem(new ItemStack(Material.COOKED_BEEF, 64));
        }
        for(int i = 0; i < 100; i++) Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.RED +"> §l[SERVEUR] §cPréparation au lancement du §a§lTerrafunder ... ");

        tpTeam(); // PLS WORK YOU WILL HAVE A COOKIE

        for(Teams team : Teams.teams){
            if(team.getName().equals("Defenseur")){
                tpPlayerByTeam(team,this.main.CONFIG.getInt("SpawnDef.x"),this.main.CONFIG.getInt("SpawnDef.z"));
                break;
            }

        }


        this.main.WORLD.setDifficulty(Difficulty.NORMAL);

        TimerTasks timer = new TimerTasks(this.main);
        timer.runTaskTimer(this.main, 0, 20);
        TimerTasks.setRunning(true);
        timer.run();
        return true;
    }

    private void tpTeam(){
        Random rand = new Random();
        boolean firstTeam = true;
        int coordUsed[][] = new int[Teams.teams.size()][2];
        int index = 0;
        int x,z;
        for(Teams team : Teams.teams){
            if(!team.getName().equals("Defenseur")){
                x = negativeOrNot(rand.nextInt((int)this.main.WORLD.getWorldBorder().getSize() / 2));
                z = negativeOrNot(rand.nextInt((int)this.main.WORLD.getWorldBorder().getSize() / 2));
                if (firstTeam){
                    coordUsed[index][0] = x;
                    coordUsed[index][1] = z;
                    tpPlayerByTeam(team,x,z);
                    firstTeam = false;
                }
                else {
                    while (!checkProximity(x,z,coordUsed)){
                        x = negativeOrNot(rand.nextInt((int)this.main.WORLD.getWorldBorder().getSize() / 2));
                        z = negativeOrNot(rand.nextInt((int)this.main.WORLD.getWorldBorder().getSize() / 2));
                    }
                    coordUsed[index][0] = x;
                    coordUsed[index][1] = z;
                    tpPlayerByTeam(team,x,z);
                }
                index++;
            }
        }
    }

    private void tpPlayerByTeam(Teams team, int x, int z){
        Location location = new Location(this.main.WORLD,x,this.main.WORLD.getHighestBlockYAt(x, z)+2,z);
        for(UUID uuid : team.getListOfPlayerInTheTeam()){
            Bukkit.getPlayer(uuid).teleport(location);
        }
    }

    private int negativeOrNot(int number){
        Random rand = new Random();
        int random = rand.nextInt(2);
        return random == 0 ? number*-1 : number;
    }

    private boolean checkProximity(int x0,int z0, int listCoord[][]){
        int count = 0;
        int x1,z1;
        for(int i=0;i<listCoord.length;i++){
            x1 = listCoord[i][0];
            z1 = listCoord[i][1];
            if(Math.sqrt(Math.pow((x1 - x0), 2) + Math.pow((z1 - z0), 2)) >= 150) count++;
        }
        return count == listCoord.length;
    }


}
