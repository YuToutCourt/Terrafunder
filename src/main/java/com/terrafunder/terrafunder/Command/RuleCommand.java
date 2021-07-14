package com.terrafunder.terrafunder.Command;

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
    public static String STRENGHT_POTIONS;
    public static String PROJECTILES;
    public static String NETHER;
    public static String HORSE;
    public static String FIRE_ENCHANTS;
    public static String CUT_CLEAN;


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
                sender.sendMessage("§9-----------Page 1/7------------");
                sender.sendMessage("> §3But : Récupérer le bloc §ad'émeraude §3qui se situe dans le château des §6défenseurs §3et le placer sur le bloc §7bedrock.");
                sender.sendMessage("> §3Au minimum 1 §adéfenseur §3doit mourir (pvp) pour récupérer le bloc §ad'émeraude");
                sender.sendMessage("> §3Les §6défenseurs §3ont le droit de placer le bloc §ad'émeraude         §3n'importe où dans le château, mais une §6face §3du §bbloc doit être visible");
                sender.sendMessage("> §3Les §f§lattaquants §3ne peuvent qu’attaquer à partir du §c§ljour 4 !");
                return true;
            case 2:
                sender.sendMessage("§9-----------Page 2/7------------");
                sender.sendMessage("> §3Pvp activer §c§ljour 3");
                sender.sendMessage("> §3Potion de §4force §3et §dPomme de Noctch §cdésactiver");
                sender.sendMessage("> §3Bordure de map §c1500 §fblocs. §cRéduit §3a 500 blocs §cjour 5");
                sender.sendMessage("> §3Tous technique d'attaques sont autorisées");
                return true;
            case 3:
                sender.sendMessage("§9-----------Page 3/7------------");
                sender.sendMessage("> §3Chaque jour des coffres spawn a des cordonnées §6aléatoire");
                sender.sendMessage("> §3Friendly Fire §cdésactiver");
                sender.sendMessage("> §3Les §f§lattaquants §3ont les droits de casser §aTOUS les blocs du château");
                sender.sendMessage("> §3Pas de §alimite §3de stuff");
                return true;
            case 4:
                sender.sendMessage("§9-----------Page 4/7------------");
                sender.sendMessage("> §3Fire spreading: "+ FIRE_SPREADING);
                sender.sendMessage("> §3Team chat: "+ TEAM_CHAT);
                sender.sendMessage("> §3Invicibility time: §e"+ INVICIBILITY + " second(s)");
                sender.sendMessage("> §3The pvp is enable day: §e"+ TIME_BEFORE_PVP);
                return true;
            case 5:
                sender.sendMessage("§9-----------Page 5/7------------");
                sender.sendMessage("> §3Border size: §e"+ BORDER_START_SIZE);
                sender.sendMessage("> §3Time before border moving: §e"+ TIME_BEFORE_BORDER_MOVE + " §3minute(s)");
                sender.sendMessage("> §3Moving time of the border: §e"+ BORDER_MOVE_DURATION + " §3second(s)");
                sender.sendMessage("> §3Final size of the border: §e"+ BORDER_END_SIZE);
                return true;
            case 6:
                sender.sendMessage("§9-----------Page 6/7------------");
                sender.sendMessage("> §3Notch Apple: "+ NOTCH_APPLE);
                sender.sendMessage("> §3Level 2 Potions: "+ LEVEL_TWO_POTION);
                sender.sendMessage("> §3Strenght Potions: "+ STRENGHT_POTIONS);
                sender.sendMessage("> §3Projectiles Knockback: "+ PROJECTILES);
                return true;
            case 7:
                sender.sendMessage("§9-----------Page 7/7------------");
                sender.sendMessage("> §3Nether: "+ NETHER);
                sender.sendMessage("> §3Horses: "+ HORSE);
                sender.sendMessage("> §3Fire Enchants: "+ FIRE_ENCHANTS);
                sender.sendMessage("> §3Cut Clean: "+ CUT_CLEAN);
                return true;

            default:
                return false;
        }
    }
}
