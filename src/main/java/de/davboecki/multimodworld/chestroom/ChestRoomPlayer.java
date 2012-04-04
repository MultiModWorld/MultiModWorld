package de.davboecki.multimodworld.chestroom;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.davboecki.multimodworld.MMWPlayer;

public class ChestRoomPlayer {

	MMWPlayer mmwplayer;

	public ChestRoomPlayer(MMWPlayer mmwplayer) {
		this.mmwplayer = mmwplayer;
	}

	private WorldPos wPos = WorldPos.Normal;

	public WorldPos getWorldPos() {
		return wPos;
	}

	public void setWorldPos(WorldPos wPos) {
		this.wPos = wPos;
	}

	public enum WorldPos {
		Normal, Mod, Room, LavaWall, Underground, Unknown
	}

	public boolean isPlayerinRoom() {
		// TODO
		return false;
	}

	public WorldPos getPlayerPos() {
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
