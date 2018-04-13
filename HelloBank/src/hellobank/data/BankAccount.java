package hellobank.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import hellobank.main.Main;

public class BankAccount {
	private UUID uuid;
	private String password;
	private double lastPasswordCheck;
	private String items;
	
	public BankAccount(UUID uuid, String password, List<ItemStack> items) {
		this.uuid = uuid;
		this.password = password;
		this.lastPasswordCheck = 0;
		setItems(items);
	}
	
	public BankAccount(UUID uuid, String password) {
		this.uuid = uuid;
		this.password = password;
		this.lastPasswordCheck = 0;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pin) {
		this.password = pin;
		Main.accountManager.save(this);
	}

	public double getLastPasswordCheck() {
		return lastPasswordCheck;
	}

	public void setLastPasswordCheck(double lastPasswordCheck) {
		this.lastPasswordCheck = lastPasswordCheck;
	}

	/*
	 * Generates a list of items in this account
	 * DO NOT USE TO STORE ITEMS! use setItems
	 */
	public List<ItemStack> getItems()
	{
		if(items == null) return null;
		
		YamlConfiguration config = new YamlConfiguration();
		try
		{
			config.loadFromString(items);
		} catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
		return (List<ItemStack>) config.getList("items");
	}
	public void setItems(List<ItemStack> items)
	{
		if(items == null)
		{
			this.items = null;
		} else {
			YamlConfiguration config = new YamlConfiguration();
			config.set("items", items);
			this.items = config.saveToString();
		}
		
		Main.accountManager.save(this);
	}
}
