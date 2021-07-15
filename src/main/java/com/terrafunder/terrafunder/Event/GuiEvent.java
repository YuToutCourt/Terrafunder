package com.terrafunder.terrafunder.Event;

import com.terrafunder.terrafunder.Team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiEvent implements Listener {

    private final static Integer VALUE = 15;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if(item == null) return;
        if (item.getType() == Material.CHEST && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Sélecteur de team")){
            Inventory inv = Bukkit.createInventory(null, 45, "§6Sélecteur de team");
            ItemStack glass = newItem(Material.STAINED_GLASS_PANE,1, (byte)(VALUE.byteValue() - Teams.getDataTeamDef()));
            ItemStack bannerDef = newItem(Material.BANNER,1,Teams.getDataTeamDef());
            ItemMeta itemMetaDef = bannerDef.getItemMeta();
            itemMetaDef.setDisplayName(Teams.getColorTeamDef() + "Rejoignez la Team Defenseur");
            bannerDef.setItemMeta(itemMetaDef);

            for(int i=0;i<=9;i++) inv.setItem(i,glass);
            inv.setItem(13,bannerDef);
            inv.setItem(17,glass);

            int index = 27;
            for(Teams team : Teams.teams){
                if(!team.getName().equals("Defenseur")){
                    ItemStack banner = newItem(Material.BANNER,1,team.getData());
                    ItemMeta itemMeta = banner.getItemMeta();
                    itemMeta.setDisplayName(team.getColor() + "Rejoignez la Team " + team.getName());
                    banner.setItemMeta(itemMeta);
                    inv.setItem(index, banner);
                    index++;
                }
            }
            player.openInventory(inv);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();
        if (current == null) return;
        // CANCEL PLAYER TO TAKE THE ITEM IN THE INV
        if (inv.getName().equalsIgnoreCase("§6Sélecteur de team")) {
            if(current != null && current.getType().equals(Material.BANNER)){
                for(Teams team : Teams.teams){
                    // FUCKING DEPRECRATED CODE
                    if (team.getData() == current.getData().getData()){
                        Teams.addPlayer(player,team);
                        player.sendMessage(team.getColor()+ "Tu a rejoin la team " + team.getName());
                    }
                }
            }
            event.setCancelled(true);
        }

    }

    private ItemStack newItem(Material mat,int amount, byte data){
        return new ItemStack(mat,amount,data);
    }

}