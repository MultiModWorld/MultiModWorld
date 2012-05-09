package de.davboecki.multimodworld.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.server.CraftingRecipe;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.settings.MMWWorldSettings;
import de.davboecki.multimodworld.settings.cache.MMWWorldCache;

public class MMWWorld {
	
	private World world;
	MMWWorldSettings settings;
	private final HashMap<ModInfo,Boolean> WorldModList = new HashMap<ModInfo,Boolean>();
	private MMWWorldCache cache = new MMWWorldCache();
	
	public World getWorld() {
		return world;
	}

	public HashMap<ModInfo,Boolean> getModList(){
		return WorldModList;
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
	
	public ArrayList<CraftingRecipe> getRecipies(ArrayList<CraftingRecipe> normal) {
		if(cache.craftinglistForWorld == null || !cache.vanillacraftinglistForWorld.equals(normal)) {
			ArrayList<CraftingRecipe> list = new ArrayList<CraftingRecipe>(normal);
			for(ModInfo info : this.WorldModList.keySet()) {
				if(this.WorldModList.get(info)) {
					if(info.getAddedRecipies() != null) {
						list.addAll(Arrays.asList(info.getAddedRecipies()));
					}
					if(info.getRemovedRecipies() != null) {
						list.removeAll(Arrays.asList(info.getRemovedRecipies()));
					}
				}
			}
			cache.vanillacraftinglistForWorld = normal;
			return cache.craftinglistForWorld = list;
		} else {
			return cache.craftinglistForWorld;
		}
	}
	
	public boolean isIdAllowed(int id) {
		for(ModInfo info : this.WorldModList.keySet()) {
			if(this.WorldModList.get(info)) {
				if(Arrays.asList(info.getIds()).contains(id)){
					return true;
				}
			} else {
				if(Arrays.asList(info.getIds()).contains(id)){
					return false;
				}
			}
		}
		return true;
	}
	
	public static MMWWorld getMMWWorld(String worldname) {
		return getMMWWorld(Bukkit.getWorld(worldname));
	}
}
