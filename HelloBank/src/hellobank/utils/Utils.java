package hellobank.utils;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.google.common.collect.Maps;

import hellobank.data.ATM;
import hellobank.data.BankAccount;
import hellobank.main.Main;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	public static Permission pm = new Permission("addRemoveATM");
	public static Map<String, String> guide = Maps.newHashMap();
	public static int count = 0;

	public static void init() {
		// Adding commands to the map
		guide.put("addaccount <acc> <pin>", "Creates HelloBank account");
		guide.put("removeaccount <acc> <pin>", "Deletes your HelloBank account");
		if (Main.INSTANCE.getConfig().getConfigurationSection("Accounts") != null) {
		    for (String uuid : Main.INSTANCE.getConfig().getConfigurationSection("Accounts").getKeys(false)) {
		    	String path = "Accounts." + uuid + ".";
		    	String accName = Main.INSTANCE.getConfig().getString(path + "AccountName");
		    	String pin = Main.INSTANCE.getConfig().getString(path + "Pin");
		    	new BankAccount(UUID.fromString(uuid), accName, pin);
		    }
		}
	    if (Main.INSTANCE.getConfig().getConfigurationSection("ATM") != null) {
		    for (String id : Main.INSTANCE.getConfig().getConfigurationSection("ATM").getKeys(false)) {
		    	String path = "ATM." + id + ".";
		    	double locX = Main.INSTANCE.getConfig().getDouble(path + "X");
		    	double locY = Main.INSTANCE.getConfig().getDouble(path + "Y");
		    	double locZ = Main.INSTANCE.getConfig().getDouble(path + "Z");
		    	String world = Main.INSTANCE.getConfig().getString(path + "World");
		    	String uuid = Main.INSTANCE.getConfig().getString(path + "UUID");
		    	World w = Bukkit.getServer().getWorld(world);
		    	Location loc = new Location(w, locX, locY, locZ);
		    	Block block = loc.getBlock();
		    	if (block != null && block.getState().getType().equals(Material.CHEST)) {
			    	new ATM(UUID.fromString(uuid), block, loc);
			    	Utils.count = Utils.count + 1;
		    	} else {
		    		Main.INSTANCE.getConfig().set("ATM." + id, null);
		    	}
		    }
	    }
	}

	public static void showGuide(Player plr) {
		plr.sendMessage(ChatColor.GOLD + "------------------------------");
		plr.sendMessage(ChatColor.GOLD + "---- Bank Commands Guide ----");
		for (String cmdName : guide.keySet()) {
			plr.sendMessage(ChatColor.AQUA + "/bank " + cmdName + ChatColor.WHITE + " > " + ChatColor.GREEN + guide.get(cmdName));
		}
		plr.sendMessage(ChatColor.GOLD + "------------------------------");
	}

	public static String getIdByBlock(Block brokenBlock) {
		Location blockLoc = brokenBlock.getLocation();
	    if (Main.INSTANCE.getConfig().getConfigurationSection("ATM") != null) {
		    for (String id : Main.INSTANCE.getConfig().getConfigurationSection("ATM").getKeys(false)) {
		    	String path = "ATM." + id + ".";
		    	double locX = Main.INSTANCE.getConfig().getDouble(path + "X");
		    	double locY = Main.INSTANCE.getConfig().getDouble(path + "Y");
		    	double locZ = Main.INSTANCE.getConfig().getDouble(path + "Z");
		    	String world = Main.INSTANCE.getConfig().getString(path + "World");
		    	World w = Bukkit.getServer().getWorld(world);
		    	Location loc = new Location(w, locX, locY, locZ);
		    	if (blockLoc.equals(loc)) {
		    		return id;
		    	}
		    }
	    }
		return null;
	}
}
