package com.terrafunder.terrafunder.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AlertCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if("alert".equalsIgnoreCase(command.getName()) && sender.isOp()){
            return alert(sender,args);
        }
        return false;
    }


    public boolean alert(CommandSender sender, String[] args){
        if(args.length == 0) return false;
        String message = "§c§l> [SERVER] ";
        for(String letter : args) message += " " + letter;
        Bukkit.broadcastMessage(message);
        return true;

    }
}