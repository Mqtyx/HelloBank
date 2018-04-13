package hellobank.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import hellobank.main.Main;

public class BankAccount {
	private UUID uuid;
	private String password;
	private String items;
	
	public BankAccount(UUID uuid, String password, List<ItemStack> items) {
		this.uuid = uuid;
		this.password = password;
		setItems(items);
	}
	
	public BankAccount(UUID uuid, String password) {
		this.uuid = uuid;
		this.password = password;
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

	/*
	 * Generates a list of items in this account
	 * DO NOT USE TO STORE ITEMS! use setItems
	 */
	public List<ItemStack> getItems() {
		if(items == null) return Lists.newArrayList();
		
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
	
	public void setItems(List<ItemStack> items) {
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
	
	public ItemStack createCard() {
		ItemStack card = new ItemStack(Material.PAPER);
		ItemMeta meta = card.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Bank Card");
		meta.setLore(Arrays.asList(
				"Card id: " + uuid.toString(),
				"Holder: " + Bukkit.getOfflinePlayer(uuid).getName()));
		card.setItemMeta(meta);
		return card;
	}
	
	public static boolean isValidCardFormat(ItemStack card) {
		if(card.getType() == Material.PAPER) {
			if(card.hasItemMeta()) {
				ItemMeta meta = card.getItemMeta();
				if(!card.containsEnchantment(Enchantment.DURABILITY)) return false;
				if(!meta.hasLore()) return false;
				if(!meta.hasDisplayName()) return false;
				if(!meta.getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Bank Card")) return false;
				
				return true;
			}
		}
		
		return false;
	}
}
