package com.terrafunder.terrafunder.Timer;

import com.terrafunder.terrafunder.Team.Teams;
import com.terrafunder.terrafunder.Terrafunder;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTasks extends BukkitRunnable {

    public static boolean RUN = false;
    public static int day = 1;
    public static int time = 0;

    private boolean castleAttack = false;
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
            if(day >= this.main.CONFIG.getInt("Border.TimeBeforeMoving") && WBstate == 0) { // worldborder starts moving
                this.moveWorldBorder();
                WBstate ++;
            }
            if(WBstate == 1) { // worldborder ends moving
                WBstate ++;
            }
            if(time%1200 == 0) {
                Bukkit.broadcastMessage("§b------ Fin du jour " + day + " -------");
                this.main.WORLD.playSound(this.main.WORLD.getSpawnLocation(),  Sound.CAT_MEOW, 1000.0F, 1.0F);
                day++;
            }

            if(day == 4 && !castleAttack){
                Bukkit.broadcastMessage("§c------ Attaque du Château Disponible -------");
                this.main.WORLD.playSound(this.main.WORLD.getSpawnLocation(), Sound.WOLF_GROWL, 1000.0F, 1.0F);
                castleAttack = true;
            }

            if(day >= this.main.CONFIG.getInt("TimeBeforePvp") && !this.main.WORLD.getPVP()) { // turn pvp on
                this.main.WORLD.setPVP(true);
                this.main.WORLD.playSound(this.main.WORLD.getSpawnLocation(), Sound.WOLF_GROWL, 1000.0F, 1.0F);
                Bukkit.broadcastMessage("§c------ PVP Actif -------");
            }
        }

    }

    public static void setRunning(boolean state) {
        RUN = state;
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
            board.updateLine(5, formatLine(Teams.getColorTeamDef()+"Defenseur", Teams.nbDefenseur()));
            if(Teams.getTeamOf(board.getPlayer()) == null) board.updateLine(6, formatLine("Attaquant",Teams.nbAttacker()));
            else board.updateLine(6, formatLine(Teams.getTeamOf(board.getPlayer()).getColor()+"Attaquant",Teams.nbAttacker()));
            board.updateLine(8, formatLine("Bordure", (int)this.main.WORLD.getWorldBorder().getSize()));
        }
    }

    private void moveWorldBorder() {
        this.main.WORLD.playSound(this.main.WORLD.getSpawnLocation(), Sound.ANVIL_LAND, 1000.0F, 1.0F);
        int endSize = this.main.CONFIG.getInt("Border.EndSize");
        int duration = this.main.CONFIG.getInt("Border.MovingDuration");
        this.main.WORLD.getWorldBorder().setSize(endSize, duration);
        Bukkit.broadcastMessage("§c§lBorder is now moving");
    }
}