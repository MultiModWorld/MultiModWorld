package de.davboecki.multimodworld.settings.cache;

import cpw.mods.fml.common.ModContainer;
import de.davboecki.multimodworld.mod.ModInfo;

public class QueueNode {

	public QueueNode(int x, int z, ModInfo info, boolean flag) {
		this.x = x;
		this.z = z;
		this.info = info;
		this.flag = flag;
	}

	public QueueNode(int x, int z, ModContainer container, boolean flag) {
		this.x = x;
		this.z = z;
		this.container = container;
		this.flag = flag;
	}
	
	int x;
	int z;
	ModInfo info;
	ModContainer container;
	boolean flag;
}