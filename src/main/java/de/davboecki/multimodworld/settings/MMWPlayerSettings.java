package de.davboecki.multimodworld.settings;

import java.io.File;
import java.util.HashMap;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.constant.FileSystem;
import de.davboecki.multimodworld.utils.MMWPlayer;

public class MMWPlayerSettings extends Settings {

	private final MMWPlayer mmwplayer;

	public MMWPlayerSettings(MMWPlayer mmwplayer) {
		super(new File(MultiModWorld.getInstance().getDataFolder().getPath() + "/" + FileSystem.PlayerSettings + "/" + mmwplayer.getName() + ".yml"));
		this.mmwplayer = mmwplayer;
	}

	public boolean isNew() {
		return !getFile().exists();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void loadparse(SettingsParser parser) {
		HashMap<String,HashMap<String,Object>> tmp = (HashMap<String, HashMap<String,Object>>) parser.get("RoomSettings");
		HashMap<String,MMWRoomSettings> settings = new HashMap<String,MMWRoomSettings>();
		for(String key:tmp.keySet()) {
			settings.put(key, new MMWRoomSettings(tmp.get(key)));
		}
		mmwplayer.getChestRoomPlayer().rSettings = settings;
	}

	@Override
	protected HashMap<String, Object> saveparse() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("RoomSettings", mmwplayer.getChestRoomPlayer().rSettings);
		return map;
	}

	@Override
	protected String error(ErrorType et) {
		final String Start = "Could not load player information file. ";
		switch(et) {
		case Cast:
			return Start+"File is broken. ("+file.toString()+")";
		case FileNotFound:
			return null;
		case IO:
			return Start+"Could not access file. ("+file.toString()+")";
		}
		return Start+"Internal Error. ("+file.toString()+")";
	}
}
