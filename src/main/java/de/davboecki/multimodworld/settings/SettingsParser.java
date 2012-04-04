package de.davboecki.multimodworld.settings;

import java.util.HashMap;

public class SettingsParser {
	private final HashMap<String, Object> map;

	SettingsParser(HashMap<String, Object> map) {
		this.map = map;
	}

	public Object get(String key) {
		for (final String lkey : map.keySet()) {
			if (key.equals(lkey)) {
				return map.get(lkey);
			}
		}
		return "";
	}
}
