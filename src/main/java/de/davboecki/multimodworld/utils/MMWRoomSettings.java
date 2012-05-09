package de.davboecki.multimodworld.utils;

import java.util.HashMap;

import de.davboecki.multimodworld.settings.Hashmapable;

public class MMWRoomSettings implements Hashmapable {

	private MMWLocation normalPortal;
	private MMWLocation otherPortal;
	private MMWRegion roomregion;

	public MMWRoomSettings(HashMap<String, Object> map) {
		this.fromHashMap(map);
	}
	
	public MMWRoomSettings(MMWLocation nPortal,MMWLocation oPortal, MMWRegion region) {
		normalPortal = nPortal;
		otherPortal = oPortal;
		roomregion = region;
	}
	
	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("normalPortal", normalPortal);
		map.put("otherPortal", otherPortal);
		map.put("roomregion", roomregion);
		return map;
	}

	@SuppressWarnings("unchecked")
	public void fromHashMap(HashMap<String, Object> map) {
		normalPortal = new MMWLocation((HashMap<String, Object>)map.get("normalPortal"));
		otherPortal = new MMWLocation((HashMap<String, Object>)map.get("otherPortal"));
		roomregion = new MMWRegion((HashMap<String, Object>)map.get("roomregion"));
	}
}
