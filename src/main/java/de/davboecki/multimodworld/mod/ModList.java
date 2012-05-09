package de.davboecki.multimodworld.mod;

import java.util.ArrayList;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;

import de.davboecki.multimodworld.api.ModChecker;
import de.davboecki.multimodworld.api.plugin.ModAddList;
import forge.NetworkMod;

import net.minecraft.server.Entity;
import net.minecraft.server.Packet;

public class ModList extends ArrayList<ModInfo> {
	
	public void load() throws Exception {
		ArrayList<ModContainer> ModList = ModChecker.getModList();
		for(ModContainer bMod:ModList) {
			if(!contains(bMod)) {
				this.add(new ModInfo(bMod));
			}
		}
		ArrayList<ModAddList> List = ModChecker.getAddedBlockList();
		for(ModAddList Moddata:List) {
			if(contains(Moddata.getMod())) {
				ModInfo info = this.get(Moddata.getMod());
				if(Moddata.getIds() != null && Moddata.getIds().length > 0) {
					info.addIds(Moddata.getIds());
				}
				if(Moddata.getEntitis() != null && Moddata.getEntitis().length > 0) {
					info.addEntities(Moddata.getEntitis());
				}
				if(Moddata.getPackets() != null && Moddata.getPackets().length > 0) {
					info.addPackets(Moddata.getPackets());
				}
				if(Moddata.getAddedRecipies() != null && Moddata.getAddedRecipies().length > 0) {
					info.addAddedRecipies(Moddata.getAddedRecipies());
				}
				if(Moddata.getRemovedRecipies() != null && Moddata.getRemovedRecipies().length > 0) {
					info.addRemovedRecipies(Moddata.getRemovedRecipies());
				}
			} else {
				throw new Exception("Mod: "+Moddata.getMod().toString()+" added Blocks/Entities/Packets/Recipies but he was not loaded.");
			}
		}
	}

	public boolean contains(ModContainer cMod) {
		if(cMod.getMod() instanceof NetworkMod) {
			return contains((NetworkMod)cMod.getMod());
		}
		if(cMod.getMod() instanceof Mod) {
			return contains((Mod)cMod.getMod());
		}
		
		return false;
	}

	public boolean contains(NetworkMod bMod) {
		for(ModInfo Moddata:this) {
			if(bMod.equals(Moddata.getModContainer().getMod())) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Mod mMod) {
		for(ModInfo Moddata:this) {
			if(mMod.equals(Moddata.getModContainer().getMod())) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(String Name) {
		for(ModInfo Moddata:this) {
			if(Name.equals(Moddata.getName()) || Name.equals(Moddata.getModContainer().getName())) {
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

	public ModInfo get(ModContainer bMod) {
		for(ModInfo Moddata:this) {
			if(bMod.equals(Moddata.getModContainer())) {
				return Moddata;
			}
		}
		return null;
	}

	public ModInfo get(NetworkMod bMod) {
		for(ModInfo Moddata:this) {
			if(bMod.equals(Moddata.getModContainer().getMod())) {
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

	public ModInfo getById(int id) {
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
	
	/*
	public ModInfo[] getByClassName(String[] modnames) {
		for(ModInfo ModInfo:this) {
			System.out.print(ModInfo.getModContainer().toString());
		}
		return null;
	}
	*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3165623826644968390L;

}
