package hellobank.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import hellobank.main.Main;

public class BankAccount {
	private UUID uuid;
	private String acc;
	private String password;
	private double lastPasswordCheck;
	private List<ItemStack> items = Lists.newArrayList();
	public static List<BankAccount> accounts = Lists.newArrayList();
	
	public BankAccount(UUID uuid, String acc, String password, List<ItemStack> items) {
		this.uuid = uuid;
		this.acc = acc;
		this.password = password;
		this.lastPasswordCheck = 0;
		this.items = items;
		accounts.add(this);
	}
	
	public BankAccount(UUID uuid, String acc, String password) {
		this.uuid = uuid;
		this.acc = acc;
		this.password = password;
		this.lastPasswordCheck = 0;
		accounts.add(this);
	}
	
	public static BankAccount getAccountFromUUID(UUID uniqueId) {
		for (BankAccount e : accounts) {
			if (e.getUUID().equals(uniqueId)) {
				return e;
			}
		}
		return null;
	}
	
	public static boolean deleteAccount(UUID uniqueId) {
		for (BankAccount e : accounts) {
			if (e.getUUID().equals(uniqueId)) {
				accounts.remove(e);
				return true;
			}
		}
		return false;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUId(UUID uuid) {
		this.uuid = uuid;
	}

	public String getAccount() {
		return this.acc;
	}

	public void setAccount(String acc) {
		this.acc = acc;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pin) {
		this.password = pin;
		Main.INSTANCE.getConfig().set("Accounts." + this.uuid.toString() + ".Pin", pin);
	}

	public static boolean isAccountTaken(String acc) {
		for (BankAccount e : accounts) {
			if (e.getAccount().equals(acc)) {
				return true;
			}
		}
		return false;
	}

	public double getLastPasswordCheck() {
		return lastPasswordCheck;
	}

	public void setLastPasswordCheck(double lastPasswordCheck) {
		this.lastPasswordCheck = lastPasswordCheck;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> itemStacks) {
		this.items = itemStacks;
	}
}
