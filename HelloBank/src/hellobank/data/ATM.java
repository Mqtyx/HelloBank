package hellobank.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.google.common.collect.Lists;

public class ATM {
	private UUID uuid;
	private Block block;
	private Location loc;
	public static List<ATM> atms = Lists.newArrayList();
	
	public ATM(UUID uniqueId, Block block, Location loc) {
		this.uuid = uniqueId;
		this.block = block;
		this.loc = loc;
		atms.add(this);
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public static boolean isChest(Block block) {
		for (ATM atm : atms) {
			if (atm.getChest().equals(block)) {
				return true;
			}
		}
		return false;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Block getChest() {
		return block;
	}

	public void setChest(Block chest) {
		this.block = chest;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
}
