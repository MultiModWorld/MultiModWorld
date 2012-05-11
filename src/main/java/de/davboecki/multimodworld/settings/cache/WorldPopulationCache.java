package de.davboecki.multimodworld.settings.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.HashBasedTable;

import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.utils.MMWWorld;

public class WorldPopulationCache extends Thread {
	
	private String settingsFilePath;
	private HashMap<String/* ChunkPos:'x;y' */,HashMap<String/* Modname */,Boolean/* ModPopulatedChunk */>> settingslist = null;
	private boolean changed = false;
	private long changetime = 0;
	
	public WorldPopulationCache(MMWWorld mmwworld) {
		settingsFilePath = mmwworld.getWorld().getWorldFolder().getPath() + "multimodmorld.populatechunk.yml";
		this.start();
	}
	
	public void reset() {
		settingslist = null;
		changed = false;
		changetime = 0;
		//TODO needed?
		System.gc();
	}
	
	private String cordsToString(int x, int z) {
		return x+";"+z;
	}
	
	public HashMap<String,Boolean> getModListForChunk(int x, int z) {
		if(settingslist == null) load();
		return settingslist.get(cordsToString(x, z));
	}
	
	public void addModPopulated(int x, int z,ModInfo info, boolean flag) {
		if(settingslist == null) load();
		if(!settingslist.containsKey(cordsToString(x, z)) || !(settingslist.get(cordsToString(x, z)) instanceof HashMap)) {
			settingslist.put(cordsToString(x, z), new HashMap<String,Boolean>());
		}
		settingslist.get(cordsToString(x, z)).put(info.getName(), flag);
	}

	public void addModPopulatedDenied(int x, int z,ModInfo info) {
		addModPopulated(x, z, info, false);
	}

	public void addModPopulated(int x, int z,ModInfo info) {
		addModPopulated(x, z, info, true);
	}
	
	public void save() {
		if(settingslist == null) load();
		Yaml yaml = new Yaml();
		try {
			Writer writer = new FileWriter(new File(settingsFilePath));
			writer.append("# DO NOT TOUCH");
			writer.append("# This is not a file you should change. All information in this file are changed by the system itself.");
			writer.append("# Any changes will break the plugin.");
			writer.append("# This are just status information.");
			writer.append("# DO NOT TOUCH");
			yaml.dump(settingslist, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		if(settingslist != null) return;
		Yaml yaml = new Yaml();
		try {
			settingslist = (HashMap<String,HashMap<String,Boolean>>)yaml.load(new FileInputStream(new File(settingsFilePath)));
		} catch (FileNotFoundException e) {
			settingslist = new HashMap<String,HashMap<String,Boolean>>();
		} catch(ClassCastException e) {
			settingslist = new HashMap<String,HashMap<String,Boolean>>();
			new File(settingsFilePath).renameTo(new File(settingsFilePath + new Date().toString() + ".backup"));
			Logger.getLogger("MultiModWorld").severe("'"+settingsFilePath+"' is broken. The broben file can be found at '" + settingsFilePath + new Date().toString() + ".backup' . A new config file will be regenerated. All old chunk information are lost! (You can correct the broken file and merge the information into the new config files)");
			save();
		}
	}
	
	public void changed() {
		changetime = System.currentTimeMillis();
		changed = true;
	}
	
	public void run() {
		while(true) {
			if(changed) {
				if((changetime + 10*1000) > System.currentTimeMillis()) {
					save();
					reset();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			} else {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {}
			}
		}
	}
}
