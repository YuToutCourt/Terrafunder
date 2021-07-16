package com.terrafunder.terrafunder.Event;

import com.terrafunder.terrafunder.Terrafunder;
import com.terrafunder.terrafunder.Timer.TimerTasks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class CancelDeathEvent implements Listener {


    private Terrafunder main;

    public CancelDeathEvent(Terrafunder game){
        this.main = game;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(TimerTasks.day < this.main.CONFIG.getInt("TimeBeforePvp")){
            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                if (event.getFinalDamage() >= player.getHealth()){
                    event.setCancelled(true);
                    player.sendMessage("§a>[SERVEUR] Le bon dieu vous fait grâce de votre mort");
                }
            }
        }
    }
}
