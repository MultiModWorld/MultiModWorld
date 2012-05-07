package de.davboecki.multimodworld.exchangeworld;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.davboecki.multimodworld.utils.MMWExchangeWorld;
import de.davboecki.multimodworld.utils.MMWPlayer;
import de.davboecki.multimodworld.utils.MMWRoomSettings;

public class ExchangeWorldPlayer {

	public enum WorldPos {
		Normal, Mod, Room, LavaWall, Underground, Unknown
	}

	MMWPlayer mmwplayer;
	HashMap<String/* Worldname */,MMWRoomSettings> rSettings;

	public ExchangeWorldPlayer(MMWPlayer mmwplayer) {
		this.mmwplayer = mmwplayer;
	}

	private WorldPos wPos = WorldPos.Normal;

	public WorldPos getShouldBePos() {
		return wPos;
	}

	public void setShouldBePos(WorldPos wPos) {
		this.wPos = wPos;
	}

	public MMWRoomSettings getRoomSettingsForWorld(MMWExchangeWorld world) {
		return rSettings.get(world.getWorld().getName());
	}
	
	public boolean isPlayerinRoom() {
		// TODO
		return false;
	}

	public WorldPos getRealPos() {
		final Player player = mmwplayer.getPlayer();
		final Location loc = player.getLocation();
		if (loc.getY() > 63) {
			if (loc.getX() > 2) {
				return WorldPos.Normal;
			} else if (loc.getX() < -1) {
				return WorldPos.Mod;
			} else {
				return WorldPos.LavaWall;
			}
		} else if (isPlayerinRoom()) {
			return WorldPos.Room;
		} else {
			return WorldPos.Underground;
		}
	}

}
