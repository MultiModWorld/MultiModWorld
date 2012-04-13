package de.davboecki.multimodworld.settings;

import java.io.File;
import java.util.HashMap;

import de.davboecki.multimodworld.MMWPlayer;
import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.constant.FileSystem;

public class MMWPlayerSettings extends Settings {

	private final MMWPlayer mmwplayer;

	public MMWPlayerSettings(MMWPlayer mmwplayer) {
		super(new File(MultiModWorld.getInstance().getDataFolder().getPath() + "/" + FileSystem.PlayerSettings + "/" + mmwplayer.getName() + ".yml"));
		this.mmwplayer = mmwplayer;
	}

	public boolean isNew() {
		return !getFile().exists();
	}

	@Override
	protected void loadparse(SettingsParser parser) {
		// TODO Auto-generated method stub
	}

	@Override
	protected HashMap<String, Object> saveparse() {
		final String pos = mmwplayer.getChestRoomPlayer().getWorldPos().toString();
		System.out.print(pos);
		return null;
	}

	@Override
	protected String error(ErrorType et) {
		// TODO Auto-generated method stub
		return "";
	}
}
