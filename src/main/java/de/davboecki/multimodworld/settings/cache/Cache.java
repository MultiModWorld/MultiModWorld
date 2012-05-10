package de.davboecki.multimodworld.settings.cache;

import java.util.ArrayList;

public abstract class Cache {

	private ArrayList<Cache> cachelist = new ArrayList<Cache>();
	
	Cache() {
		cachelist.add(this);
	}
	
	public abstract void reset();
	
	public void resetAll() {
		for(Cache cache:cachelist) {
			cache.reset();
		}
	}
}
