package de.davboecki.multimodworld.utils;

import org.bukkit.Location;
import org.bukkit.World;

import de.davboecki.multimodworld.settings.MMWExchangeWorldSettings;
import de.davboecki.multimodworld.settings.MMWRoomSettings;

public class MMWExchangeWorld extends MMWWorld {
	
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
}
