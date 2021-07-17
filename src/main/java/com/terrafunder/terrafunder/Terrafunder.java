package com.terrafunder.terrafunder;

import com.terrafunder.terrafunder.Command.AlertCommand;
import com.terrafunder.terrafunder.Command.RuleCommand;
import com.terrafunder.terrafunder.Command.SetDayCommand;
import com.terrafunder.terrafunder.Command.StartCommand;
import com.terrafunder.terrafunder.Event.*;
import com.terrafunder.terrafunder.Team.Teams;
import com.terrafunder.terrafunder.Timer.TimerTasks;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Terrafunder extends JavaPlugin{

    public FileConfiguration CONFIG;
    public final List<FastBoard> boards = new ArrayList<FastBoard>();
    public World WORLD;
    public boolean PVP = false;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        CONFIG = this.getConfig();
        // Plugin startup logic
        Bukkit.broadcastMessage("§a-------- Terrafunder On ---------");
        this.getCommand("alert").setExecutor(new AlertCommand());
        this.getCommand("start").setExecutor(new StartCommand(this));
        this.getCommand("rule").setExecutor(new RuleCommand(this));
        this.getCommand("setday").setExecutor(new SetDayCommand());

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new ChatEvent(this), this);
        pm.registerEvents(new RulesEvents(), this);
        pm.registerEvents(new PlayersEvent(this), this);
        pm.registerEvents(new BlockBreakEmerald(),this);
        pm.registerEvents(new DeathEvent(this),this);
        pm.registerEvents(new GuiEvent(),this);
        pm.registerEvents(new WinEvent(this),this);
        if(CONFIG.getBoolean("NoobMode")) pm.registerEvents(new CancelDeathEvent(this),this);
        if(CONFIG.getBoolean("CutClean")) pm.registerEvents(new CutCleanEvent(), this);
        if(!CONFIG.getBoolean("World.BadWeather")) pm.registerEvents(new RainEvent(), this);
        this.resetGame();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.broadcastMessage("§c------- Terrafunder Off --------");
    }

    public void resetGame(){
        WORLD = Bukkit.getWorld(CONFIG.getString("World.WorldName"));
        WORLD.setPVP(false);
        WORLD.setSpawnLocation(CONFIG.getInt("Spawn.x"), CONFIG.getInt("Spawn.y"), CONFIG.getInt("Spawn.z"));
        WORLD.getWorldBorder().setCenter(WORLD.getSpawnLocation());
        WORLD.getWorldBorder().setSize(CONFIG.getInt("Border.StartSize"));

        // Reset teams
        List<String> teams = CONFIG.getStringList("Teams.Teams");
        Teams.teams.clear();
        for(int i = 0; i < teams.size(); i++) {
            String currentTeam = teams.get(i);
            String[] splitTeam = currentTeam.split(",");
            Teams.teams.add(new Teams(splitTeam[1],splitTeam[0],splitTeam[2]));
        }

        // Reset timers
        TimerTasks.setRunning(false);

        // Reset objectives & gamerules
        RulesEvents.NOTCH_APPLE = CONFIG.getBoolean("NotchApple");
        RulesEvents.STRENGTH_POTIONS = CONFIG.getBoolean("StrenghtPotions");
        RulesEvents.LEVEL_TWO_POTION = CONFIG.getBoolean("LevelTwoPotions");
        RulesEvents.PROJECTILES = CONFIG.getBoolean("ProjectilesKnockback");
        RulesEvents.HORSE = CONFIG.getBoolean("AllowHorse");
        RulesEvents.NETHER = CONFIG.getBoolean("AllowNether");
        RulesEvents.FIRE_ENCHANTS = CONFIG.getBoolean("FireAndFlame");

        RuleCommand.FIRE_SPREADING = CONFIG.getBoolean("World.EnableFireSpreading") ? "§aOn" : "§cOff";
        RuleCommand.TEAM_CHAT = CONFIG.getBoolean("Teams.TeamChat") ? "§aOn" : "§cOff";
        RuleCommand.INVICIBILITY = CONFIG.getString("Invicibility");
        RuleCommand.TIME_BEFORE_PVP = CONFIG.getString("TimeBeforePvp");
        RuleCommand.BORDER_START_SIZE = CONFIG.getString("Border.StartSize");
        RuleCommand.TIME_BEFORE_BORDER_MOVE = CONFIG.getString("Border.TimeBeforeMoving");
        RuleCommand.BORDER_END_SIZE = CONFIG.getString("Border.EndSize");
        RuleCommand.BORDER_MOVE_DURATION = CONFIG.getString("Border.MovingDuration");

        RuleCommand.NOTCH_APPLE = RulesEvents.NOTCH_APPLE ? "§aOn" : "§cOff";
        RuleCommand.STRENGTHS_POTIONS = RulesEvents.STRENGTH_POTIONS ? "§aOn" : "§cOff";
        RuleCommand.LEVEL_TWO_POTION = RulesEvents.LEVEL_TWO_POTION ? "§aOn" : "§cOff";
        RuleCommand.PROJECTILES = RulesEvents.PROJECTILES ? "§aOn" : "§cOff";
        RuleCommand.HORSE = RulesEvents.HORSE ? "§aOn" : "§cOff";
        RuleCommand.NETHER = RulesEvents.NETHER ? "§aOn" : "§cOff";
        RuleCommand.FIRE_ENCHANTS = RulesEvents.FIRE_ENCHANTS ? "§aOn" : "§cOff";
        RuleCommand.CUT_CLEAN = CONFIG.getBoolean("CutClean") ? "§aOn" : "§cOff";
        RuleCommand.FRIENDLY_FIRE = CONFIG.getBoolean("Teams.FriendlyFire") ? "§aOn" : "§cOff";
        RuleCommand.NOOB_MODE = CONFIG.getBoolean("NoobMode") ? "§aOn" : "§cOff";


        // Reset scoreboard
        for(FastBoard board : this.boards) {
            board.delete();
        }
        this.boards.clear();
        for(Player p : Bukkit.getOnlinePlayers()) {
            this.boards.add(this.createBoard(p));
        }
    }
    public FastBoard createBoard(Player player) {
        String SEPARATOR = ChatColor.RED + "";
        FastBoard board = new FastBoard(player);
        board.updateTitle(ChatColor.DARK_GREEN + "Terra§a§lFunder");

        List<String> lines = new ArrayList<String>();
        lines.add(SEPARATOR);
        lines.add(TimerTasks.formatTime(0, true));
        lines.add(SEPARATOR);
        lines.add(TimerTasks.formatLine("Day", 0));
        lines.add(SEPARATOR);
        lines.add(TimerTasks.formatLine(Teams.getColorTeamDef()+"Defenseur", 0));
        lines.add(TimerTasks.formatLine("Attaquant", 0));
        lines.add(SEPARATOR);
        lines.add(TimerTasks.formatLine("Bordure", 0));
        lines.add(SEPARATOR);
        board.updateLines(lines);

        return board;
    }

    public void removeBoardOf(Player player) {
        for(FastBoard board : this.boards) {
            if(board.getPlayer().getUniqueId() == player.getUniqueId()) {
                board.delete();
                this.boards.remove(board);
                return;
            }
        }
    }
}
