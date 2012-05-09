package de.davboecki.multimodworld.exchangeworld.rooms;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import de.davboecki.multimodworld.constant.Roomconstants;
import de.davboecki.multimodworld.handler.ColorHandler;
import de.davboecki.multimodworld.handler.LanguageHandler;
import de.davboecki.multimodworld.utils.MMWExchangeWorld;
import de.davboecki.multimodworld.utils.MMWLocation;
import de.davboecki.multimodworld.utils.MMWPlayer;
import de.davboecki.multimodworld.utils.MMWRegion;
import de.davboecki.multimodworld.utils.MMWRoomSettings;

public class SearchThread extends Thread {
	
	private enum Direction {
		up, left, down, right;
		
		public Direction next() {
			switch (this) {
			case up:    return left;
			case left:  return down;
			case down:  return right;
			case right: return up;
			}
			return null;
		}
	}
	
	private class SearchPos {
		int x = 0;
		int z = 0;
		int sidelength = 1;
		Direction dir = Direction.up;
		
		public void turn() {
			dir = dir.next();
			if(dir == Direction.up || dir == Direction.down) {
				sidelength++;
			}
		}
		
		public boolean hasStoneBlock(MMWExchangeWorld world) {
			return world.getWorld().getBlockAt(x, Roomconstants.RoomGroundLayer , z) != null && world.getWorld().getBlockAt(x, Roomconstants.RoomGroundLayer , z).getTypeId() == Material.STONE.getId();
		}
		
		public void move() {
			switch (dir) {
			case up:
				x++;
				break;
			case left:
				z--;
				break;
			case down:
				x--;
				break;
			case right:
				z++;
				break;
			}
		}
		
		public String toString() {
			return "x: "+x+"z: "+z;
		}
	}
	
	private Room room;
	private MMWExchangeWorld mmwexworld;
	private MMWPlayer mmwplayer;
	
	SearchThread(Room room, MMWExchangeWorld mmwexworld, MMWPlayer mmwplayer) {
		this.room = room;
		this.mmwexworld = mmwexworld;
		this.mmwplayer = mmwplayer;
		this.start();
	}
	
	public void run() {
		if(mmwplayer.getChestRoomPlayer().getRoomSettingsForWorld(mmwexworld) == null) {
			SearchPos pos = new SearchPos();
			while(true) {
				for(int i = 0; i < pos.sidelength; i++) {
					if(!pos.hasStoneBlock(mmwexworld)) {
						MMWLocation loc1 = new MMWLocation(pos.x, Roomconstants.RoomGroundLayer, pos.z);
						MMWLocation loc2 = new MMWLocation(pos.x + room.getSizeX(), Roomconstants.RoomGroundLayer, pos.z + room.getSizeZ());
						MMWRegion region = new MMWRegion(loc1, loc2, mmwexworld);
						int[] blocks = region.getContainedBlocks();
						if(blocks.length == 1 && blocks[0] == Material.DIRT.getId()) {
							MMWLocation roomcorner = new MMWLocation(pos.x,Roomconstants.RoomBottom,pos.z);
							room.generateWithGroundLayerAt(roomcorner, mmwexworld);
							if(room.getNormalSignPos() != null) {
								BlockState sign = room.getNormalSignPos().getBukkitLocation(mmwexworld).getBlock().getState();
								if(sign instanceof Sign) {
									((Sign)sign).setLine(0, LanguageHandler.RoomSign_Normal_Line1.toString());
									((Sign)sign).setLine(1, LanguageHandler.RoomSign_Normal_Line2.toString());
									((Sign)sign).setLine(2, LanguageHandler.RoomSign_Normal_Line3.toString());
									((Sign)sign).setLine(3, LanguageHandler.RoomSign_Normal_Line4.toString());
								}
							}
							if(room.getOtherSignPos() != null) {
								BlockState sign = room.getOtherSignPos().getBukkitLocation(mmwexworld).getBlock().getState();
								if(sign instanceof Sign) {
									((Sign)sign).setLine(0, LanguageHandler.RoomSign_Other_Line1.toString());
									((Sign)sign).setLine(1, LanguageHandler.RoomSign_Other_Line2.toString());
									((Sign)sign).setLine(2, LanguageHandler.RoomSign_Other_Line3.toString());
									((Sign)sign).setLine(3, LanguageHandler.RoomSign_Other_Line4.toString());
								}	
							}
							mmwplayer.getChestRoomPlayer().insertRoomSettings(mmwexworld, new MMWRoomSettings(roomcorner.moved(room.getNormalPortal()), roomcorner.moved(room.getOtherPortal()), new MMWRegion(new MMWLocation(pos.x,Roomconstants.RoomBottom,pos.z), new MMWLocation(pos.x + room.getSizeX(), Roomconstants.RoomBottom + room.getSizeY(), pos.z + room.getSizeZ()), mmwexworld)));
							mmwplayer.getPlayer().sendMessage(ColorHandler.GREEN.toString() + LanguageHandler.Room_Generated.toString());
							break;
						} // else continue
					}
					pos.move();
				}
				pos.turn();
			}
		} else {
			throw new UnsupportedOperationException("This should not happen. Please report this to the MMW author.");
		}
	}
}
