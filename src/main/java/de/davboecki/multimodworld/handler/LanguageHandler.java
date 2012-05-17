package de.davboecki.multimodworld.handler;

public enum LanguageHandler {
	Language("en"),
	ServerMod("ServerMod:"),
	ClientMod("ClientMod:"),
	VersionMod("VerionMod:"),
	Version_Header("Mods with different verion than the server:"),
	None("NONE"),
	Fewarguments("To few arguments"),
	Error("Error"),
	Player_Info_Help("Player Help"),
	NotAplayer("Argument is not a player."),
	Player_Info("Player Info"),
	Player_Info_Position("Position:"),
	Player_Info_World("World:"),
	WorldEdit_Loaded("WorldEdit plugin found."),
	Room_Generated("A PrivateRoom in world %w has been generated for you. You can now teleport into that room."),
	RoomPos_Search_Start("Starting search for free space for a PrivateRoom in world %w."),
	RoomPos_Found("Found free space for a PrivateRoom in world %w."),
	RoomSign_Normal_Line1(""),
	RoomSign_Normal_Line2("Normal"),
	RoomSign_Normal_Line3("Portal Room"),
	RoomSign_Normal_Line4(""),
	RoomSign_Other_Line1(""),
	RoomSign_Other_Line2("Other"),
	RoomSign_Other_Line3("Portal Room"),
	RoomSign_Other_Line4(""),
	Walk_On_Lava_Wall("You are not allowed to move to the other side. Please use the portal to get on the other side."),
	Walk_Underground("You are not allowed to walk so deap into the ground without being in you private room."),
	Please_Use_Portals("Please use the portals to get on the other side of the Lava River."),
	Not_Allowed_Block_Change_Lobby("You are not allowed to change any block inside the lobby."),
	Not_Allowed_Interact_Underground("You are not allowed to interact with any item that is not in your room or in the lobby."),
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
	
	Mod_List("Players Mods");
	
	LanguageHandler(String Content) {
		content = Content;
	}

	public String getContent() {
		return content;
	}
	public String toString() {
		return content;
	}
	private String content;
}
