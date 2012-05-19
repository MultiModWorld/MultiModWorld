package de.davboecki.multimodworld.listener;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.davboecki.multimodworld.handler.ColorHandler;
import de.davboecki.multimodworld.handler.LanguageHandler;
import de.davboecki.multimodworld.utils.MMWPlayer;

public class PlayerFakeLockListener implements Listener {
	
	private ArrayList<String> lockedPlayers = new ArrayList<String>();

	public void addPlayer(String name) {
		if(!lockedPlayers.contains(name)) {
			lockedPlayers.add(name);
		}
	}

	public void removePlayer(String name) {
		lockedPlayers.remove(name);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if(lockedPlayers.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Access_Denied);
			event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Remove_Item_First);
			MMWPlayer.getMMWPlayer(event.getPlayer()).fakeLockPlayer();
		}
	}

	@EventHandler
	public void teleport(PlayerTeleportEvent event) {
		if(lockedPlayers.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Access_Denied);
			event.getPlayer().sendMessage(ColorHandler.RED.toString() + LanguageHandler.Remove_Item_First);
		}
	}
}
