package hellobank.main;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import hellobank.events.BankEvents;
import hellobank.utils.CommandManager;

public class Main extends JavaPlugin {
	public static Main INSTANCE;
	public Logger logger;
	public static FileConfiguration config;
	
	public void onEnable()
	{
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
	}
	
	public void onDisable()
	{
		try {
			logger.info(this.getName() + " " +this.getServer().getVersion() + " has been disabled."); // Console outputs disable message to declare if it has been disabled successfully
			saveConfig();
		} catch(Exception ex) {
			logger.info(this.getName() + " couldn't be disabled");
		}
	}
	
	public void registerCommands()
	{
		// getCommand("ts").setExecutor(new CommandManager());
	}
}
