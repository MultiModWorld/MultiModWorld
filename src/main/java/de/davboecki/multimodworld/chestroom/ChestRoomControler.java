package de.davboecki.multimodworld.chestroom;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.chestroom.listener.PlayerMoveListener;

public class ChestRoomControler {

	private final MultiModWorld plugin;
	public final ChestRoomGenerator generator = new ChestRoomGenerator();

	public ChestRoomControler(MultiModWorld instance) {
		plugin = instance;
	}

	public void onLoad() {
	}

	public void onDisable() {
	}

	public void onEnable() {
		final PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new ChestRoomSpawnListener(plugin), plugin);
		pm.registerEvents(new PlayerMoveListener(plugin), plugin);
	}

	public boolean isChestWorld(World world) {
		if (world.getGenerator() == null) {
			return false;
		}
		return world.getGenerator().equals(generator);
	}
}
