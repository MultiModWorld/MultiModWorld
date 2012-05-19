package de.davboecki.multimodworld.mmwevents;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet103SetSlot;
import net.minecraft.server.Packet104WindowItems;
import net.minecraft.server.Packet132TileEntityData;
import net.minecraft.server.Packet250CustomPayload;
import net.minecraft.server.Packet50PreChunk;
import net.minecraft.server.Packet51MapChunk;
import net.minecraft.server.Packet53BlockChange;

import org.bukkit.entity.Player;

import de.davboecki.multimodworld.MultiModWorld;
import de.davboecki.multimodworld.handler.ColorHandler;
import de.davboecki.multimodworld.handler.LanguageHandler;
import de.davboecki.multimodworld.mod.ModInfo;
import de.davboecki.multimodworld.utils.MMWPlayer;

public class PacketHandler {
	
	Logger logger = Logger.getLogger("MultiModWorld");
	
	public PacketHandler(){}
	
	public boolean handlePacketByServer(Packet packet, Player player) {
		if(packet == null) {
			return true;
		}
		if(MultiModWorld.isdebug() && false) {
			StringBuilder logentry = new StringBuilder();
			logentry.append("Player:"+player.getName()+", Packet:"+packet.getClass().getName());
			logentry.append(", ");
			try {
				Field a = Packet.class.getDeclaredField("a");
				a.setAccessible(true);
				@SuppressWarnings("unchecked")
				HashMap<Class<?>, Integer> aMap = (HashMap<Class<?>, Integer>)a.get(null);
				logentry.append("Real Number: "+aMap.get(packet.getClass())+", ");
			} catch(Exception e) {}
			for(Field f:packet.getClass().getDeclaredFields()) {
				try {
					try {
						f.setAccessible(true);
					} catch(Exception e) {}
					Object o = f.get(packet);
					try {
						Object[] oarray = (Object[])o;
						StringBuilder arrayentry = new StringBuilder();
						arrayentry.append("[");
						for(Object opart:oarray) {
							if(arrayentry.toString().equals("[")) {
								arrayentry.append(opart.toString());
							} else {
								arrayentry.append(", "+opart.toString());
							}
						logentry.append(f.getName()+": '"+arrayentry+"]', ");
						}
					} catch(Exception e) {
						logentry.append(f.getName()+": '"+o+"', ");
					}
				} catch(Exception e) {
					logentry.append(f.getName()+": 'ACCESS_DENIED', ");
				}
			}
			logger.info(logentry.toString());
		}
		if(packet instanceof Packet250CustomPayload) {
			return handle250Packet((Packet250CustomPayload)packet,player);
		} else if(packet instanceof Packet132TileEntityData) {
			return handle132Packet((Packet132TileEntityData)packet,player);
		} else if(packet instanceof Packet53BlockChange) {
			return handle53Packet((Packet53BlockChange)packet,player);
		} else if(packet instanceof Packet50PreChunk) {
			return handle50Packet((Packet50PreChunk)packet,player);
		} else if(packet instanceof Packet51MapChunk) {
			return handle51Packet((Packet51MapChunk)packet,player);
		} else if(packet instanceof Packet103SetSlot) {
			return handle103Packet((Packet103SetSlot)packet,player);
		} else if(packet instanceof Packet104WindowItems) {
			return handle104Packet((Packet104WindowItems)packet,player);
		}
		ModInfo info = MultiModWorld.getInstance().getModList().get(packet.getClass());
		if(info != null) {
			return MMWPlayer.getMMWPlayer(player).knowsmod(info);
		}
		return true;
	}

	private boolean handle104Packet(Packet104WindowItems packet, Player player) {
		if(packet.b == null) return true;
		for(int i=0;i < packet.b.length; i++) {
			if(packet.b[i] != null) {
				if(!MMWPlayer.getMMWPlayer(player).knowsId(packet.b[i].id)) {
					packet.b[i] = null;
					MMWPlayer.getMMWPlayer(player).cleanUnknownItem();
					player.sendMessage(ColorHandler.RED.toString() + LanguageHandler.Item_Removed_Crach);
				}
			}
		}
		return true;
	}

	private boolean handle103Packet(Packet103SetSlot packet, Player player) {
		ItemStack item = packet.c;
		if(item != null) {
			if(!MMWPlayer.getMMWPlayer(player).knowsId(item.id)) {
				packet.c = null;
				MMWPlayer.getMMWPlayer(player).cleanUnknownItem();
				player.sendMessage(ColorHandler.RED.toString() + LanguageHandler.Item_Removed_Crach);
			}
		}
		return true;
	}

	private boolean handle250Packet(Packet250CustomPayload packet, Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean handle132Packet(Packet132TileEntityData packet, Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean handle53Packet(Packet53BlockChange packet, Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean handle50Packet(Packet50PreChunk packet, Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean handle51Packet(Packet51MapChunk packet, Player player) {
		// TODO Auto-generated method stub
		return true;
	}
}
