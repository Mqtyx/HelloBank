package hellobank.main;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import hellobank.commands.CommandManager;
import hellobank.events.BankEvents;
import hellobank.manager.AtmManager;
import hellobank.manager.BankAccountManager;
import hellobank.utils.Utils;

public class Main extends JavaPlugin {
	public static Main INSTANCE;
	public Logger logger;
	public static FileConfiguration config;
	public static AtmManager atmManager = new AtmManager();
	public static BankAccountManager accountManager = new BankAccountManager();
	
	public void onEnable() {
		INSTANCE = this;
		config = this.getConfig();
		logger = this.getLogger();
		try {
			logger.info(this.getName() + " " + this.getServer().getVersion() + " has been enabled."); // Console outputs enable message to declare if it has been enabled successfully
		} catch(Exception ex) {
			logger.info(this.getName() + " couldn't be enabled");
		}
		registerCommands();
		new BankEvents(this);
		Utils.initGuide();
		
		atmManager.loadAtms();
		accountManager.loadAccounts();
	}
	
	public void onDisable() {
		atmManager.saveAtms();
		accountManager.saveAccounts();
		
		try {
			logger.info(this.getName() + " " +this.getServer().getVersion() + " has been disabled."); // Console outputs disable message to declare if it has been disabled successfully
			saveConfig();
		} catch(Exception ex) {
			logger.info(this.getName() + " couldn't be disabled");
		}
	}
	
	public void registerCommands() {
		getCommand("bank").setExecutor(new CommandManager());
	}
}
