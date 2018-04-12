package hellobank.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hellobank.data.ATM;
import hellobank.data.BankAccount;
import net.md_5.bungee.api.ChatColor;
import hellobank.main.Main;

public class CommandManager implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			Player plr = (Player) sender;
			if (args.length == 0) {
				Utils.showGuide(plr);
				return true;
			}
			String func = args[0];
			UUID uuid = plr.getUniqueId();
			if (func.equals("atm")) {
				if (!plr.hasPermission(Utils.pm)) {
					plr.sendMessage(ChatColor.RED + "You don't have access to this command!");
					return false;
				}
				Location loc = plr.getLocation();
				Bukkit.getServer().getWorld(loc.getWorld().getName()).getBlockAt(loc).setType(Material.CHEST);
				Block chest = loc.getWorld().getBlockAt(loc);
				plr.sendMessage(ChatColor.GREEN + "Added an ATM successfully!");
				new ATM(uuid, chest, loc);
				Utils.count = Utils.count + 1;
				Main.INSTANCE.getConfig().set("ATM." + Utils.count + ".X", loc.getBlockX());
				Main.INSTANCE.getConfig().set("ATM." + Utils.count + ".Y", loc.getBlockY());
				Main.INSTANCE.getConfig().set("ATM." + Utils.count + ".Z", loc.getBlockZ());
				Main.INSTANCE.getConfig().set("ATM." + Utils.count + ".World", loc.getWorld().getName());
				Main.INSTANCE.getConfig().set("ATM." + Utils.count + ".UUID", uuid.toString());
				return true;
			}
			if (func.equals("addaccount")) {
				if (BankAccount.getAccountFromUUID(uuid) != null) {
					plr.sendMessage(ChatColor.RED + "You already have a registered account.");
					return false;
				}
				if (args.length < 3) {
					plr.sendMessage(ChatColor.RED + "Usage: /bank addaccount <acc> <pin>.");
					return false;
				}
				String acc = args[1];
				String pin = args[2];
				if (acc.length() < 6 || acc.length() > 20) {
					plr.sendMessage(ChatColor.RED + "Account must be between 6 and 20 characters.");
					return false;
				}
				if (pin.length() < 4 || pin.length() > 12) {
					plr.sendMessage(ChatColor.RED + "Pin must be between 4 and 12 characters.");
					return false;
				}
				if (BankAccount.isAccountTaken(acc)) {
					plr.sendMessage(ChatColor.RED + "" + acc + " is already taken.");
					return false;
				}
				new BankAccount(uuid, acc, pin);
				plr.sendMessage(ChatColor.GREEN + "Successfully created '" + acc + "' account with '" + pin + "' pin!");
				Main.INSTANCE.getConfig().set("Accounts." + uuid.toString() + ".Account", acc);
				Main.INSTANCE.getConfig().set("Accounts." + uuid.toString() + ".Pin", pin);
				return true;
			}
			if (func.equals("deleteaccount") || func.equals("removeaccount")) {
				BankAccount acc = BankAccount.getAccountFromUUID(uuid);
				if (acc == null) {
					plr.sendMessage(ChatColor.RED + "You don't have an account to delete.");
					return false;
				}
				if (args.length < 3) {
					plr.sendMessage(ChatColor.RED + "Usage: /bank " + func + " <acc> <pin>.");
					return false;
				}
				String accName = args[1];
				String pass = args[2];
				if (!acc.getAccount().equals(accName) || (!acc.getPassword().equals(pass))) {
					plr.sendMessage(ChatColor.RED + "Invalid credentials.");
					return false;
				}
				boolean success = BankAccount.deleteAccount(uuid);
				if (success) {
					Main.INSTANCE.getConfig().set("Accounts." + uuid.toString(), null);
					plr.sendMessage(ChatColor.GREEN + "Successfully deleted your account!");
				} else {
					plr.sendMessage(ChatColor.RED + "An error has occurred while deleting your account.");
				}
				return true;
			}
			if (func.equals("pin")) {
				BankAccount acc = BankAccount.getAccountFromUUID(uuid);
				if (acc == null) {
					plr.sendMessage(ChatColor.RED + "You don't have an account.");
					return false;
				}
				if (args.length < 2) {
					plr.sendMessage(ChatColor.RED + "Usage: /bank pin <password>.");
					return false;
				}
				String enteredPassword = args[1];
				if (!acc.getPassword().equals(enteredPassword)) {
					plr.sendMessage(ChatColor.RED + "Wrong password.");
					return false;
				}
				acc.setLastPasswordCheck(System.currentTimeMillis());
				plr.sendMessage(ChatColor.GREEN + "Successfully entered your account for 30 seconds!");
				return true;
			}
			if (func.equals("changepin")) {
				BankAccount acc = BankAccount.getAccountFromUUID(uuid);
				if (acc == null) {
					plr.sendMessage(ChatColor.RED + "You don't have an account.");
					return false;
				}
				if (args.length < 3) {
					plr.sendMessage(ChatColor.RED + "Usage: /bank changepin <oldPin> <newPin>.");
					return false;
				}
				String oldPass = args[1];
				String newPass = args[2];
				if (!acc.getPassword().equals(oldPass)) {
					plr.sendMessage(ChatColor.RED + "Wrong password.");
					return false;
				}
				if (newPass.length() < 4 || newPass.length() > 12) {
					plr.sendMessage(ChatColor.RED + "Pin must be between 4 and 12 characters.");
					return false;
				}
				acc.setPassword(newPass);
				plr.sendMessage(ChatColor.GREEN + "Successfully changed your password from '" + oldPass + "' to '" + newPass + "'!");
				return true;
			}
			Utils.showGuide(plr);
			return false;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
