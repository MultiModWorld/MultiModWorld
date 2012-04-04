package de.davboecki.multimodworld.settings;

import java.util.HashMap;

public interface Hashmapable {

	public abstract HashMap<String, Object> toHashMap();

	public abstract void fromHashMap(HashMap<String, Object> map);

}
