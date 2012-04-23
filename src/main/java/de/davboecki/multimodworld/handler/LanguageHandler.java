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
	//Dummy(""),
	//Dummy(""),
	//Dummy(""),
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
