package com.terrafunder.terrafunder.Command;

import com.terrafunder.terrafunder.Timer.TimerTasks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetDayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if("setday".equalsIgnoreCase(command.getName()) && sender.isOp()){
            return day(sender,args);
        }
        return false;
    }

    public boolean day(CommandSender sender, String[] args){
        if(args.length == 0) return false;
        try{
            int day = Integer.parseInt(args[0]);
            TimerTasks.day = day;
            sender.sendMessage("§eLe jour a été fixée à §c"+ args[0]);
            return true;
        }
        catch(Exception e){return false;}
    }
}

