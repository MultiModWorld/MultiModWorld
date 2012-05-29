package de.davboecki.multimodworld.settings.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.yaml.snakeyaml.Yaml;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.utils.MMWWorld;

public class WorldPopulationCache extends Thread {
	
	private String settingsFilePath;
	private HashMap<String/* ChunkPos:'x;y' */,HashMap<String/* Modname */,Boolean/* ModPopulatedChunk */>> settingslist = null;
	private java.util.Queue<QueueNode> Queue = new LinkedList<QueueNode>();
	private boolean changed = false;
	private long changetime = 0;
	private String worldname = "";
	
	public WorldPopulationCache(MMWWorld mmwworld) {
		super("MMWCacheSaveThread-"+mmwworld.getWorld().getName());
		worldname = mmwworld.getWorld().getName();
		settingsFilePath = mmwworld.getWorld().getWorldFolder().getAbsolutePath() + "\\populatechunk.multimodworld.dat";
		this.start();
	}
	
	public synchronized void reset() {
		settingslist = null;
		changed = false;
		changetime = 0;
	}
	
	private String cordsToString(int x, int z) {
		return "("+x+";"+z+")";
	}
	
	public HashMap<String,Boolean> getModListForChunk(int x, int z) {
		if(settingslist == null) load();
		return settingslist.get(cordsToString(x, z));
	}
	
	public void addModPopulated(int x, int z, ModInfo info, boolean flag) {
		Queue.add(new QueueNode(x, z, info, flag));
	}

	private void save() {
		if(settingslist == null) load();
		Yaml yaml = new Yaml();
		try {
			new File(new File(settingsFilePath).getParent()).mkdirs();
			Writer writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(new File(settingsFilePath))));
			writer.append("# DO NOT TOUCH\n");
			writer.append("# This is not a file you should change. All information in this file are changed by the system itself.\n");
			writer.append("# Any changes will break the plugin.\n");
			writer.append("# This are just status information.\n");
			writer.append("# This file is for the world '"+worldname+"'.\n");
			writer.append("# DO NOT TOUCH\n");
			yaml.dump(settingslist, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void load() {
		if(settingslist != null) return;
		Yaml yaml = new Yaml();
		try {
			settingslist = (HashMap<String,HashMap<String,Boolean>>)yaml.load(new GZIPInputStream(new FileInputStream(new File(settingsFilePath))));
		} catch (FileNotFoundException e) {
			settingslist = new HashMap<String,HashMap<String,Boolean>>();
			save();
		} catch(Exception e) {
			settingslist = new HashMap<String,HashMap<String,Boolean>>();
			new File(settingsFilePath).renameTo(new File(settingsFilePath + new Date().toString() + ".backup"));
			Logger.getLogger("MultiModWorld").severe("'"+settingsFilePath+"' is broken. The broben file can be found at '" + settingsFilePath + new Date().toString() + ".backup' . A new config file will be regenerated. All old chunk information are lost! (You can correct the broken file and merge the information into the new config files)");
			save();
		}
		if(settingslist == null) {
			settingslist = new HashMap<String,HashMap<String,Boolean>>();
		}
	}

	public void run() {
		load();
		while(true) {
			if(Queue.size() > 0) {
				QueueNode node = Queue.poll();
				if(node.info == null) {
					node.info = MultiModWorld.getInstance().getModList().get(node.container);
				}
				if(settingslist == null) load();
				if(!settingslist.containsKey(cordsToString(node.x, node.z)) || !(settingslist.get(cordsToString(node.x, node.z)) instanceof HashMap)) {
					settingslist.put(cordsToString(node.x, node.z), new HashMap<String,Boolean>());
				}
				if(settingslist.get(cordsToString(node.x, node.z)).containsKey(node.info.getName())) {
					settingslist.get(cordsToString(node.x, node.z)).remove(node.info.getName());
				}
				settingslist.get(cordsToString(node.x, node.z)).put(node.info.getName(), node.flag);
				changetime = System.currentTimeMillis();
				changed = true;
			} else if(changed) {
				if((changetime + 10*1000) > System.currentTimeMillis()) {
					System.out.print("Unload.");
					save();
					reset();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
	}
}
