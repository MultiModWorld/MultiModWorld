package de.davboecki.multimodworld;

import java.util.ArrayList;

import org.bukkit.World;

import de.davboecki.multimodworld.chestroom.ChestRoomControler;
import de.davboecki.multimodworld.settings.MMWWorldSettings;

public class MMWWorld {
	
	private World world;
	MMWWorldSettings settings;
	
	
	public World getWorld() {
		return world;
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
}
