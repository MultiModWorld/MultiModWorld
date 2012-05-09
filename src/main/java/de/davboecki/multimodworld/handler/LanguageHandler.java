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
	Room_Generated("A PrivateRoom has been generated for you. You can now teleport into that room."),
	RoomSign_Normal_Line1(""),
	RoomSign_Normal_Line2("Normal"),
	RoomSign_Normal_Line3("Portal Room"),
	RoomSign_Normal_Line4(""),
	RoomSign_Other_Line1(""),
	RoomSign_Other_Line2("Other"),
	RoomSign_Other_Line3("Portal Room"),
	RoomSign_Other_Line4(""),
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
