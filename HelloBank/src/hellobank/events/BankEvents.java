package hellobank.events;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import hellobank.data.ATM;
import hellobank.data.BankAccount;
import hellobank.main.Main;
import hellobank.utils.Utils;

public class BankEvents implements Listener {
	Map<Inventory, Player> invs = Maps.newHashMap();
	
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
	        	Player plr = (Player) e.getPlayer();
	        	BankAccount acc = BankAccount.getAccountFromUUID(plr.getUniqueId());
	        	e.setCancelled(true);
	        	if (acc == null) {
	        		 plr.sendMessage(ChatColor.RED + "You don't have an account on HelloBank to have access to ATM.");
	        		return;
	        	}
	        	if ((System.currentTimeMillis() - acc.getLastPasswordCheck()) > 30000) {
	        		 plr.sendMessage(ChatColor.RED + "Use /bank pin <pin> to use ATMs for the next 30 seconds.");
	        		 return;
	        	}
	        	Inventory inv = Bukkit.createInventory(null, 54);
	        	for (ItemStack item : acc.getItems()) {
	        		if (item != null) {
	        			inv.addItem(item);
	        		}
	        	}
	        	plr.openInventory(inv);
	        	invs.put(inv, plr);
	        }
	    }
	}
	
	@EventHandler
	public void onPlayerCloseInventory(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		Player plr = (Player) e.getPlayer();
		if (plr == null || (!invs.containsKey(inv))) {
			return;
		}
		Utils.update(plr, inv);
	}
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent e) {
		Block brokenBlock = e.getBlock();
		ATM chest = ATM.getChest(brokenBlock);
		if (chest != null) {
			if (!e.getPlayer().hasPermission(Utils.pm)) {
				e.getPlayer().sendMessage(ChatColor.RED + "You can not break ATMs.");
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
