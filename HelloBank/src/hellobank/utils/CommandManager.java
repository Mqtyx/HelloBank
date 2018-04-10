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
			if (func.equals("atm")) {
				if (!plr.hasPermission(Utils.pm)) {
					plr.sendMessage(ChatColor.RED + "You don't have access to this command!");
					return false;
				}
				Location loc = plr.getLocation();
				Bukkit.getServer().getWorld(loc.getWorld().getName()).getBlockAt(loc).setType(Material.CHEST);
				Block chest = loc.getWorld().getBlockAt(loc);
				plr.sendMessage(ChatColor.GREEN + "Added an ATM successfully!");
				new ATM(plr.getUniqueId(), chest, loc);
				return true;
			}
			UUID uuid = plr.getUniqueId();
			if (func.equals("addaccount")) {
				if (BankAccount.isAccount(uuid)) {
					plr.sendMessage(ChatColor.RED + "You already have a registered account.");
					return false;
				}
				if (args.length < 2) {
					plr.sendMessage(ChatColor.RED + "Usage: addaccount <acc> <pin>.");
					return false;
				}
				String acc = args[1];
				String pin = args[2];
				if (pin.length() < 6 || acc.length() < 6) {
					plr.sendMessage(ChatColor.RED + "Account/pin mustn't be less than 6 characters.");
					return false;
				}
				if (BankAccount.isTaken(acc)) {
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
				if (!BankAccount.isAccount(uuid)) {
					plr.sendMessage(ChatColor.RED + "You don't have an account to delete.");
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
			Utils.showGuide(plr);
			return false;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
