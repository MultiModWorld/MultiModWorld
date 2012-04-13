package de.davboecki.multimodworld.mod;

public class ClientMod extends ModInfoBase {
	
	public ClientMod(String Name) {
		super(Name);
		if(Name.startsWith("mod_")) {
			String tmpName = Name.substring(0,Name.indexOf(" "));
			this.Version = Name.substring(Name.indexOf(" "));
			this.Name = tmpName;
		}
	}
	
	protected String Version;
}
