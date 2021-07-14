package com.terrafunder.terrafunder.Event;

import com.terrafunder.terrafunder.Team.Teams;
import com.terrafunder.terrafunder.Terrafunder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    private Terrafunder main;

    public ChatEvent(Terrafunder game){
        this.main = game;
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event){
        Player playerSender = event.getPlayer();
        String message = event.getMessage();
        if(this.main.CONFIG.getBoolean("Teams.TeamChat") && message.charAt(0) == '!'){
            event.setCancelled(true);
            for(Player p : Bukkit.getOnlinePlayers()){
                if(Teams.getTeamOf(p).equals(Teams.getTeamOf(playerSender))){
                    p.sendMessage(Teams.getTeamOf(p).getColor() + "[" + Teams.getTeamOf(p).getName() + "] "+ playerSender.getName()+ "§4->"+ formatMessage(message));
                }
            }
        }
        else{
            event.setFormat("§4<- "+Teams.getTeamOf(playerSender).getColor() +  playerSender.getDisplayName() + " §4-> " + message); // DarkRed <- ColorTeam DarkRed-> ColorTeam <message>

        }
    }

    private String formatMessage(String message){
        String newMessage  ="";
        String[] toLoop = message.split("");
        for(int i=1;i<toLoop.length;i++) {
            newMessage += toLoop[i];
        }
        return newMessage;
    }

}
