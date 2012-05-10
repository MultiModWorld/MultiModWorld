package de.davboecki.multimodworld.mmwevents;

import de.davboecki.multimodworld.utils.MMWWorld;

public class IDHandler {

	public boolean isIdAllowed(String worldname, int id) {
		MMWWorld mmwworld = MMWWorld.getMMWWorld(worldname);
		if(mmwworld != null) {
			return mmwworld.isIdAllowed(id);
		}
		return false;
	}

}
