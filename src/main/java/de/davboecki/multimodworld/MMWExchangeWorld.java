package de.davboecki.multimodworld;

import org.bukkit.World;

import de.davboecki.multimodworld.settings.MMWExchangeWorldSettings;

public class MMWExchangeWorld extends MMWWorld {

	protected MMWExchangeWorld(World world) {
		super(world);
		settings = new MMWExchangeWorldSettings(this);
	}
}
