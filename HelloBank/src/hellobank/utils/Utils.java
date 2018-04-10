package hellobank.utils;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.google.common.collect.Maps;

import hellobank.data.BankAccount;
import hellobank.main.Main;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	public static Permission pm = new Permission("addATM");
	public static Map<String, String> guide = Maps.newHashMap();

	public static void init() {
		// Adding commands to the map
		guide.put("addaccount <acc> <pin>", "Creates HelloBank account");
		guide.put("removeaccount <acc> <pin>", "Deletes your HelloBank account");
	    for (String uuid : Main.INSTANCE.getConfig().getConfigurationSection("Accounts").getKeys(false)) {
	    	String path = "Accounts." + uuid + ".";
	    	String accName = Main.INSTANCE.getConfig().getString(path + "AccountName");
	    	String pin = Main.INSTANCE.getConfig().getString(path + "Pin");
	    	new BankAccount(UUID.fromString(uuid), accName, pin);
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
}
