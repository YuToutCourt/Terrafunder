package com.terrafunder.terrafunder.Team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teams {

    public static List<Teams> teams = new ArrayList<Teams>();

    private final String name;
    private final ChatColor color;
    private List<UUID> listOfPlayers;


    public Teams(String nom,String colorCode){
        this.name = nom;
        this.color = ChatColor.valueOf(colorCode.toUpperCase());;
        listOfPlayers = new ArrayList<UUID>();
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getListOfPlayerInTheTeam() {
        return listOfPlayers;
    }

    public void setListOfPlayerInTheTeam(List<UUID> listOfPlayerInTheTeam) {
        this.listOfPlayers = listOfPlayerInTheTeam;
    }

    public int nbPlayerInTeam(){
        return this.listOfPlayers.size();
    }

    public void setColorForPlayer(Player player){
        ChatColor color = getTeamOf(player).getColor();
        player.setDisplayName(color+player.getName());
        player.setPlayerListName(color+player.getName());
    }

    public static Teams getTeamOf(Player player){
        for(int i=0; i<teams.size();i++){
            if(teams.get(i).listOfPlayers.contains(player.getUniqueId())){
                return teams.get(i);
            }
        }
        return null;
    }



}