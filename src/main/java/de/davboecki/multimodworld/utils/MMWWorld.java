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
import de.davboecki.multimodworld.settings.cache.WorldPopulationCache;

public class MMWWorld {
	
	private World world;
	MMWWorldSettings settings;
	private final HashMap<ModInfo,Boolean> WorldModList = new HashMap<ModInfo,Boolean>();
	private MMWWorldCache cache = new MMWWorldCache();
	private WorldPopulationCache populationchache;
	
	public World getWorld() {
		return world;
	}

	public HashMap<ModInfo,Boolean> getModList(){
		return WorldModList;
	}
	
	public boolean isModEnabled(ModInfo info) {
		if(!WorldModList.containsKey(info)) {
			return false;
		}
		return WorldModList.get(info);
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
		/*for(ModInfo info : this.WorldModList.keySet()) {
			if(this.WorldModList.get(info)) {
				if(Arrays.asList(info.getIds()).contains(id)){
					return true;
				}
			} else {
				if(Arrays.asList(info.getIds()).contains(id)){
					return false;
				}
			}
		}*/
		if(id == 97) return false;
		ModInfo info = MultiModWorld.getInstance().getModList().getById(id);
		if(info == null) return true;
		return WorldModList.containsKey(info) && WorldModList.get(info);
	}
	
	public boolean isModByNameEnabled(String... names) {
		for(ModInfo info : this.WorldModList.keySet()) {
			if(this.WorldModList.get(info)) {
				for(String name:names) {
					if(info.getName().toLowerCase().contains(name.toLowerCase())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public WorldPopulationCache getPopulationchache() {
		return populationchache;
	}

	private void addUnListedMods() {
		for(ModInfo info: MultiModWorld.getInstance().getModList()) {
			if(!WorldModList.containsKey(info)) {
				WorldModList.put(info,false);
			}
		}
	}
	
	//Create MMWWorld
	private static final ArrayList<MMWWorld> worldlist = new ArrayList<MMWWorld>();

	protected MMWWorld(World world) {
		this.world = world;
		settings = new MMWWorldSettings(this);
		if (settings.isNew()) {
			settings.save();
		} else {
			settings.load();
		}
		populationchache = new WorldPopulationCache(this);
		worldlist.add(this);
		addUnListedMods();
	}

	public static MMWWorld getMMWWorld(World world) {
		for (final Object ommwworld : worldlist.toArray()) {
			final MMWWorld mmwworld = (MMWWorld) ommwworld;
			if(mmwworld.world.getName().equals(world.getName())) {
				return mmwworld;
			}
		}
		if(MultiModWorld.getInstance().getRoomcontroler().isChestWorld(world)){
			return new MMWExchangeWorld(world);
		} else {
			return new MMWWorld(world);
		}
	}
	
	public static MMWWorld getMMWWorld(String worldname) {
		for (final Object ommwworld : worldlist.toArray()) {
			final MMWWorld mmwworld = (MMWWorld) ommwworld;
			if(mmwworld.world.getName().equals(worldname)) {
				return mmwworld;
			}
		}
		World world;
		if((world = Bukkit.getWorld(worldname)) == null) {
			return null;
		}
		return getMMWWorld(world);
	}
	
	//Base Functions
	@Override
	public boolean equals(Object object) {
		if(object == null) return false;
		if(!(object instanceof MMWWorld)) return false;
		MMWWorld mmwworld = (MMWWorld)object;
		return mmwworld.world.getName().equals(this.world.getName());
	}
}
