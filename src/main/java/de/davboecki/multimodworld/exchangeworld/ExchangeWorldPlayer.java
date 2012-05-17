package de.davboecki.multimodworld.exchangeworld;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.davboecki.multimodworld.exchangeworld.rooms.Room;
import de.davboecki.multimodworld.exchangeworld.rooms.RoomDefault;
import de.davboecki.multimodworld.exchangeworld.rooms.SearchThread;
import de.davboecki.multimodworld.settings.MMWRoomSettings;
import de.davboecki.multimodworld.utils.MMWExchangeWorld;
import de.davboecki.multimodworld.utils.MMWPlayer;
import de.davboecki.multimodworld.utils.MMWPlayer.exchangeworldposition;

public class ExchangeWorldPlayer {

	public enum WorldPos {
		Normal, Mod, Room, LavaWall, Underground, Unknown
	}

	private MMWPlayer mmwplayer;
	public HashMap<String/* Worldname */,MMWRoomSettings> rSettings = new HashMap<String/* Worldname */,MMWRoomSettings>();

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
	

	public void teleport(MMWExchangeWorld world,exchangeworldposition pos) {
		if(mmwplayer.needTeleportcooldown()) return;
		if(pos == exchangeworldposition.normal_RoomPortal || pos == exchangeworldposition.other_RoomPortal) {
			MMWRoomSettings room = this.getRoomSettingsForWorld(world);
			if(room == null) {
				new SearchThread(this.getRoomType(),world,mmwplayer);
				return;
			}
			if(pos == exchangeworldposition.normal_RoomPortal) {
				mmwplayer.teleportWithGateViewCheck(room.getNormalPortal());
				this.setShouldBePos(WorldPos.Room);
			} else {
				mmwplayer.teleportWithGateViewCheck(room.getOtherPortal());
				this.setShouldBePos(WorldPos.Room);
			}
		} else {
			if(pos == exchangeworldposition.normal_Area) {
				mmwplayer.teleportWithGateViewCheck(ExchangeWorldGenerator.getPortalNormal(world.getWorld()));
				this.setShouldBePos(WorldPos.Normal);
			} else {
				mmwplayer.teleportWithGateViewCheck(ExchangeWorldGenerator.getOtherPortal(world.getWorld()));
				this.setShouldBePos(WorldPos.Mod);
			}
		}
		mmwplayer.setWaitTeleportcooldown();
	}
	
	
	private Room getRoomType() {
		//TODO Implement Funtion
		return new RoomDefault();
	}

	public void insertRoomSettings(MMWExchangeWorld world, MMWRoomSettings settings) {
		if(!rSettings.containsKey(world.getWorld().getName())) {
			rSettings.put(world.getWorld().getName(), settings);
		} else {
			throw new UnsupportedOperationException("This should not happen. Please report this to the MMW author.");
		}
	}
	
	public boolean isPlayerinRoom() {
		return rSettings.containsKey(mmwplayer.getPlayer().getWorld().getName()) && rSettings.get(mmwplayer.getPlayer().getWorld().getName()).isPlayerInRoom(mmwplayer);
	}

	public WorldPos getRealPos() {
		final Player player = mmwplayer.getPlayer();
		final Location loc = player.getLocation();
		if (loc.getY() >= 63) {
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
