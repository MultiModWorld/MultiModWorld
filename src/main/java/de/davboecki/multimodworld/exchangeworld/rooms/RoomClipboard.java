package de.davboecki.multimodworld.exchangeworld.rooms;

import java.io.File;
import java.util.HashMap;

import org.bukkit.command.CommandException;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.SchematicFormat;

import de.davboecki.multimodworld.constant.Roomconstants;
import de.davboecki.multimodworld.exchangeworld.settings.ExchangeWorldRoomTypeSettings;
import de.davboecki.multimodworld.settings.SettingsParser;
import de.davboecki.multimodworld.utils.MMWLocation;
import de.davboecki.multimodworld.utils.MMWWorld;

public class RoomClipboard extends Room {
	
	private CuboidClipboard clipboard;
	private MMWLocation normalPortal;
	private MMWLocation otherPortal;
	private MMWLocation normalSign;
	private MMWLocation otherSign;
	private String roomname;
	private ExchangeWorldRoomTypeSettings settings;
	private int RoomPriority;
	
	public RoomClipboard(File schematicfile,File settingsfile, String RoomName) {
		super();
		SchematicFormat format = SchematicFormat.getFormat("mcedit");
		try {
			clipboard = format.load(schematicfile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandException("Could not load file: "+schematicfile.getAbsolutePath());
		}
		settings = new ExchangeWorldRoomTypeSettings(settingsfile);
		this.roomname = RoomName;
		settings.load(this);
	}
	
	public CuboidClipboard getClipboard() {
		return clipboard;
	}
	
	@Override
	public String getName() {
		return roomname;
	}

	@Override
	public MMWLocation getNormalSignPos() {
		return normalSign;
	}

	@Override
	public MMWLocation getOtherSignPos() {
		return otherSign;
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

	@SuppressWarnings("unchecked")
	public void parse(SettingsParser parser) {
		normalSign = new MMWLocation((HashMap<String, Object>) parser.get("nSign"));
		otherSign = new MMWLocation((HashMap<String, Object>) parser.get("oSign"));
		normalPortal = new MMWLocation((HashMap<String, Object>) parser.get("nPortal"));
		otherPortal = new MMWLocation((HashMap<String, Object>) parser.get("oPortal"));
		RoomPriority = Integer.valueOf((Integer) parser.get("RoomPriority"));
	}

	public int getRoomPriority() {
		return RoomPriority;
	}
}
