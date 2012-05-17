package de.davboecki.multimodworld.mmwevents;

import cpw.mods.fml.common.ModContainer;
import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.utils.MMWWorld;

public class ChunkHandler {
	
	private MMWWorld lastused = null;
	
	public boolean populateChunk(String name, int chunkX, int chunkZ, ModContainer mod) {
		MMWWorld mmwworld;
		if(lastused != null && lastused.getWorld().getName().equalsIgnoreCase(name)) {
			mmwworld = lastused;
		} else {
			mmwworld = lastused = MMWWorld.getMMWWorld(name);
		}
		if(mmwworld == null) {
			throw new UnsupportedOperationException("This should not be null. Internal error. Please report this to the MMW authors.");
		}
		ModInfo info = MultiModWorld.getInstance().getModList().get(mod);
		if(mmwworld.isModEnabled(info)) {
			mmwworld.getPopulationchache().addModPopulated(chunkX, chunkZ, info, true);
			return true;
		} else {
			mmwworld.getPopulationchache().addModPopulated(chunkX, chunkZ, info, false);
			return false;
		}
	}
}
