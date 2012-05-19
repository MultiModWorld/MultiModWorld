package de.davboecki.multimodworld.exchangeworld;

import org.bukkit.World;

import de.davboecki.multimodworld.MultiModWorld;

public class TimeControler implements Runnable {
	
	public void run() {
		for(World world:MultiModWorld.getInstance().getServer().getWorlds()) {
			if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(world)) {
				if(world.getTime() > 12000) {
					world.setTime(0);
				}
			}
		}
	}

}
