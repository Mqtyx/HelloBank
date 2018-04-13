/*
 * 
 * Class LocationAdapter from HosPluginLib.
 * Do not use without permission.
 * 
 * @author Hossam Mohsen (7osCraft - HosCraft)
 * 
 */
package hoscraft.pluginlib.adapters;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LocationAdapter extends TypeAdapter<Location>
{

	@Override
	public Location read(JsonReader reader) throws IOException
	{
		reader.beginObject();
		String fieldname = null;

		String worldName = null;
		double x = 0, y = 0, z = 0;
		float yaw = 0, pitch = 0;
		while (reader.hasNext())
		{
			JsonToken token = reader.peek();

			if (token.equals(JsonToken.NAME))
			{
				fieldname = reader.nextName();
			}
			if ("world".equals(fieldname))
			{
				token = reader.peek();
				worldName = reader.nextString();
			}
			if ("x".equals(fieldname))
			{
				token = reader.peek();
				x = reader.nextDouble();
			}
			if ("y".equals(fieldname))
			{
				token = reader.peek();
				y = reader.nextDouble();
			}
			if ("z".equals(fieldname))
			{
				token = reader.peek();
				z = reader.nextDouble();
			}
			if ("yaw".equals(fieldname))
			{
				token = reader.peek();
				yaw = (float) reader.nextDouble();
			}
			if ("pitch".equals(fieldname))
			{
				token = reader.peek();
				pitch = (float) reader.nextDouble();
			}
		}
		reader.endObject();
		
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			throw new IllegalArgumentException("unknown world");
		}
		
		return new Location(world, x, y, z, yaw, pitch);
	}

	@Override
	public void write(JsonWriter writer, Location loc) throws IOException
	{
		if(loc == null)
		{
			writer.nullValue();
			return;
		}
		writer.beginObject();
		writer.name("world");
		writer.value(loc.getWorld().getName());
		writer.name("x");
		writer.value(loc.getX());
		writer.name("y");
		writer.value(loc.getY());
		writer.name("z");
		writer.value(loc.getZ());
		writer.name("yaw");
		writer.value(loc.getYaw());
		writer.name("pitch");
		writer.value(loc.getPitch());
		writer.endObject();
	}

}
