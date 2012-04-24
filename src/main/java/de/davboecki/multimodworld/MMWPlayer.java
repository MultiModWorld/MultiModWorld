package de.davboecki.multimodworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.EntityPlayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.davboecki.multimodworld.exchangeworld.ExchangeWorldPlayer;
import de.davboecki.multimodworld.mod.ClientMod;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.mod.ModInfoBase;
import de.davboecki.multimodworld.settings.MMWPlayerSettings;
import forge.packets.PacketModList;

public class MMWPlayer {

	private final Player player;
	private final ExchangeWorldPlayer crplayer;
	private final MMWPlayerSettings settings;
	private ArrayList<ModInfoBase> KnownMods = new ArrayList<ModInfoBase>();
	private ArrayList<ModInfoBase> KnownModsOtherVersion = new ArrayList<ModInfoBase>();
	private long teleportcooldown;
	
	public void teleport(Location to) {
		to.setPitch(player.getLocation().getPitch());
		to.setYaw(player.getLocation().getYaw());
		player.teleport(to);
	}
	
	public long getTeleportcooldown() {
		return teleportcooldown;
	}

	public void clearTeleportcooldown() {
		teleportcooldown = -1;
	}

	public void setWaitTeleportcooldown() {
		teleportcooldown = -2;
	}

	public boolean waitTeleportcooldown() {
		return teleportcooldown == -2;
	}
	
	public boolean needTeleportcooldown() {
		if(teleportcooldown == -1) return false;
		if(teleportcooldown == -2) return true;
		return teleportcooldown > (System.currentTimeMillis() - 500);
	}

	public void setTeleportcooldownNow() {
		teleportcooldown = System.currentTimeMillis();
	}
	
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
		return KnownMods.toArray(new ModInfoBase[0]);
	}

	public ModInfoBase[] getKnownModsWithDifferentVersion() {
		return KnownModsOtherVersion.toArray(new ModInfoBase[0]);
	}

	public ModInfo[] getKnownServerMods() {
		return KnownMods.toArray(new ModInfo[0]);
	}
	
	public void disconnect() {
		settings.save();
		playerlist.remove(this);
	}
	
	public void handleModPacketResponse(PacketModList pkt) {
		if(pkt.Mods.length > 0) {
			List<String> Mods = new ArrayList<String>(Arrays.asList(pkt.Mods));
			
			for(ModInfo info:MultiModWorld.getInstance().getModList()) {
				for(String ModName:pkt.Mods) {
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
		System.out.println("Analysed PacketModList");
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
	
	public static MMWPlayer getDefinedMMWPlayer(String Name) {
		for (final Object ommwplayer : playerlist.toArray()) {
			final MMWPlayer mmwplayer = (MMWPlayer) ommwplayer;
			mmwplayer.player.getName().equalsIgnoreCase(Name);
			return mmwplayer;
		}
		return null;
	}
}
