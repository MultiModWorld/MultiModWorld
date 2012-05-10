package de.davboecki.multimodworld.settings.cache;

import java.util.ArrayList;

import net.minecraft.server.CraftingRecipe;

public class MMWWorldCache extends Cache {

	public ArrayList<CraftingRecipe> craftinglistForWorld = null;
	public ArrayList<CraftingRecipe> vanillacraftinglistForWorld = null;
	public int lastallowedid = -1;
	public int lastblockedid = -1;
	
	public void reset() {
		craftinglistForWorld = null;
		vanillacraftinglistForWorld = null;
		lastallowedid = -1;
		lastblockedid = -1;
	}
}
