package de.davboecki.multimodworld.exchangeworld.rooms;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.World;
import org.bukkit.entity.Player;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.constant.FileSystem;

public class RoomManager {
	private RoomClipboard[] roomlist = new RoomClipboard[0];

	public void LoadRooms() {
		ArrayList<RoomClipboard> tmp = new ArrayList<RoomClipboard>();
		File roomfolder = new File(MultiModWorld.getInstance().getDataFolder().getPath() + "/" + FileSystem.RoomsFolder + "/");
		roomfolder.mkdirs();
		String[] rooms = null;
		if((rooms = roomfolder.list()) == null) return;
		for(String roomfilename:rooms) {
			if(!roomfilename.endsWith(".room")) continue;
			File roominfo = new File(roomfolder.getAbsolutePath() + "/" + roomfilename);
			String roomname = roomfilename.substring(0, roomfilename.indexOf("."));
			File roomstructure = new File(roomfolder.getAbsolutePath() + "/" + roomname + ".schematic");
			tmp.add(new RoomClipboard(roomstructure,roominfo,roomname));
		}
		roomlist = tmp.toArray(new RoomClipboard[0]);
		Arrays.sort(roomlist, new Comparator<RoomClipboard>() {
			public int compare(RoomClipboard o1, RoomClipboard o2) {
				if(o1.getRoomPriority() < o2.getRoomPriority()) {
					return -1;
				} else if(o1.getRoomPriority() > o2.getRoomPriority()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}
	
	public Room getRoom(Player player, World world) {
		for(RoomClipboard room:roomlist) {
			if(player.hasPermission("multimodworld.room."+world.getName()+"."+room.getName())) {
				return room;
			}
		}
		for(RoomClipboard room:roomlist) {
			if(player.hasPermission("multimodworld.room."+room.getName())) {
				return room;
			}
		}
		return new RoomDefault();
	}
}
