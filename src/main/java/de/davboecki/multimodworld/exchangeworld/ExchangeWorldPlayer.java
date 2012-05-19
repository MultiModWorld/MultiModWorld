package de.davboecki.multimodworld.exchangeworld;

import java.util.ArrayList;
import java.util.HashMap;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.craftbukkit.ItemInformation;
import de.davboecki.multimodworld.exchangeworld.rooms.Room;
import de.davboecki.multimodworld.exchangeworld.rooms.RoomDefault;
import de.davboecki.multimodworld.exchangeworld.rooms.SearchThread;
import de.davboecki.multimodworld.handler.ColorHandler;
import de.davboecki.multimodworld.handler.LanguageHandler;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.settings.MMWRoomSettings;
import de.davboecki.multimodworld.utils.MMWExchangeWorld;
import de.davboecki.multimodworld.utils.MMWPlayer;
import de.davboecki.multimodworld.utils.MMWPlayer.exchangeworldposition;
import de.davboecki.multimodworld.utils.MMWWorld;

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
				if(!hasPlayerModItemsForWorld(world)) {
					mmwplayer.teleportWithGateViewCheck(ExchangeWorldGenerator.getPortalNormal(world.getWorld()));
					this.setShouldBePos(WorldPos.Normal);
				} else {
					mmwplayer.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Not_Allowed_Items.toString());
					messageUnallowedItemsForWorld(world);
				}
			} else {
				mmwplayer.teleportWithGateViewCheck(ExchangeWorldGenerator.getOtherPortal(world.getWorld()));
				this.setShouldBePos(WorldPos.Mod);
			}
		}
		mmwplayer.setWaitTeleportcooldown();
	}
	
	private void messageUnallowedItemsForWorld(MMWWorld mmwworld) {
		for(ItemStack item:mmwplayer.getTotalInventory()) {
			if(item == null) continue;
			ModInfo info = MultiModWorld.getInstance().getModList().getById(item.getTypeId());
			if(info != null) {
				if(mmwworld.getModList().containsKey(info)) {
					if(!mmwworld.getModList().get(info)) {
						mmwplayer.getPlayer().sendMessage(ColorHandler.DARK_BLUE.toString() + new ItemInformation(item.getTypeId()).getName());
					}
				}
			}
		}
	}
	
	public ArrayList<ModInfo> getModsForPlayerInventory() {
		ArrayList<ModInfo> mods = new ArrayList<ModInfo>();
		for(ItemStack item:mmwplayer.getTotalInventory()) {
			if(item == null) continue;
			ModInfo info = MultiModWorld.getInstance().getModList().getById(item.getTypeId());
			if(info != null) {
				mods.add(info);
			}
		}
		return mods;
	}

	public boolean hasPlayerModItems() {
		return getModsForPlayerInventory() != null;
	}

	public boolean hasPlayerModItemsForWorld(MMWWorld mmwworld) {
		ArrayList<ModInfo> mods;
		if((mods = getModsForPlayerInventory()) == null) return false;
		for(ModInfo info:mods) {
			if(mmwworld.getModList().containsKey(info)) {
				if(!mmwworld.getModList().get(info)) {
					return true;
				}
			}
		}
		return false;
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
