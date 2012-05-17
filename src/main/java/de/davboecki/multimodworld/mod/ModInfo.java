package de.davboecki.multimodworld.mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.ModContainer;
import forge.NetworkMod;

import net.minecraft.server.CraftingRecipe;
import net.minecraft.server.Entity;
import net.minecraft.server.Packet;

public class ModInfo extends ModInfoBase {

	public ModInfo(ModContainer bMod) {
		super(bMod.getName());
		this.bMod = bMod;
		if(bMod.getMod() instanceof NetworkMod) {
			NetworkMod nMod = (NetworkMod)bMod.getMod();
			this.Version = nMod.getVersion();
			this.ClientSide = nMod.clientSideRequired();
		} else {
			this.ClientSide = true;
		}
	}

	private ModContainer bMod;
	protected String Name;
	protected String Version = null;
	protected boolean ClientSide = true;
	@SuppressWarnings("unchecked")
	protected Class<Packet>[] Packets = new Class[0];
	@SuppressWarnings("unchecked")
	protected Class<Entity>[] Entities = new Class[0];
	protected int[] ids = new int[0];
	protected CraftingRecipe[] addedRecipies;
	protected CraftingRecipe[] removedRecipies;

	public ModContainer getModContainer() {
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
	
	public CraftingRecipe[] getAddedRecipies() {
		return addedRecipies;
	}

	public CraftingRecipe[] getRemovedRecipies() {
		return removedRecipies;
	}

	@SuppressWarnings("unchecked")
	public void addPackets(Class<Packet>[] packets) {
		this.Packets = merge(this.Packets,packets);
	}
	
	@SuppressWarnings("unchecked")
	public void addEntities(Class<Entity>[] entities) {
		this.Entities = merge(this.Entities,entities);
	}

	public void addIds(int[] ids) {
		this.ids = merge(this.ids,ids);
	}

	public void addAddedRecipies(CraftingRecipe[] addedRecipies) {
		this.addedRecipies = merge(this.addedRecipies,addedRecipies);
	}

	public void addRemovedRecipies(CraftingRecipe[] removedRecipies) {
		this.removedRecipies = merge(this.removedRecipies,removedRecipies);
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
		return pName.equals(this.getModContainer().getMod().toString());
	}

	public boolean equalsWithOtherVersion(String pName) {
		return pName.startsWith(this.getName());
	}
	
	public String toString() {
		return this.getName()+" "+this.getVersion();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int[] merge(int[]... arrays) {
		List list = new ArrayList();
		
		for( int[] array : arrays )
		list.addAll( Arrays.asList( array ) );
		
		int[] content = new int[list.size()];
		int count = 0;
		for(Object Object:list) {
			if(Object instanceof Integer) {
				content[count++] = (Integer)Object;
			}
		}
		return content;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Class[] merge(Class[]... arrays) {
		List list = new ArrayList();
		
		for( Class[] array : arrays )
		list.addAll( Arrays.asList( array ) );
		
		Class[] content = new Class[list.size()];
		int count = 0;
		for(Object Object:list) {
			if(Object instanceof Integer) {
				content[count++] = (Class)Object;
			}
		}
		return content;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static CraftingRecipe[] merge(CraftingRecipe[]... arrays) {
		List list = new ArrayList();
		
		for( CraftingRecipe[] array : arrays )
		list.addAll( Arrays.asList( array ) );
		
		CraftingRecipe[] content = new CraftingRecipe[list.size()];
		int count = 0;
		for(Object Object:list) {
			if(Object instanceof Integer) {
				content[count++] = (CraftingRecipe)Object;
			}
		}
		return content;
	}
}
