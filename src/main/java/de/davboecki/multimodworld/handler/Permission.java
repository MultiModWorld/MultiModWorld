package de.davboecki.multimodworld.handler;

import org.bukkit.entity.Player;

public enum Permission {
	Player("player.*"),
	Admin("admin.*",Player),
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
	Permission supernode = null;
	Permission(String node) {
		permissionnode = "multimodworld."+node;
	}
	
	Permission(String node, Permission superperm) {
		permissionnode = "multimodworld."+node;
		supernode = superperm;
	}
	
	public String getNode() {
		return permissionnode;
	}
	
	public Permission getSuperNode() {
		return supernode;
	}
	
	public boolean hasPermission(Player player) {
		return player.hasPermission(permissionnode) || (supernode != null && supernode.hasPermission(player)) || player.isOp();
	}
}
