package hellobank.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import hellobank.data.ATM;
import hellobank.data.BankAccount;
import hellobank.main.Main;
import hellobank.utils.Utils;

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
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent e) {
		Block brokenBlock = e.getBlock();
		ATM chest = ATM.getChest(brokenBlock);
		if (chest != null) {
			if (!e.getPlayer().hasPermission(Utils.pm)) {
				e.getPlayer().sendMessage(ChatColor.RED + "You can not break ATMs!");
				e.setCancelled(true);
				return;
			} else {
				ATM.atms.remove(chest);
				String id = Utils.getIdByBlock(brokenBlock);
				if (id != null) {
					Main.INSTANCE.getConfig().set("ATM." + id, null);
				}
				Utils.count = Utils.count - 1;
			}
		}
	}
}
