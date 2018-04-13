package hellobank.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;

import hellobank.data.ATM;
import hellobank.main.Main;
import hellobank.utils.Utils;

public class AtmManager
{
	public List<ATM> atms = Lists.newArrayList();
	private Type atmListType = new TypeToken<List<ATM>>(){}.getType();
	
	public void loadAtms() {
		File atmsFile = new File(Main.INSTANCE.getDataFolder(), "atms.json");
		if(atmsFile.exists())
		{
			try(Reader reader = new FileReader(atmsFile)) {
				atms = Utils.GSON.fromJson(reader, atmListType);
				if(atms == null) {
					atms = Lists.newArrayList();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveAtms() {
		File atmsFile = new File(Main.INSTANCE.getDataFolder(), "atms.json");
		if(!atmsFile.exists()) {
			try {
				atmsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!atms.isEmpty()) {
			try (Writer writer = new FileWriter(atmsFile)) {
				Utils.GSON.toJson(atms, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addAtm(ATM atm) {
		atms.add(atm);
		saveAtms();
	}
	
	public ATM getAtmByLocation(Location location) {
		for(ATM atm : atms) {
			if(atm.getLoc().equals(location)) {
				return atm;
			}
		}
		return null;
	}

	public void removeAtm(ATM atm) {
		atms.remove(atm);
		saveAtms();
	}

	public boolean isAtm(Location location) {
		return getAtmByLocation(location) != null;
	}
	
	public boolean isAtm(Block block) {
		return isAtm(block.getLocation());
	}
}
