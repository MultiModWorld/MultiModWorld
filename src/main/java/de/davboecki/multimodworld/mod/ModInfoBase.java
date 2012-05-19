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
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ModInfoBase)) return false;
		return ((ModInfoBase)o).Name.equalsIgnoreCase(Name);
	}
}
