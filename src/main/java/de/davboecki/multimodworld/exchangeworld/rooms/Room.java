package de.davboecki.multimodworld.exchangeworld.rooms;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.command.CommandException;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;

import de.davboecki.multimodworld.MultiModWorld;

public class Room {
	
	private CuboidClipboard clipboard;
	private EditSession session;
	
	public Room(File file, World world) {
		session = new EditSession(BukkitUtil.getLocalWorld(world), 1000000); //TODO Config
		session.enableQueue();
		SchematicFormat format = SchematicFormat.getFormat("mcedit");
		try {
			clipboard = format.load(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandException("Could not load file: "+file.getAbsolutePath());
		}
	}

	public CuboidClipboard getClipboard() {
		return clipboard;
	}
	
	public void generateAt(int x, int y, int z) {
		if(clipboard != null) {
			try {
				clipboard.paste(session, new Vector(x,y,z), false);
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
				throw new CommandException("Room is too big.");
			}
		}
	}
	
}
