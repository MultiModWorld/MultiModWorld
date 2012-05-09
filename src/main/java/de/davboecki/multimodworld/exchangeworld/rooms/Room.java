package de.davboecki.multimodworld.exchangeworld.rooms;

import java.util.HashMap;

import de.davboecki.multimodworld.constant.Roomconstants;
import de.davboecki.multimodworld.utils.MMWLocation;
import de.davboecki.multimodworld.utils.MMWWorld;

public abstract class Room {
	
	private static HashMap<String,Room> roomlist = new HashMap<String,Room>();
	
	protected Room() {
		roomlist.put(this.getName(), this);
	}
	
	public static Room getRoom(String name) {
		return roomlist.get(name);
	}
	
	public abstract String getName();

	public abstract MMWLocation getNormalPortal();
	public abstract MMWLocation getOtherPortal();
	
	public abstract MMWLocation getNormalSignPos();
	public abstract MMWLocation getOtherSignPos();

	public abstract int getSizeX();
	public abstract int getSizeY();
	public abstract int getSizeZ();
	
	protected abstract void generateAt(int x, int z, MMWWorld world);
	
	public void generateWithGroundLayerAt(MMWLocation corner, MMWWorld world) {
		int x = corner.getIntX();
		int z = corner.getIntZ();
		generateAt(x, z, world);
		for(int ix = x;ix < x + getSizeX();ix++) {
			for(int iz = z;iz < z + getSizeZ();iz++) {
				world.getWorld().getBlockAt(ix, Roomconstants.RoomGroundLayer, iz).setTypeId(1);
			}
		}
	}
}
