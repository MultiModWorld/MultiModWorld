package de.davboecki.multimodworld.mmwevents;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.utils.MMWWorld;
import net.minecraft.server.Entity;

public class EntityHandler {

	public Entity replayeEntity(String worldName, Entity entity) {
		MMWWorld mmwworld = MMWWorld.getMMWWorld(worldName);
		if(mmwworld == null) {
			throw new UnsupportedOperationException("This should not be null. Internal error. Please report this to the MMW authors.");
		}
		if(mmwworld.isModByNameEnabled("railcraft","rail craft","rail_craft")) {
			try {
				if(Class.forName("railcraft.common.carts.EntityCartChest").isInstance(entity)) {
					entity = new net.minecraft.server.EntityMinecart(entity.world, entity.locX, entity.locY, entity.locZ, 1);
				} else if(Class.forName("railcraft.common.carts.EntityCartSteam").isInstance(entity)) {
					entity = new net.minecraft.server.EntityMinecart(entity.world, entity.locX, entity.locY, entity.locZ, 2);
				}
			} catch (Exception e) {}
		}
		return entity;
	}
	
	public boolean isEntityAllowed(String worldName, Entity entity) {
		MMWWorld mmwworld = MMWWorld.getMMWWorld(worldName);
		if(mmwworld == null) {
			throw new UnsupportedOperationException("This should not be null. Internal error. Please report this to the MMW authors.");
		}
		ModInfo mod = MultiModWorld.getInstance().getModList().get(entity.getClass());
		if(mod != null) {
			return mmwworld.isModEnabled(mod);
		}
		return true;
	}

}
