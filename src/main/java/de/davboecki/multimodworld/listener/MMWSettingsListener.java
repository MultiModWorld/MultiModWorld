package de.davboecki.multimodworld.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import de.davboecki.multimodworld.MMWPlayer;
import de.davboecki.multimodworld.MMWWorld;

public class MMWSettingsListener implements Listener {
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		//Check and/or Create MMWPlayer Object
		MMWPlayer.getMMWPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void worldLoad(WorldLoadEvent event) {
		//Check and/or Create MMWWorld Object
		MMWWorld.getMMWWorld(event.getWorld());
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		MMWPlayer.getMMWPlayer(event.getPlayer()).disconnect();
	}
}
