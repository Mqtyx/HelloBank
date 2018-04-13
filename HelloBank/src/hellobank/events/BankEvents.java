package hellobank.events;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	private static Map<Inventory, Player> invs = Maps.newHashMap();
	public static Map<Player, BankAccount> pendingAccounts = Maps.newHashMap();
	private static Map<Player, ItemStack> pendingCards = Maps.newHashMap();
	
	public BankEvents(Main plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerInteractChest(PlayerInteractEvent e) {
	    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    	if (!Main.atmManager.isAtm(e.getClickedBlock())) {
	    		return;
	    	}
	    	
	    	Player plr = (Player) e.getPlayer();
	    	
	    	if(pendingAccounts.containsKey(plr))
	    	{
	    		plr.sendMessage(ChatColor.RED + "You are already accessing an account.");
	    		e.setCancelled(true);
	    		return;
	    	}
	    	
	    	ItemStack clickedWith = plr.getInventory().getItemInMainHand();
	    	
	    	if(clickedWith != null) {
	    		if(BankAccount.isValidCardFormat(clickedWith)) {
	    			BankAccount acc = Main.accountManager.getAccountFromCard(clickedWith);
	    			
	    			if (acc == null) {
	    				plr.sendMessage(ChatColor.RED + "Invalid card.");
	    				e.setCancelled(true);
	    				return;
	    	    	}
	    			
	    			pendingCards.put(plr, clickedWith.clone());
	    			plr.getInventory().removeItem(clickedWith);
	    			plr.sendMessage(ChatColor.GREEN + "Valid Card! Use /bank pin <pin> to use access items.");
	    			pendingAccounts.put(plr, acc);
	    			e.setCancelled(true);
	    		} else {
	    			plr.sendMessage(ChatColor.RED + "You must have a Bank Card to use an ATM!");
	    			e.setCancelled(true);
	    		}
	    	} else {
	    		plr.sendMessage(ChatColor.RED + "You must have a Bank Card to use an ATM!");
	    		e.setCancelled(true);
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
		returnCard(plr);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Block brokenBlock = e.getBlock();
		ATM atm = Main.atmManager.getAtmByLocation(brokenBlock.getLocation());
		if (atm != null) {
			if (!e.getPlayer().hasPermission(Utils.pm)) {
				e.getPlayer().sendMessage(ChatColor.RED + "You can not break ATMs.");
				e.setCancelled(true);
				return;
			} else {
				Main.atmManager.removeAtm(atm);
			}
		}
	}

	public static void returnCard(Player plr) {
		pendingAccounts.remove(plr);
		plr.getInventory().addItem(pendingCards.get(plr));
		pendingCards.remove(plr);
	}
	
	public static void openAccountInv(Player plr, BankAccount acc) {
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
