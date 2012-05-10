package de.davboecki.multimodworld.mmwevents;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.CraftingRecipe;

import de.davboecki.multimodworld.utils.MMWWorld;

public class RecipiesHandler {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List replaceRecipies(List recipies, String worldname) {
		MMWWorld mmwworld = MMWWorld.getMMWWorld(worldname);
		if(mmwworld != null) {
			return mmwworld.getRecipies((ArrayList<CraftingRecipe>)recipies);
		} else {
			return recipies;
		}
	}

}
