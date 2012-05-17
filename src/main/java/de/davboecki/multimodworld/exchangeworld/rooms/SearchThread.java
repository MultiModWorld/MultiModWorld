package de.davboecki.multimodworld.exchangeworld.rooms;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import de.davboecki.multimodworld.constant.Roomconstants;
import de.davboecki.multimodworld.handler.ColorHandler;
import de.davboecki.multimodworld.handler.LanguageHandler;
import de.davboecki.multimodworld.settings.MMWRoomSettings;
import de.davboecki.multimodworld.utils.MMWExchangeWorld;
import de.davboecki.multimodworld.utils.MMWLocation;
import de.davboecki.multimodworld.utils.MMWPlayer;
import de.davboecki.multimodworld.utils.MMWRegion;

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
	
	private static ArrayList<SearchThread> ThreadList = new ArrayList<SearchThread>();
	private static Object Lock = new Object();
	
	private Room room;
	private MMWExchangeWorld mmwexworld;
	private MMWPlayer mmwplayer;
	
	public SearchThread(Room room, MMWExchangeWorld mmwexworld, MMWPlayer mmwplayer) {
		super("RoomPositionSearchThread");
		this.room = room;
		this.mmwexworld = mmwexworld;
		this.mmwplayer = mmwplayer;
		if(!ThreadList.contains(this) && room != null && mmwexworld != null && mmwplayer != null) {
			ThreadList.add(this);
			super.setName("RoomPositionSearchThread-"+mmwplayer.getName()+"-"+mmwexworld.getWorld().getName());
			super.start();
		}
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof SearchThread)) return false;
		return ((SearchThread)obj).mmwexworld.equals(this.mmwexworld) && ((SearchThread)obj).mmwplayer.equals(this.mmwplayer);
	}
	
	public void run() {
		boolean finished = false;
		if(mmwplayer.getChestRoomPlayer().getRoomSettingsForWorld(mmwexworld) == null) {
			mmwplayer.getPlayer().sendMessage(ColorHandler.BLUE.toString() + LanguageHandler.RoomPos_Search_Start.toString().replace("%w", mmwexworld.getWorld().getName()));
			SearchPos pos = new SearchPos();
			while(!finished) {
				for(int i = 0; i < pos.sidelength && !finished; i++) {
					synchronized (Lock) {
						if(!pos.hasStoneBlock(mmwexworld)) {
							MMWLocation loc1 = new MMWLocation(pos.x, Roomconstants.RoomGroundLayer, pos.z);
							MMWLocation loc2 = new MMWLocation(pos.x + room.getSizeX() - 1, Roomconstants.RoomGroundLayer, pos.z + room.getSizeZ() - 1);
							MMWRegion region = new MMWRegion(loc1, loc2, mmwexworld);
							int[] blocks = region.getContainedBlocks();
							if(blocks.length == 1 && blocks[0] == Material.DIRT.getId()) {
								mmwplayer.getPlayer().sendMessage(ColorHandler.GREEN.toString() + LanguageHandler.RoomPos_Found.toString().replace("%w", mmwexworld.getWorld().getName()));
								MMWLocation roomcorner = new MMWLocation(pos.x,Roomconstants.RoomBottom,pos.z);
								room.generateWithGroundLayerAt(roomcorner, mmwexworld);
								if(room.getNormalSignPos() != null) {
									BlockState sign = roomcorner.moved(room.getNormalSignPos()).getBukkitLocation(mmwexworld).getBlock().getState();
									if(sign instanceof Sign) {
										((Sign)sign).setLine(0, LanguageHandler.RoomSign_Normal_Line1.toString());
										((Sign)sign).setLine(1, LanguageHandler.RoomSign_Normal_Line2.toString());
										((Sign)sign).setLine(2, LanguageHandler.RoomSign_Normal_Line3.toString());
										((Sign)sign).setLine(3, LanguageHandler.RoomSign_Normal_Line4.toString());
										((Sign)sign).update();
									}
								}
								if(room.getOtherSignPos() != null) {
									BlockState sign = roomcorner.moved(room.getOtherSignPos()).getBukkitLocation(mmwexworld).getBlock().getState();
									if(sign instanceof Sign) {
										((Sign)sign).setLine(0, LanguageHandler.RoomSign_Other_Line1.toString());
										((Sign)sign).setLine(1, LanguageHandler.RoomSign_Other_Line2.toString());
										((Sign)sign).setLine(2, LanguageHandler.RoomSign_Other_Line3.toString());
										((Sign)sign).setLine(3, LanguageHandler.RoomSign_Other_Line4.toString());
										((Sign)sign).update();
									}	
								}
								mmwplayer.getChestRoomPlayer().insertRoomSettings(mmwexworld, new MMWRoomSettings(roomcorner.moved(room.getNormalPortal()), roomcorner.moved(room.getOtherPortal()), new MMWRegion(new MMWLocation(pos.x,Roomconstants.RoomBottom,pos.z), new MMWLocation(pos.x + room.getSizeX(), Roomconstants.RoomBottom + room.getSizeY(), pos.z + room.getSizeZ()), mmwexworld)));
								mmwplayer.getPlayer().sendMessage(ColorHandler.GREEN.toString() + LanguageHandler.Room_Generated.toString().replace("%w", mmwexworld.getWorld().getName()));
								finished = true;
								ThreadList.remove(this);
								break;
							} // else continue
						}
					} //synchronized end
					pos.move();
				}
				pos.turn();
			}
		} else {
			throw new UnsupportedOperationException("This should not happen. Please report this to the MMW author.");
		}
	}
}
