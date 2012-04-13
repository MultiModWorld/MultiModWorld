package de.davboecki.multimodworld.mod;

import java.util.ArrayList;

import de.davboecki.multimodworld.api.ModChecker;
import de.davboecki.multimodworld.api.plugin.ModAddList;

import net.minecraft.server.BaseMod;
import net.minecraft.server.BaseModMp;
import net.minecraft.server.Entity;
import net.minecraft.server.Packet;

public class ModList extends ArrayList<ModInfo> {
	
	public void load() throws Exception {
		ArrayList<BaseMod> ModList = ModChecker.getModList();
		for(BaseMod bMod:ModList) {
			if(bMod instanceof BaseModMp) {
				if(!contains((BaseModMp)bMod)) {
					this.add(new ModInfo((BaseModMp) bMod));
				}
			}
		}
		ArrayList<ModAddList> List = ModChecker.getAddedBlockList();
		for(ModAddList Moddata:List) {
			if(Moddata.getMod() instanceof BaseModMp) {
				if(contains(Moddata.getMod())) {
					ModInfo info = this.get((BaseModMp) Moddata.getMod());
					if(Moddata.getIds() != null && Moddata.getIds().length > 0) {
						info.setIds(Moddata.getIds());
					}
					if(Moddata.getEntitis() != null && Moddata.getEntitis().length > 0) {
						info.setEntities(Moddata.getEntitis());
					}
					if(Moddata.getPackets() != null && Moddata.getPackets().length > 0) {
						info.setPackets(Moddata.getPackets());
					}
				} else {
					throw new Exception("Mod added Blocks/Entities/Packets but he was not loaded.");
				}
			}
		}
	}
	
	public boolean contains(BaseModMp bMod) {
		for(ModInfo Moddata:this) {
			if(bMod.equals(Moddata.getBaseMod())) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(String Name) {
		for(ModInfo Moddata:this) {
			if(Name.equals(Moddata.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(int id) {
		for(ModInfo Moddata:this) {
			if(Moddata.addedIds()) {
				for(int localid:Moddata.getIds()) {
					if(localid == id) {
						return true;
					}
				}
			}
		}
		return false;
	}
	

	public boolean contains(Class<?> Class) {
		for(ModInfo Moddata:this) {
			if(Class.isAssignableFrom(Entity.class)) {
				if(Moddata.addedEntities()) {
					for(Class<Entity> localclass:Moddata.getEntities()) {
						if(localclass.equals(Class)) {
							return true;
						}
					}
				}
			} else if(Class.isAssignableFrom(Packet.class)) {
				if(Moddata.addedPackets()) {
					for(Class<Packet> localpacket:Moddata.getPackets()) {
						if(localpacket.equals(Class)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public ModInfo get(BaseModMp bMod) {
		for(ModInfo Moddata:this) {
			if(bMod.equals(Moddata.getBaseMod())) {
				return Moddata;
			}
		}
		return null;
	}

	public ModInfo get(String Name) {
		for(ModInfo Moddata:this) {
			if(Name.equals(Moddata.getName())) {
				return Moddata;
			}
		}
		return null;
	}

	public ModInfo get(int id) {
		for(ModInfo Moddata:this) {
			if(Moddata.addedIds()) {
				for(int localid:Moddata.getIds()) {
					if(localid == id) {
						return Moddata;
					}
				}
			}
		}
		return null;
	}

	public ModInfo get(Class<?> Class) {
		for(ModInfo Moddata:this) {
			if(Class.isAssignableFrom(Entity.class)) {
				if(Moddata.addedEntities()) {
					for(Class<Entity> localclass:Moddata.getEntities()) {
						if(localclass.equals(Class)) {
							return Moddata;
						}
					}
				}
			} else if(Class.isAssignableFrom(Packet.class)) {
				if(Moddata.addedPackets()) {
					for(Class<Packet> localpacket:Moddata.getPackets()) {
						if(localpacket.equals(Class)) {
							return Moddata;
						}
					}
				}
			}
		}
		return null;
	}
	
	public ModInfo[] toArray() {
		return (ModInfo[])super.toArray();
	}
	
	public ModInfo[] getByClassName(String[] modnames) {
		for(ModInfo ModInfo:this) {
			System.out.print(ModInfo.getBaseMod().toString());
		}
		return null;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3165623826644968390L;

}
