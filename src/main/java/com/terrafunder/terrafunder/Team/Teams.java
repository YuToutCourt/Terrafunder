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
    private final byte data;


    public Teams(String nom,String colorCode,String data){
        this.name = nom;
        this.color = ChatColor.valueOf(colorCode.toUpperCase());;
        this.data = Byte.parseByte(data);
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

    public byte getData() {
        return data;
    }

    public static int nbDefenseur(){
        for(Teams team : teams){
            if(team.getName().equals("Defenseur")){
                return team.listOfPlayers.size();
            }
        }
        return 0;
    }
    public static ChatColor getColorTeamDef(){
        for(Teams team : teams){
            if(team.getName().equals("Defenseur")){
                return team.getColor();
            }
        }
        return null;
    }

    public static int nbAttacker(){
        int nbAtt = 0;
        for(Teams team : teams){
            if(!team.getName().equals("Defenseur"))
                nbAtt += team.listOfPlayers.size();
        }
        return nbAtt;
    }

    public static void setColorForPlayer(Player player){
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

    public static void addPlayer(Player player, Teams team){
        removePlayer(player);
        for(int i=0; i<teams.size();i++){
            if(teams.get(i).equals(team)){
                teams.get(i).listOfPlayers.add(player.getUniqueId());
                setColorForPlayer(player);
            }
        }
    }
    public static void removePlayer(Player player){
        for(int i=0; i<teams.size();i++){
            if(teams.get(i).listOfPlayers.contains(player.getUniqueId())){
                teams.get(i).listOfPlayers.remove(player.getUniqueId());
            }
        }
    }



}