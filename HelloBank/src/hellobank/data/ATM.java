package hellobank.data;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class ATM {
	private Block block;
	private Location loc;
	private UUID placer;
	
	public ATM(UUID placer, Block block, Location loc) {
		this.placer = placer;
		this.block = block;
		this.loc = loc;
	}

	public UUID getPlacer() {
		return placer;
	}
	
	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Location getLoc() {
		return loc;
	}
}
