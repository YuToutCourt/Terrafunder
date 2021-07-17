package com.terrafunder.terrafunder.Command;

import com.terrafunder.terrafunder.Terrafunder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RuleCommand implements CommandExecutor {

    public static String FIRE_SPREADING;
    public static String TEAM_CHAT;
    public static String INVICIBILITY;
    public static String TIME_BEFORE_PVP;
    public static String BORDER_START_SIZE;
    public static String TIME_BEFORE_BORDER_MOVE;
    public static String BORDER_END_SIZE;
    public static String BORDER_MOVE_DURATION;

    public static String NOTCH_APPLE;
    public static String LEVEL_TWO_POTION;
    public static String STRENGTHS_POTIONS;
    public static String PROJECTILES;
    public static String NETHER;
    public static String HORSE;
    public static String FIRE_ENCHANTS;
    public static String CUT_CLEAN;
    public static String FRIENDLY_FIRE;
    public static String NOOB_MODE;


    private Terrafunder main;

    public RuleCommand(Terrafunder game){
        this.main = game;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if("rule".equalsIgnoreCase(command.getName())){
            return rule(sender,args);
        }
        return false;
    }

    public boolean rule(CommandSender sender, String[] args){
        int page;
        try {
            page = Integer.parseInt(args[0]);
        } catch (Exception e) {
            return false;
        }
        switch (page) {
            //TODO FRENCH OR ENGLISH
            case 1:
                sender.sendMessage("§9-----------Page 1/6------------");
                sender.sendMessage("> §3But : Récupérer le bloc §ad'émeraude §3qui se situe dans le château des §6défenseurs §3et le placer sur le bloc §7bedrock.");
                sender.sendMessage("> §3Au minimum 1 §adéfenseur §3doit mourir (pvp) pour récupérer le bloc §ad'émeraude");
                sender.sendMessage("> §3Les §6défenseurs §3ont le droit de placer le bloc §ad'émeraude         §3n'importe où dans le château, mais une §6face §3du §bbloc doit être visible");
                sender.sendMessage("> §3Les §f§lattaquants §3ne peuvent qu’attaquer à partir du §c§ljour 4 !");
                return true;
            case 2:
                sender.sendMessage("§9-----------Page 2/6------------");
                sender.sendMessage("> §3Tous technique d'attaques sont autorisées");
                sender.sendMessage("> §3Pvp activer §c§ljour "+TIME_BEFORE_PVP);
                sender.sendMessage("> §3Bordure de map §c"+ this.main.CONFIG.getInt("Border.StartSize")+" §fblocs. §cRéduit §3a " + this.main.CONFIG.getInt("Border.EndSize") +" blocs §cjour "+ this.main.CONFIG.getInt("Border.TimeBeforeMoving"));
                sender.sendMessage("> §3Chaque jour des coffres spawn a des cordonnées §6aléatoire");
                return true;
            case 3:
                sender.sendMessage("§9-----------Page 3/6------------");
                sender.sendMessage("> §3Les §f§lattaquants §3ont les droits de casser §aTOUS les blocs du château");
                sender.sendMessage("> §3Pas de §alimite §3de stuff");
                sender.sendMessage("> §3Friendly Fire: "+FRIENDLY_FIRE);
                return true;
            case 4:
                sender.sendMessage("§9-----------Page 4/6------------");
                sender.sendMessage("> §3Fire spreading: "+ FIRE_SPREADING);
                sender.sendMessage("> §3Team chat: "+ TEAM_CHAT);
                sender.sendMessage("> §3Invicibility time: §e"+ INVICIBILITY + " second(s)");
                sender.sendMessage("> §3Noob mode: "+ NOOB_MODE);
                return true;
            case 5:
                sender.sendMessage("§9-----------Page 5/6------------");
                sender.sendMessage("> §3Pomme de Notch : "+ NOTCH_APPLE);
                sender.sendMessage("> §3Potions niveau 2 : "+ LEVEL_TWO_POTION);
                sender.sendMessage("> §3Potions de force : "+ STRENGTHS_POTIONS);
                sender.sendMessage("> §3Road/Egg/Snowball: "+ PROJECTILES);
                return true;
            case 6:
                sender.sendMessage("§9-----------Page 6/6------------");
                sender.sendMessage("> §3Nether: "+ NETHER);
                sender.sendMessage("> §3Cheuvaux: "+ HORSE);
                sender.sendMessage("> §3Fire Enchants: "+ FIRE_ENCHANTS);
                sender.sendMessage("> §3Cut Clean: "+ CUT_CLEAN);
                return true;

            default:
                return false;
        }
    }
}
