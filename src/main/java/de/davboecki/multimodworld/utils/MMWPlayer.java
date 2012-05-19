package de.davboecki.multimodworld.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.craftbukkit.ItemInformation;
import de.davboecki.multimodworld.exchangeworld.ExchangeWorldPlayer;
import de.davboecki.multimodworld.handler.LanguageHandler;
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
	private int taskId;
	
	
	public enum exchangeworldposition { normal_Area, other_Area, normal_RoomPortal, other_RoomPortal};
	
	private class InventoryControler implements Runnable {
		ItemStack[] old;
		boolean[] lockedfor;
		boolean locked = false;
		
		public void run() {
			boolean force = false;
			ItemStack[] inv = getTotalInventory();
			if(old == null) {
				old = inv;
				lockedfor = new boolean[old.length];
				force = true;
			}
			for(int i=0;i < inv.length;i++){
				if(old[i] != inv[i] || force) {
					old[i] = inv[i];
					if(inv[i] != null && !getMMWWorld().isIdAllowed(inv[i].getTypeId()) && !MultiModWorld.getInstance().getRoomcontroler().isChestWorld(player.getPlayer().getWorld())) {
						if(!locked) {
							fakeLockPlayer();
							player.sendMessage(ChatColor.RED.toString() + LanguageHandler.Remove_Denied_Item.toString());
							locked = true;
						}
						player.sendMessage(ChatColor.RED.toString() + new ItemInformation(inv[i].getTypeId()).getName());
						lockedfor[i] = true;
					} else if(lockedfor[i]) {
						lockedfor[i] = false;
						boolean last = true;
						for(boolean flag:lockedfor) {
							if(flag) {
								last = false;
							}
						}
						if(last) {
							unLockPlayer();
							player.sendMessage(ChatColor.GREEN.toString() + LanguageHandler.Free_Go.toString());
							locked = false;
						}
					}
				}
			}
		}
		
	}

	private ArrayList<Block> fakechangedBlocks = null;
	private ArrayList<Block> fakechangedTorchs = null;
	
	public void fakeLockPlayer() {
		MultiModWorld.getInstance().getFakeLockListener().addPlayer(player.getName());
		fakechangedBlocks = new ArrayList<Block>();
		fakechangedTorchs = new ArrayList<Block>();
		for(int x = -1;x < 2;x++) {
			for(int y = -1;y < 3;y++) {
				for(int z = -1;z < 2;z++) {
					Block block = player.getLocation().clone().add(new Vector(x, y, z)).getBlock();
					if(block.isEmpty() || block.getType() == Material.SNOW) {
						if(x == 0 && z == 0 && y != 2 && y != -1) {
							player.sendBlockChange(block.getLocation(), Material.TORCH.getId(), (byte)3);
							fakechangedTorchs.add(block);
						} else {
							player.sendBlockChange(block.getLocation(), Material.OBSIDIAN.getId(), (byte)0);
							fakechangedBlocks.add(block);
						}
					}
				}
			}
		}
	}
	
	public void unLockPlayer() {
		for(Block block:fakechangedBlocks) {
			player.sendBlockChange(block.getLocation(), block.getTypeId(), block.getData());
		}
		for(Block block:fakechangedTorchs) {
			player.sendBlockChange(block.getLocation(), block.getTypeId(), block.getData());
		}
		fakechangedBlocks = null;
		fakechangedTorchs = null;
		MultiModWorld.getInstance().getFakeLockListener().removePlayer(player.getName());
	}
	
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
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	public ItemStack[] getTotalInventory() {
		ItemStack[] items = new ItemStack[player.getInventory().getSize() + 4];
		int i = 0;
		for(ItemStack stack:player.getInventory()) {
			items[i] = stack;
			i++;
		}
		items[i++] = player.getInventory().getHelmet();
		items[i++] = player.getInventory().getChestplate();
		items[i++] = player.getInventory().getLeggings();
		items[i++] = player.getInventory().getBoots();
		return items;
	}
	
	public boolean knowsId(int id) {
		ModInfo info = MultiModWorld.getInstance().getModList().getById(id);
		if(info == null) return true;
		return KnownMods.contains(info);
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
	
	public MMWWorld getMMWWorld() {
		return MMWWorld.getMMWWorld(player.getWorld());
	}
	
	public void cleanUnknownItem() {
		for(int i = 0;i < player.getInventory().getSize() + 4;i++) {
			if(player.getInventory().getItem(i) != null) {
				if(!knowsId(player.getInventory().getItem(i).getTypeId())) {
					player.getInventory().setItem(i, null);
				}
			}
		}
		
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
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(MultiModWorld.getInstance(), new InventoryControler(), 1, 10);
	}

	private MMWPlayer(EntityPlayer eplayer) {
		this(eplayer.getBukkitEntity());
	}

	public static MMWPlayer getMMWPlayer(Player player) {
		for (final Object ommwplayer : playerlist.toArray()) {
			final MMWPlayer mmwplayer = (MMWPlayer) ommwplayer;
			if(mmwplayer.player.getName().equals(player.getName())) {
				return mmwplayer;
			}
		}
		return new MMWPlayer(player);
	}
	
	public static MMWPlayer getMMWPlayer(EntityPlayer eplayer) {
		for (final Object ommwplayer : playerlist.toArray()) {
			final MMWPlayer mmwplayer = (MMWPlayer) ommwplayer;
			if(mmwplayer.player.getName().equals(eplayer.name)) {
				return mmwplayer;
			}
		}
		return new MMWPlayer(eplayer);
	}
	
	public static MMWPlayer getDefinedMMWPlayer(String Name) {
		for (final Object ommwplayer : playerlist.toArray()) {
			final MMWPlayer mmwplayer = (MMWPlayer) ommwplayer;
			if(mmwplayer.player.getName().equalsIgnoreCase(Name)) {
				return mmwplayer;
			}
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
