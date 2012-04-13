package de.davboecki.multimodworld;

import java.util.List;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet230ModLoader;

import org.bukkit.entity.Player;

import de.davboecki.multimodworld.api.ModChecker;
import de.davboecki.multimodworld.api.plugin.IModWorldHandlePlugin;

public class MultiModWorldApiPlugin implements IModWorldHandlePlugin {
	
	MultiModWorldApiPlugin() {
		ModChecker.registerIModWorldHandlePlugin(this);
	}
	
	public boolean PacketSend(Packet packet, Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	public Entity ReplaceEntity(String WorldName, Entity entity) {
		// TODO Auto-generated method stub
		return entity;
	}

	public boolean hasWorldSetting(String WorldName, String setting) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCraftingAllowed(String WorldName, int id) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEntityAllowed(String WorldName, Entity entity) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isIdAllowed(String WorldName, int id) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean handleModPacketResponse(Packet230ModLoader packet, EntityPlayer player, List bannedMods) {
		MMWPlayer.getMMWPlayer(player).handleModPacketResponse(packet, bannedMods);
		return true;
	}

}
