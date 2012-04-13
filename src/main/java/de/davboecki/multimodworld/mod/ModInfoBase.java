package de.davboecki.multimodworld.mod;

public class ModInfoBase {
	
	protected ModInfoBase(String name) {
		this.Name = name;
	}
	
	protected String Name;

	public String getName() {
		return Name;
	}
	
	public String toString() {
		return this.getName();
	}
}
