package de.davboecki.multimodworld.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;

import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.settings.MMWExchangeWorldSettings;
import de.davboecki.multimodworld.settings.MMWRoomSettings;

public class MMWExchangeWorld extends MMWWorld {
	
	public HashMap<ModInfo,Boolean> requiredMods = new HashMap<ModInfo,Boolean>();
	
	protected MMWExchangeWorld(World world) {
		super(world);
		settings = new MMWExchangeWorldSettings(this);
	}
	
	public MMWRoomSettings getRoomSettingsForLocation(Location loc) {
		for(MMWPlayer player:MMWPlayer.getAllMMWPlayers()) {
			if(player.getChestRoomPlayer().rSettings.get(loc.getWorld().getName()).isLocationInRoom(new MMWLocation(loc))) {
				return player.getChestRoomPlayer().rSettings.get(loc.getWorld().getName());
			}
		}
		return null;
	}
	
	public ArrayList<ModInfo> getRequiredMods() {
		ArrayList<ModInfo> mods = new ArrayList<ModInfo>();
		for(ModInfo info:requiredMods.keySet()) {
			if(requiredMods.get(info)) {
				mods.add(info);
			}
		}
		return mods;
	}
}
