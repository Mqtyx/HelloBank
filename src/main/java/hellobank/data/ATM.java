package hellobank.data;

import java.lang.reflect.Type;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ATM {
	private Location loc;
	private UUID placer;
	
	public ATM(UUID placer, Location loc) {
		this.placer = placer;
		this.loc = loc;
	}

	public UUID getPlacer() {
		return placer;
	}
	
	public Location getLoc() {
		return loc;
	}
	
	public static class ATMSerializer implements JsonSerializer<ATM>, JsonDeserializer<ATM> {

		@Override
		public ATM deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject atmObject = json.getAsJsonObject();
			UUID placer = UUID.fromString(atmObject.get("placer").getAsString());
			
			JsonObject locationObject = atmObject.getAsJsonObject("loc");
			String worldName = locationObject.get("world").getAsString();
			double x = locationObject.get("x").getAsDouble();
			double y = locationObject.get("y").getAsDouble();
			double z = locationObject.get("z").getAsDouble();
			float yaw = locationObject.get("yaw").getAsFloat();
			float pitch = locationObject.get("pitch").getAsFloat();
			
			
			World world = Bukkit.getWorld(worldName);
			if (world == null) {
				throw new IllegalArgumentException("unknown world");
			}
			
			Location loc = new Location(world, x, y, z, yaw, pitch);
			
			return new ATM(placer, loc);
		}

		@Override
		public JsonElement serialize(ATM src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject atmObject = new JsonObject();
			atmObject.addProperty("placer", src.getPlacer().toString());
			
			JsonObject locationObject = new JsonObject();
			locationObject.addProperty("world", src.getLoc().getWorld().getName());
			locationObject.addProperty("x", src.getLoc().getX());
			locationObject.addProperty("y", src.getLoc().getY());
			locationObject.addProperty("z", src.getLoc().getZ());
			locationObject.addProperty("yaw", src.getLoc().getYaw());
			locationObject.addProperty("pitch", src.getLoc().getPitch());
			
			atmObject.add("loc", locationObject);
			return atmObject;
		}
	}
}
