package de.davboecki.multimodworld;

import java.util.List;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet;

import org.bukkit.entity.Player;

import de.davboecki.multimodworld.api.ModChecker;
import de.davboecki.multimodworld.api.plugin.IModWorldHandlePlugin;
import de.davboecki.multimodworld.mmwevents.EntityHandler;
import de.davboecki.multimodworld.mmwevents.IDHandler;
import de.davboecki.multimodworld.mmwevents.PacketHandler;
import de.davboecki.multimodworld.mmwevents.RecipiesHandler;
import de.davboecki.multimodworld.mmwevents.WorldSettingsHandler;
import de.davboecki.multimodworld.utils.MMWPlayer;
import forge.packets.PacketModList;

public class MultiModWorldApiPlugin implements IModWorldHandlePlugin {
	
	PacketHandler packethandler = new PacketHandler();
	EntityHandler entityhandler = new EntityHandler();
	IDHandler idhandler = new IDHandler();
	RecipiesHandler recipieshandler = new RecipiesHandler();
	WorldSettingsHandler worldsettingshandler = new WorldSettingsHandler();
	
	MultiModWorldApiPlugin() {
		ModChecker.registerIModWorldHandlePlugin(this);
	}
	
	public boolean PacketSend(Packet packet, Player player) { // TODO
		return packethandler.handlePacketByServer(packet, player);
	}

	public Entity ReplaceEntity(String WorldName, Entity entity) { // TODO
		return entityhandler.replayeEntity(WorldName,entity);
	}

	public boolean hasWorldSetting(String WorldName, String setting) { // TODO
		return worldsettingshandler.hasWorldSetting(WorldName, setting);
	}

	public boolean isCraftingAllowed(String WorldName, int id) {
		return isIdAllowed(WorldName, id);
	}

	public boolean isEntityAllowed(String WorldName, Entity entity) { // TODO
		return entityhandler.isEntityAllowed(WorldName, entity);
	}

	public boolean isIdAllowed(String WorldName, int id) {
		return idhandler.isIdAllowed(WorldName, id);
	}
	
	public boolean handleModPacketResponse(EntityPlayer eplayer, PacketModList pkt) {
		MMWPlayer.getMMWPlayer(eplayer).handleModPacketResponse(pkt);
		return false;
	}

	@SuppressWarnings("rawtypes")
	public List replaceRecipies(List recipies, String Worldname) {
		return recipieshandler.replaceRecipies(recipies, Worldname);
	}

}
