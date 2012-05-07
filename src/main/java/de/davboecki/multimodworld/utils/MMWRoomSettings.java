package de.davboecki.multimodworld.utils;

import java.util.HashMap;

import de.davboecki.multimodworld.settings.Hashmapable;

public class MMWRoomSettings implements Hashmapable {

	private MMWLocation normalPortal = new MMWLocation(0,0,0);
	private MMWLocation otherPortal = new MMWLocation(0,0,0);

	public MMWRoomSettings(HashMap<String, Object> map) {
		this.fromHashMap(map);
	}
	
	public MMWRoomSettings(MMWLocation nPortal,MMWLocation oPortal, MMWLocation downcorner, MMWLocation upcorner) {
		
	}
	
	public HashMap<String, Object> toHashMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public void fromHashMap(HashMap<String, Object> map) {
		
	}
}
