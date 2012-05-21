package de.davboecki.multimodworld.craftbukkit;

import org.bukkit.Material;

public class ItemInformation {
	
	private int id;
	public ItemInformation(int id) {
		this.id = id;
	}
	
	public String getName() {
		try {
			String name = net.minecraft.server.Item.byId[id].l();
			if(name == null || name.equals("null.name")) {
				name = net.minecraft.server.Item.byId[id].getName();
			}
			if(name == null || name.equals("null.name") || name.equals("null")) {
				name = Material.getMaterial(id).name();
			}
			return net.minecraft.server.Item.byId[id].l();
		} catch(Exception e) {
			return Material.getMaterial(id).name();
		}
	}
}
