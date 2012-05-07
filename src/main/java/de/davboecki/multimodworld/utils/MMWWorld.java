package de.davboecki.multimodworld.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.settings.MMWWorldSettings;

public class MMWWorld {
	
	private World world;
	MMWWorldSettings settings;
	private final HashMap<ModInfo,Boolean> ModList = new HashMap<ModInfo,Boolean>();
	
	public World getWorld() {
		return world;
	}

	public HashMap<ModInfo,Boolean> getModList(){
		return ModList;
	}

	//Create MMWWorld
	private static final ArrayList<MMWPlayer> worldlist = new ArrayList<MMWPlayer>();

	protected MMWWorld(World world) {
		this.world = world;
		settings = new MMWWorldSettings(this);
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
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(world)){
			return new MMWExchangeWorld(world);
		} else {
			return new MMWWorld(world);
		}
	}
	
	public static MMWWorld getMMWWorld(String worldname) {
		return getMMWWorld(Bukkit.getWorld(worldname));
	}
}
