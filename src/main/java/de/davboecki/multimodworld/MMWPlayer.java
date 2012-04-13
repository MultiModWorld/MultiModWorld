package de.davboecki.multimodworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet230ModLoader;

import org.bukkit.entity.Player;

import de.davboecki.multimodworld.exchangeworld.ExchangeWorldPlayer;
import de.davboecki.multimodworld.mod.ClientMod;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.mod.ModInfoBase;
import de.davboecki.multimodworld.settings.MMWPlayerSettings;

public class MMWPlayer {

	private final Player player;
	private final ExchangeWorldPlayer crplayer;
	private final MMWPlayerSettings settings;
	private ArrayList<ModInfoBase> KnownMods = new ArrayList<ModInfoBase>();
	private ArrayList<ModInfoBase> KnownModsOtherVersion = new ArrayList<ModInfoBase>();

	public Player getPlayer() {
		return player;
	}

	public ExchangeWorldPlayer getChestRoomPlayer() {
		return crplayer;
	}

	public String getName() {
		return player.getName();
	}

	public ModInfoBase[] getKnownMods() {
		//TODO
		return null;
	}

	public ModInfo[] getKnownServerMods() {
		//TODO
		return null;
	}
	
	public void handleModPacketResponse(Packet230ModLoader packet, List<String> bannedMods) {
		if(packet.dataString.length > 0) {
			List<String> Mods = new ArrayList<String>(Arrays.asList(packet.dataString));
			
			for(ModInfo info:MultiModWorld.getInstance().getModList()) {
				for(String ModName:packet.dataString) {
					if(info.equals(ModName)) {
						KnownMods.add(info);
						Mods.remove(ModName);
						break;
					} else if(info.equalsWithOtherVersion(ModName)) {
						KnownModsOtherVersion.add(info);
						Mods.remove(ModName);
						break;
					}
				}
			}
			for(String Modinfo:Mods){
				KnownMods.add(new ClientMod(Modinfo));
			}
		}
		System.out.println("Analysed Packet230");
	}
	
	// Player Create
	private static final ArrayList<MMWPlayer> playerlist = new ArrayList<MMWPlayer>();

	private MMWPlayer(Player player) {
		this.player = player;
		playerlist.add(this);
		crplayer = new ExchangeWorldPlayer(this);
		settings = new MMWPlayerSettings(this);
		if (settings.isNew()) {
			settings.save();
		} else {
			settings.load();
		}
	}

	private MMWPlayer(EntityPlayer eplayer) {
		this.player = eplayer.getBukkitEntity();
		playerlist.add(this);
		crplayer = new ExchangeWorldPlayer(this);
		settings = new MMWPlayerSettings(this);
		if (settings.isNew()) {
			settings.save();
		} else {
			settings.load();
		}
	}

	public static MMWPlayer getMMWPlayer(Player player) {
		for (final Object ommwplayer : playerlist.toArray()) {
			final MMWPlayer mmwplayer = (MMWPlayer) ommwplayer;
			mmwplayer.player.getName().equals(player.getName());
			return mmwplayer;
		}
		return new MMWPlayer(player);
	}
	public static MMWPlayer getMMWPlayer(EntityPlayer eplayer) {
		for (final Object ommwplayer : playerlist.toArray()) {
			final MMWPlayer mmwplayer = (MMWPlayer) ommwplayer;
			mmwplayer.player.getName().equals(eplayer.name);
			return mmwplayer;
		}
		return new MMWPlayer(eplayer);
	}
}
