package de.davboecki.multimodworld.settings;

import java.io.File;
import java.util.HashMap;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.constant.FileSystem;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.utils.MMWWorld;

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
	@SuppressWarnings("unchecked")
	protected void loadparse(SettingsParser parser) {
		HashMap<String, Boolean> ModStringList = (HashMap<String,Boolean>) parser.get("ModList");
		for(String Mod:ModStringList.keySet()) {
			boolean done = false;
			for(ModInfo ModInfo:world.getModList().keySet()) {
				if(ModInfo.getName().equalsIgnoreCase(Mod)) {
					done = true;
					world.getModList().remove(ModInfo);
					world.getModList().put(ModInfo, ModStringList.get(Mod));
					break;
				}
			}
			if(!done) {
				if(MultiModWorld.getInstance().getModList().get(Mod) != null) {
					world.getModList().put(MultiModWorld.getInstance().getModList().get(Mod), ModStringList.get(Mod));
				}
			}
		}
		
	}

	@Override
	protected HashMap<String, Object> saveparse() {
		final HashMap<String, Object> answer = new HashMap<String,Object>();
		final HashMap<String, Boolean> ModStringList = new HashMap<String,Boolean>();
		final HashMap<ModInfo,Boolean>ModList = world.getModList();
		for(ModInfo Mod:ModList.keySet()) {
			ModStringList.put(Mod.getName(), ModList.get(Mod));
		}
		answer.put("ModList", ModStringList);
		return answer;
	}

	@Override
	protected String error(ErrorType et) {
		// TODO Auto-generated method stub
		return "";
	}
}
