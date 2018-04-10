package hellobank.events;

import org.bukkit.event.Listener;

import hellobank.main.Main;

public class BankEvents implements Listener {
	
	public BankEvents(Main plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
}
