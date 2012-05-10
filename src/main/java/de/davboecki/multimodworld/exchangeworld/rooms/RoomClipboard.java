package de.davboecki.multimodworld.exchangeworld.rooms;

import java.io.File;

import org.bukkit.command.CommandException;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.SchematicFormat;

import de.davboecki.multimodworld.constant.Roomconstants;
import de.davboecki.multimodworld.utils.MMWLocation;
import de.davboecki.multimodworld.utils.MMWWorld;

public class RoomClipboard extends Room {
	
	private CuboidClipboard clipboard;
	private MMWLocation normalPortal;
	private MMWLocation otherPortal;
	
	public RoomClipboard(File file) {
		super();
		SchematicFormat format = SchematicFormat.getFormat("mcedit");
		try {
			clipboard = format.load(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandException("Could not load file: "+file.getAbsolutePath());
		}
		// TODO SettingsFile
		
	}
	
	public CuboidClipboard getClipboard() {
		return clipboard;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MMWLocation getNormalSignPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MMWLocation getOtherSignPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MMWLocation getNormalPortal() {
		return normalPortal;
	}

	@Override
	public MMWLocation getOtherPortal() {
		return otherPortal;
	}

	@Override
	public int getSizeX() {
		return clipboard.getWidth();
	}

	@Override
	public int getSizeY() {
		return clipboard.getHeight();
	}

	@Override
	public int getSizeZ() {
		return clipboard.getLength();
	}

	@Override
	protected void generateAt(int x, int z, MMWWorld world) {
		if(clipboard != null) {
			try {
				EditSession session = new EditSession(BukkitUtil.getLocalWorld(world.getWorld()), 1000000);
				session.enableQueue();
				clipboard.paste(session, new Vector(x,Roomconstants.RoomBottom,z), false);
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
				throw new CommandException("Room is too big.");
			}
		}
	}
}
