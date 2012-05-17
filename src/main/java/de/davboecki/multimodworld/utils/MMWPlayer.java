package de.davboecki.multimodworld.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.EntityPlayer;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.exchangeworld.ExchangeWorldPlayer;
import de.davboecki.multimodworld.handler.Permission;
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
	
	
	public enum exchangeworldposition { normal_Area, other_Area, normal_RoomPortal, other_RoomPortal};
	
	public void teleport(Location to) {
		to.setPitch(player.getLocation().getPitch());
		to.setYaw(player.getLocation().getYaw());
		player.teleport(to);
	}
	
	public void teleport(MMWLocation to) {
		Location loc = to.getBukkitLocation(MMWWorld.getMMWWorld(player.getWorld()));
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		player.teleport(loc);
	}
	
	public void teleportWithGateViewCheck(MMWLocation to) {
		teleportWithGateViewCheck(to.getBukkitLocation(MMWWorld.getMMWWorld(player.getWorld())));
	}
	
	private int getGateOutputYaw(Location to, boolean shifted) {
		Location BlockEye = to.clone();
		BlockEye.add(new Vector(1,1.62D,0));
		Block b1 = BlockEye.getBlock();
		BlockEye.add(new Vector(-1,0,1));
		Block b2 = BlockEye.getBlock();
		BlockEye.add(new Vector(-1,0,-1));
		Block b3 = BlockEye.getBlock();
		BlockEye.add(new Vector(1,0,-1));
		Block b4 = BlockEye.getBlock();
		if(b1.isEmpty() && !b2.isEmpty() && !b3.isEmpty() && !b4.isEmpty()) {
			return shifted ? 90: 270;
		} else if(!b1.isEmpty() && b2.isEmpty() && !b3.isEmpty() && !b4.isEmpty()) {
			return shifted ? 180: 0;
		} else if(!b1.isEmpty() && !b2.isEmpty() && b3.isEmpty() && !b4.isEmpty()) {
			return shifted ? 270: 90;
		} else if(!b1.isEmpty() && !b2.isEmpty() && !b3.isEmpty() && b4.isEmpty()) {
			return shifted ? 0: 180;
		} else {
			return -1;
		}
	}
	
	public void teleportWithGateViewCheck(Location to) {
		to.setPitch(player.getLocation().getPitch());
		float Yaw = player.getLocation().getYaw();
		int YawGatefrom = getGateOutputYaw(player.getLocation(),true);
		int YawGateto = getGateOutputYaw(to,false);
		if(YawGateto == -1 || YawGatefrom == -1) {
			to.setYaw(Yaw);
			player.sendMessage("No Portal");
		} else {
			to.setYaw(Yaw-YawGatefrom+YawGateto);
		}
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
	
	public MMWLocation getLocation() {
		return new MMWLocation(this.getPlayer().getLocation().getX(), this.getPlayer().getLocation().getY(), this.getPlayer().getLocation().getZ());
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
	
	public boolean knowsmod(ModInfo info) {
		return KnownMods.contains(info);
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
	
	public boolean hasPerrmision(Permission perm) {
		return perm.hasPermission(player);
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
	
	static MMWPlayer[] getAllMMWPlayers() {
		return playerlist.toArray(new MMWPlayer[0]);
	}
	
	//Base Functions
	@Override
	public boolean equals(Object object) {
		if(object == null) return false;
		if(!(object instanceof MMWPlayer)) return false;
		MMWPlayer mmwplayer = (MMWPlayer)object;
		return mmwplayer.player.getName() == this.player.getName();
	}
}
