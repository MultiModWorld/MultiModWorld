package de.davboecki.multimodworld.settings;

import java.io.File;
import java.util.HashMap;

import de.davboecki.multimodworld.MMWWorld;
import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.konstant.FileSystem;

public class MMWWorldSettings extends Settings {
	
	MMWWorld world;
	
	public MMWWorldSettings(MMWWorld world) {
		super(new File(MultiModWorld.getInstance().getDataFolder().getPath() + "/" + FileSystem.WorldSettings + "/" + world.getWorld().getName() + ".yml"));
		this.world = world;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String error(ErrorType et) {
		// TODO Auto-generated method stub
		return "";
	}
}
