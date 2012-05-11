package de.davboecki.multimodworld.mmwevents;

import org.bukkit.craftbukkit.CraftWorld;

import cpw.mods.fml.common.ModContainer;
import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.utils.MMWWorld;

public class ChunkHandler {

	public boolean populateChunk(String name, int chunkX, int chunkZ, ModContainer mod) {
		MMWWorld mmwworld = MMWWorld.getMMWWorld(name);
		if(mmwworld == null) {
			throw new UnsupportedOperationException("This should not be null. Internal error. Please report this to the MMW authors.");
		}
		mmwworld.getPopulationchache().load();
		mmwworld.getPopulationchache().changed();
		ModInfo info = MultiModWorld.getInstance().getModList().get(mod);
		if(mmwworld.isModEnabled(info)) {
			mmwworld.getPopulationchache().addModPopulated(chunkX, chunkZ, info);
			return true;
		} else {
			mmwworld.getPopulationchache().addModPopulatedDenied(chunkX, chunkZ, info);
			return false;
		}
	}

}
