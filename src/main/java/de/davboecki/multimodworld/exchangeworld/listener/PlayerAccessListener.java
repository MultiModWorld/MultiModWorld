package de.davboecki.multimodworld.exchangeworld.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.constant.Roomconstants;
import de.davboecki.multimodworld.handler.ColorHandler;
import de.davboecki.multimodworld.handler.LanguageHandler;
import de.davboecki.multimodworld.handler.Permission;
import de.davboecki.multimodworld.settings.MMWRoomSettings;
import de.davboecki.multimodworld.utils.MMWExchangeWorld;
import de.davboecki.multimodworld.utils.MMWLocation;
import de.davboecki.multimodworld.utils.MMWPlayer;
import de.davboecki.multimodworld.utils.MMWWorld;

public class PlayerAccessListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void interact(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) return;
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getClickedBlock().getWorld())) {
			MMWPlayer mmwplayer = MMWPlayer.getMMWPlayer(event.getPlayer());
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getTypeId() != 0) {
				if(mmwplayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld) MMWWorld.getMMWWorld(event.getPlayer().getWorld())).isLocationInRoom(new MMWLocation(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())) && !event.getClickedBlock().getType().equals(Material.CHEST)) {
					return;
				}
			}
			Location loc = event.getClickedBlock().getLocation();
			if(loc.getY() < Roomconstants.LobbyBottom) {
				if(!mmwplayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld) MMWWorld.getMMWWorld(loc.getWorld())).isLocationInRoom(new MMWLocation(loc))) {
					if(!mmwplayer.hasPerrmision(Permission.AccessUnownedRoom)) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Not_Allowed_Interact_Underground.toString());
					}	
				}
			}
		}		
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockbreak(BlockBreakEvent event) {
		if(event.getBlock() == null) return;
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getBlock().getWorld())) {
			MMWPlayer mmwplayer = MMWPlayer.getMMWPlayer(event.getPlayer());
			Location loc = event.getBlock().getLocation();
			if(loc.getY() >= Roomconstants.LobbyBottom) {
				if(!mmwplayer.hasPerrmision(Permission.ChangeLobby)) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Not_Allowed_Block_Change_Lobby.toString());
				}	
			} else {
				if(!mmwplayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld) MMWWorld.getMMWWorld(loc.getWorld())).isLocationInRoom(new MMWLocation(loc))) {
					if(!mmwplayer.hasPerrmision(Permission.AccessUnownedRoom)) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Not_Allowed_Interact_Underground.toString());
					}
				}
			}
		}		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockplace(BlockPlaceEvent event) {
		if(event.getBlock() == null) return;
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getBlock().getWorld())) {
			MMWPlayer mmwplayer = MMWPlayer.getMMWPlayer(event.getPlayer());
			Location loc = event.getBlock().getLocation();
			if(loc.getY() >= Roomconstants.LobbyBottom) {
				if(!mmwplayer.hasPerrmision(Permission.ChangeLobby)) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Not_Allowed_Block_Change_Lobby.toString());
				}	
			} else {
				if(!mmwplayer.getChestRoomPlayer().getRoomSettingsForWorld((MMWExchangeWorld) MMWWorld.getMMWWorld(loc.getWorld())).isLocationInRoom(new MMWLocation(loc))) {
					if(!mmwplayer.hasPerrmision(Permission.AccessUnownedRoom)) {
						event.setCancelled(true);
						event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Not_Allowed_Interact_Underground.toString());
					}	
				}
			}
		}		
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void pistonretract(BlockPistonRetractEvent  event) {
		if(event.getBlock() == null) return;
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getBlock().getWorld())) {
			MMWExchangeWorld mmwexworld = (MMWExchangeWorld)MMWWorld.getMMWWorld(event.getBlock().getWorld());
			MMWRoomSettings settings = mmwexworld.getRoomSettingsForLocation(event.getBlock().getLocation());
			if(settings != null) {
				if(!settings.isLocationInRoom(new MMWLocation(event.getRetractLocation())) && !event.getRetractLocation().getBlock().isEmpty()) {
					event.setCancelled(true);
				}
			} else {
				settings = mmwexworld.getRoomSettingsForLocation(event.getRetractLocation());
				if(settings != null) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void pistonextend(BlockPistonExtendEvent  event) {
		if(event.getBlock() == null) return;
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(event.getBlock().getWorld())) {
			MMWExchangeWorld mmwexworld = (MMWExchangeWorld)MMWWorld.getMMWWorld(event.getBlock().getWorld());
			MMWRoomSettings settings = mmwexworld.getRoomSettingsForLocation(event.getBlock().getLocation());
			if(settings != null) {
				for(Block block: event.getBlocks()) {
					if(!settings.isLocationInRoom(new MMWLocation(block.getLocation())) && !block.getLocation().getBlock().isEmpty()) {
						event.setCancelled(true);
					}
					if(!settings.isLocationInRoom(new MMWLocation(block.getRelative(event.getDirection(), 1).getLocation())) && !block.getLocation().getBlock().isEmpty()) {
						event.setCancelled(true);
					}
				}
			} else {
				for(Block block: event.getBlocks()) {
					settings = mmwexworld.getRoomSettingsForLocation(block.getLocation());
					if(settings != null) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
