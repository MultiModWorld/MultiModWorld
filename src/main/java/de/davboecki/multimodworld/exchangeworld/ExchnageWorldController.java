package de.davboecki.multimodworld.exchangeworld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.exchangeworld.listener.PlayerMoveListener;

public class ExchnageWorldController {

	private final MultiModWorld plugin;
	public final ExchangeWorldGenerator generator = new ExchangeWorldGenerator();

	public ExchnageWorldController(MultiModWorld instance) {
		plugin = instance;
	}

	public void onLoad() {
	}

	public void onDisable() {
	}

	public void onEnable() {
		final PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new ExchangeWorldSpawnListener(plugin), plugin);
		pm.registerEvents(new PlayerMoveListener(plugin), plugin);
	}

	public boolean isChestWorld(World world) {
		if (world.getGenerator() == null) {
			return false;
		}
		return world.getGenerator().equals(generator);
	}
}
