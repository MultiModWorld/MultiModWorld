package de.davboecki.multimodworld.handler;

import org.bukkit.entity.Player;

public enum Permission {
	Admin("admin.*"),
	Player("player.*",Admin),
	ChangeLobby("admin.changelobby",Admin),
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
	AccessUnownedRoom("admin.accessunowndedroom",Admin);
	
	String permissionnode = "";
	Permission[] supernode = null;
	Permission(String node) {
		permissionnode = "multimodworld."+node;
	}
	
	Permission(String node, Permission... superperm /* Permission in included in these permissions*/) {
		permissionnode = "multimodworld."+node;
		supernode = superperm;
	}
	
	public String getNode() {
		return permissionnode;
	}
	
	public Permission[] getSuperNodes() {
		return supernode;
	}
	
	private boolean hasSuperPermission(Player player) {
		if(supernode == null) return false;
		for(Permission node:supernode) {
			if(node.hasPermission(player)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPermission(Player player) {
		return player.hasPermission(permissionnode) || hasSuperPermission(player) || player.isOp();
	}
}
