package de.davboecki.multimodworld;

import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import de.davboecki.multimodworld.exchangeworld.ExchnageWorldController;
import de.davboecki.multimodworld.listener.MMWSettingsListener;
import de.davboecki.multimodworld.mod.ModList;
import de.davboecki.multimodworld.commands.CommandHandler;
import de.davboecki.multimodworld.commands.ConfirmListener;

public class MultiModWorld extends JavaPlugin {

	private final ExchnageWorldController roomcontroler = new ExchnageWorldController(this);
	private static MultiModWorld instance;
	private ModList ModList = new ModList();
	private WorldEditPlugin worldEdit;
	private CommandHandler commandHandler = new CommandHandler();

	public ExchnageWorldController getRoomcontroler() {
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
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new ConfirmListener(),this);
		pm.registerEvents(new MMWSettingsListener(), this);
		
		
		Plugin wePlugin = getServer().getPluginManager().getPlugin("WorldEdit");
        if (wePlugin != null) {
            worldEdit = (WorldEditPlugin) wePlugin;
           //TODO Log
        }
        getCommand("multimodworld").setExecutor(commandHandler);
        getCommand("mmw").setExecutor(commandHandler);
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return ExchnageWorldController.generator;
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

	public WorldEditPlugin getWorldEdit() {
		return worldEdit;
	}
}
