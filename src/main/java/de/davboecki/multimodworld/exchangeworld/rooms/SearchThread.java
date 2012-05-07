package de.davboecki.multimodworld.exchangeworld.rooms;

import org.bukkit.Material;

import de.davboecki.multimodworld.constant.Roomconstants;
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
						MMWLocation loc2 = new MMWLocation(pos.x + room.getClipboard().getWidth(), Roomconstants.RoomGroundLayer + room.getClipboard().getHeight(), pos.z + room.getClipboard().getLength());
						MMWRegion region = new MMWRegion(loc1, loc2, mmwexworld);
						int[] blocks = region.getContainedBlocks();
						if(blocks.length == 1 && blocks[0] == Material.DIRT.getId()) {
							//TODO Found Room
							mmwplayer.getPlayer().sendMessage("Found free space at:"+pos);
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
