package de.davboecki.multimodworld.mod;

import net.minecraft.server.BaseModMp;
import net.minecraft.server.Entity;
import net.minecraft.server.Packet;

public class ModInfo extends ModInfoBase {

	public ModInfo(BaseModMp bMod) {
		super(bMod.getName());
		this.bMod = bMod;
		this.Version = bMod.getVersion();
		this.ClientSide = bMod.hasClientSide();
	}

	private BaseModMp bMod;
	protected String Name;
	protected String Version;
	protected boolean ClientSide;
	protected Class<Packet>[] Packets;
	protected Class<Entity>[] Entities;
	protected int[] ids;

	public BaseModMp getBaseMod() {
		return bMod;
	}
	
	public String getVersion() {
		return Version;
	}
	public boolean isClientSide() {
		return ClientSide;
	}
	public Class<Packet>[] getPackets() {
		return Packets;
	}
	public Class<Entity>[] getEntities() {
		return Entities;
	}
	public int[] getIds() {
		return ids;
	}
	
	public void setPackets(Class<Packet>[] packets) {
		if(this.Packets == null)
			this.Packets = packets;
	}
	
	public void setEntities(Class<Entity>[] entities) {
		if(this.Entities == null)
			this.Entities = entities;
	}
	
	public void setIds(int[] ids) {
		if(ids == null)
			this.ids = ids;
	}
	
	public boolean addedPackets() {
		return this.Packets != null;
	}
	
	public boolean addedEntities() {
		return this.Entities != null;
	}
	
	public boolean addedIds() {
		return ids != null;
	}
	
	public boolean equals(String pName) {
		return pName.equals(this.getBaseMod().toString());
	}

	public boolean equalsWithOtherVersion(String pName) {
		return pName.startsWith(this.getName());
	}
	
	public String toString() {
		return this.getName()+" "+this.getVersion();
	}
}
