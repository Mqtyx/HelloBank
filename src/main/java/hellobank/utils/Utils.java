package hellobank.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hellobank.data.ATM;
import hellobank.data.BankAccount;
import hellobank.main.Main;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	public static Permission pm = new Permission("addRemoveATM");
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
			.registerTypeAdapter(ATM.class, new ATM.ATMSerializer())
			.create();
	public static Map<String, String> guide = Maps.newHashMap();
	
	public static String getHelloCraftPrefix()
	{
		return ChatColor.AQUA + "[HelloWorld] " + ChatColor.RESET;
	}

	public static void initGuide() {
		// Adding commands to the map
		guide.put("addaccount <pin>", "Creates HelloBank account");
		guide.put("removeaccount <pin>", "Deletes your HelloBank account");
		guide.put("pin <pin>",  "To use ATMs for the next 30 seconds.");
		guide.put("changepin <oldPin> <newPin>", "Changes your pin to a new pin you choose.");
		/*if (Main.INSTANCE.getConfig().getConfigurationSection("Accounts") != null) {
		    for (String uuid : Main.INSTANCE.getConfig().getConfigurationSection("Accounts").getKeys(false)) {
		    	String path = "Accounts." + uuid + ".";
		    	String accName = Main.INSTANCE.getConfig().getString(path + "Account");
		    	String pin = Main.INSTANCE.getConfig().getString(path + "Pin");
		    	List<ItemStack> items = Lists.newArrayList();
		    	if (Main.INSTANCE.getConfig().getConfigurationSection(path + "Items") != null) {
			    	for (String c : Main.INSTANCE.getConfig().getConfigurationSection(path + "Items").getKeys(false)) {
			    		Object p = Main.INSTANCE.getConfig().get("Accounts." + uuid + ".Items." + c);
			    		if (p != null && (ItemStack) p != null) {
			    			items.add((ItemStack) p);
			    		}
			    	}
		    	}
		    	new BankAccount(UUID.fromString(uuid), accName, pin, items);
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
	    }*/
	}

	public static void showGuide(Player plr) {
		plr.sendMessage(ChatColor.GOLD + "------------------------------");
		plr.sendMessage(ChatColor.GOLD + "---- Bank Commands Guide ----");
		for (String cmdName : guide.keySet()) {
			plr.sendMessage(ChatColor.AQUA + "/bank " + cmdName + ChatColor.WHITE + " > " + ChatColor.GREEN + guide.get(cmdName));
		}
		plr.sendMessage(ChatColor.GOLD + "------------------------------");
	}

	/*public static String getIdByBlock(Block brokenBlock) {
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
	}*/

	public static void update(Player plr, Inventory inv) {
		BankAccount acc = Main.accountManager.getAccountFromUUID(plr.getUniqueId());
		acc.setItems(Arrays.asList(inv.getContents()));
	}
	
	public static void debug(String msg) {
		for (Player plr : Bukkit.getOnlinePlayers()) {
			plr.sendMessage(msg);
		}
	}
	
	public static String hash(String toHash, String algorithm) {
		try
		{
			MessageDigest md = MessageDigest.getInstance(algorithm);
		    md.update(toHash.getBytes(StandardCharsets.UTF_8));
		    byte[] digest = md.digest();

		    String hex = String.format("%064x", new BigInteger(1, digest));
		    return hex;
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return toHash;
	}
}
