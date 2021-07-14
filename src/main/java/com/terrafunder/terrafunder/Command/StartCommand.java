package com.terrafunder.terrafunder.Command;

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
        int invicibilityDuration = this.main.CONFIG.getInt("Invicibility");

        for(Player player : Bukkit.getOnlinePlayers()){

            playersInTheParty.add(player.getUniqueId());

            for(PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            // reset achievements
            for(Achievement a : Achievement.values()) {
                if(player.hasAchievement(a)) player.removeAchievement(a);
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*invicibilityDuration, 255));
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

        this.main.WORLD.setDifficulty(Difficulty.HARD);

        TimerTasks timer = new TimerTasks(this.main);
        timer.runTaskTimer(this.main, 0, 20);
        TimerTasks.setRunning(true);
        timer.run();
        return true;
    }
}
