package de.davboecki.multimodworld.utils;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.block.Block;

import de.davboecki.multimodworld.constant.Minecraftconstants;
import de.davboecki.multimodworld.settings.Hashmapable;

public class MMWRegion implements Hashmapable {

	MMWLocation upperCorner = new MMWLocation(0,0,0);
	MMWLocation lowerCorner = new MMWLocation(0,0,0);
	MMWWorld world;
	
	public MMWRegion(){}
	
	public MMWRegion(MMWLocation cornera, MMWLocation cornerb, MMWWorld world) {
		if(cornera.getY() >= cornerb.getY()) {
			this.upperCorner = cornera;
			this.lowerCorner = cornerb;
		} else {
			this.upperCorner = cornerb;
			this.lowerCorner = cornera;
		}
		this.world = world;
	}
	
	public MMWRegion(HashMap<String, Object> hashMap) {
		this.fromHashMap(hashMap);
	}

	public boolean LocationinRegion(MMWLocation location) {
		return between(location.getX(),upperCorner.getX(),lowerCorner.getX()) && between(location.getY(),upperCorner.getY(),lowerCorner.getY()) && between(location.getZ(),upperCorner.getZ(),lowerCorner.getZ());
	}
	
	public boolean[] getContainedBlocksBooleanArray() {
		boolean[] contains = new boolean[Minecraftconstants.blockidcount];
		for(int x = getLowerIntX(); x < getHigherIntX(); x++) {
			for(int y = getLowerIntY(); y < getHigherIntY(); y++) {
				for(int z = getLowerIntZ(); z < getHigherIntZ(); z++) {
					Block block = world.getWorld().getBlockAt(x, y, z);
					if(block != null && block.getTypeId() != 0) {
						contains[block.getTypeId()] = true;
					}
				}
			}
		}
		return contains;
	}
	
	public int[] getContainedBlocks() {
		boolean[] contains = getContainedBlocksBooleanArray();
		int[] ids = new int[0];
		for(int i=0; i<contains.length;i++) {
			if(contains[i]) {
				int[] oldids = ids;
				ids = new int[ids.length + 1];
				ids = Arrays.copyOf(oldids, ids.length);
				ids[ids.length - 1] = i;
			}
		}
		return ids;
	}

	public boolean containsBlock(int id) {
		return id>0 && id<Minecraftconstants.blockidcount && getContainedBlocksBooleanArray()[id];
	}
	
	private boolean between(double value, double var1, double var2) {
		if(var1 > var2) {
			return value > var2 && value < var1;
		} else if(var1 < var2) {
			return value < var2 && value > var1;
		} else { //var1 == var2
			return value == var1;
		}
	}

	public double getLowerX() {
		return upperCorner.getX() > lowerCorner.getX() ? lowerCorner.getX() : upperCorner.getX();
	}

	public double getLowerY() {
		return lowerCorner.getY();
	}

	public double getLowerZ() {
		return upperCorner.getZ() > lowerCorner.getZ() ? lowerCorner.getZ() : upperCorner.getZ();
	}

	public double getHigherX() {
		return upperCorner.getX() > lowerCorner.getX() ? upperCorner.getX() : lowerCorner.getX();
	}

	public double getHigherY() {
		return upperCorner.getY();
	}

	public double getHigherZ() {
		return upperCorner.getZ() > lowerCorner.getZ() ? upperCorner.getZ() : lowerCorner.getZ();
	}

	public int getLowerIntX() {
		return upperCorner.getX() > lowerCorner.getX() ? (int) lowerCorner.getX() : (int) upperCorner.getX();
	}

	public int getLowerIntY() {
		return (int) lowerCorner.getY();
	}

	public int getLowerIntZ() {
		return upperCorner.getZ() > lowerCorner.getZ() ? (int) lowerCorner.getZ() : (int) upperCorner.getZ();
	}

	public int getHigherIntX() {
		return upperCorner.getX() > lowerCorner.getX() ? (int) upperCorner.getX() : (int) lowerCorner.getX();
	}

	public int getHigherIntY() {
		return (int) upperCorner.getY();
	}

	public int getHigherIntZ() {
		return upperCorner.getZ() > lowerCorner.getZ() ? (int) upperCorner.getZ() : (int) lowerCorner.getZ();
	}
	
	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("upperCorner",upperCorner.toHashMap());
		map.put("lowerCorner",lowerCorner.toHashMap());
		map.put("world",world.getWorld().getName());
		return map;
	}

	@SuppressWarnings("unchecked")
	public void fromHashMap(HashMap<String, Object> map) {
		upperCorner = new MMWLocation((HashMap<String, Object>)map.get("upperCorner"));
		lowerCorner = new MMWLocation((HashMap<String, Object>)map.get("lowerCorner"));
		world = MMWWorld.getMMWWorld((String)map.get("world"));
	}
}
