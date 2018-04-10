package hellobank.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import hellobank.data.ATM;
import hellobank.data.BankAccount;
import hellobank.main.Main;

public class BankEvents implements Listener {
	
	public BankEvents(Main plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerInteractChest(PlayerInteractEvent e) {
	    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	        if(e.getClickedBlock().getType().equals(Material.CHEST)) {
	        	if (!ATM.isChest(e.getClickedBlock())) {
	        		return;
	        	}
	        	if (!BankAccount.isAccount(e.getPlayer().getUniqueId())) {
	        		e.getPlayer().sendMessage(ChatColor.DARK_RED + "You don't have an account on HelloBank to have access to ATM.");
	        		e.setCancelled(true);
	        	}
	        }
	    }
	}
}
