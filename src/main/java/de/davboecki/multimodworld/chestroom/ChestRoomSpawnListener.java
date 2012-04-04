package de.davboecki.multimodworld.chestroom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import de.davboecki.multimodworld.MultiModWorld;

public class ChestRoomSpawnListener implements Listener {

	private final MultiModWorld plugin;

	ChestRoomSpawnListener(MultiModWorld instance) {
		plugin = instance;
	}

	@EventHandler
	public void CreatureSpawn(CreatureSpawnEvent event) {
		if (plugin.getRoomcontroler().isChestWorld(event.getLocation().getWorld())) {
			event.setCancelled(true);
		}
	}
}
