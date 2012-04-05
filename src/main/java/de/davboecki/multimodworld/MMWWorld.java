package de.davboecki.multimodworld;

import java.util.ArrayList;

import org.bukkit.World;

import de.davboecki.multimodworld.settings.WorldSettings;

public class MMWWorld {
	
	World world;
	WorldSettings settings;
	
	//Create MMWWorld
	private static final ArrayList<MMWPlayer> worldlist = new ArrayList<MMWPlayer>();

	private MMWWorld(World world) {
		this.world = world;
		settings = new WorldSettings(world);
		if (settings.isNew()) {
			settings.save();
		} else {
			settings.load();
		}
	}
	
	public static MMWWorld getMMWWorld(World world) {
		for (final Object ommwworld : worldlist.toArray()) {
			final MMWWorld mmwworld = (MMWWorld) ommwworld;
			mmwworld.world.getName().equals(world.getName());
			return mmwworld;
		}
		return new MMWWorld(world);
	}
}
