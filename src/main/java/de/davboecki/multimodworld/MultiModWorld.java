package de.davboecki.multimodworld;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import de.davboecki.multimodworld.chestroom.ChestRoomControler;
import de.davboecki.multimodworld.mod.ModList;

public class MultiModWorld extends JavaPlugin {

	private final ChestRoomControler roomcontroler = new ChestRoomControler(this);
	private static MultiModWorld instance;
	private ModList ModList = new ModList();

	public ChestRoomControler getRoomcontroler() {
		return roomcontroler;
	}

	@Override
	public void onLoad() {
		roomcontroler.onLoad();
		if (instance != null) {
			getLogger().warning("Created MultiModWorld another time.");
		}
		instance = this;
	}

	@Override
	public void onDisable() {
		roomcontroler.onDisable();
	}

	@Override
	public void onEnable() {
		roomcontroler.onEnable();
		new MultiModWorldApiPlugin();
		try {
			ModList.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return roomcontroler.generator;
	}

	public static MultiModWorld getInstance() {
		@SuppressWarnings("restriction")
		final Class<?> caller = sun.reflect.Reflection.getCallerClass(3);
		final String[] Packet = caller.getPackage().getName().split("\\.");
		if (Packet[0].equals("de") && Packet[1].equals("davboecki") && Packet[2].equals("multimodworld")) {
			return instance;
		} else {
			return null;
		}
	}

	public ModList getModList() {
		return ModList;
	}
}
