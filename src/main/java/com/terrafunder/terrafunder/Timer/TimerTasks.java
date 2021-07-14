package com.terrafunder.terrafunder.Timer;

import com.terrafunder.terrafunder.Terrafunder;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTasks extends BukkitRunnable {

    public static boolean RUN = false;
    public static int day = 1;
    private static int time = 0;

    public static int WBtime = 0;
    private int WBstate = 0;

    private Terrafunder main;

    public TimerTasks(Terrafunder game) {
        this.main = game;
        time = 0;
    }

    @Override
    public void run() {
        this.updateBoards();

        if(RUN) {
            time ++;
            if(WBstate < 2) WBtime --; // changing wb timer only if not finished

            if(WBtime == 0 && WBstate == 0) { // worldborder starts moving
                this.moveWorldBorder();
                WBstate ++;
            }
            if(WBtime == 0 && WBstate == 1) { // worldborder ends moving
                WBstate ++;
            }
            if(time%1200 == 0) day++;

            if(day == 3) { // turn pvp on
                this.main.WORLD.setPVP(true);
                this.main.WORLD.playSound(this.main.WORLD.getSpawnLocation(), Sound.WOLF_GROWL, 1000.0F, 1.0F);
                Bukkit.broadcastMessage("§c§lPvP is now enable!");
            }
        }

    }

    public static void setRunning(boolean state) {
        RUN = state;
    }

    public static void setWordborderTimer(int minutes) {
        WBtime = minutes * 60;
    }

    public static String formatTime(int secs, boolean printHour) {
        ChatColor color = RUN ? ChatColor.YELLOW : ChatColor.RED;
        String ret = "";
        if(!printHour) ret = String.format("%02d:%02d", secs / 60, secs % 60);
        else ret = String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);

        return color + ret.replace(":", ChatColor.RESET + ":" + color);
    }

    public static String formatLine(String key, Object value, ChatColor cVal) {
        ChatColor cKey = ChatColor.GOLD;
        ChatColor cRst = ChatColor.RESET;
        return cKey + key + cRst + " > " + cVal + value;
    }

    public static String formatLine(String key, Object value) {
        return formatLine(key, value, ChatColor.YELLOW);
    }

    private void updateBoards() {
        for(FastBoard board : this.main.boards) {
            board.updateLine(1, formatTime(time, true));
            board.updateLine(3, formatLine("Jour",day));
            board.updateLine(5, formatLine("Defenseur",1));
            board.updateLine(6, formatLine("Attaquant",1));
            board.updateLine(8, formatLine("Border", formatTime(WBtime, false)));
            board.updateLine(9, formatLine("Size", (int)this.main.WORLD.getWorldBorder().getSize()));
        }
    }

    private void moveWorldBorder() {
        this.main.WORLD.playSound(this.main.WORLD.getSpawnLocation(), Sound.ANVIL_LAND, 1000.0F, 1.0F);
        int endSize = this.main.CONFIG.getInt("Border.EndSize");
        int duration = this.main.CONFIG.getInt("Border.MovingDuration");
        WBtime = duration;
        this.main.WORLD.getWorldBorder().setSize(endSize, duration);
        Bukkit.broadcastMessage("§c§lBorder is now moving");
    }
}