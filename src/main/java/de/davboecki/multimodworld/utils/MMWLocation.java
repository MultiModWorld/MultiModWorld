package de.davboecki.multimodworld.utils;

import java.util.HashMap;

import org.bukkit.Location;

import de.davboecki.multimodworld.settings.Hashmapable;

public class MMWLocation implements Hashmapable {
	
	private double x;
	private double y;
	private double z;
	
	public double getX() {
		return x;
	}

	public int getIntX() {
		return (int) x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public int getIntY() {
		return (int) y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public int getIntZ() {
		return (int) z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	public MMWLocation moved(MMWLocation movefor) {
		MMWLocation loc = new MMWLocation(this);
		loc.x += movefor.x;
		loc.y += movefor.y;
		loc.z += movefor.z;
		return loc;
	}
	
	public Location getBukkitLocation(MMWWorld world) {
		return new Location(world.getWorld(),x,y,z);
	}
	
	public MMWLocation(int i, int j, int k) {
		x = i;
		y = j;
		z = k;
	}

	public MMWLocation(double i, double j, double k) {
		x = i;
		y = j;
		z = k;
	}

	public MMWLocation(HashMap<String, Object> map) {
		fromHashMap(map);
	}

	public MMWLocation(MMWLocation mmwLocation) {
		x = mmwLocation.x;
		y = mmwLocation.y;
		z = mmwLocation.z;
	}
	public MMWLocation(Location location) {
		x = location.getX();
		y = location.getY();
		z = location.getZ();
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("x",x);
		map.put("y",y);
		map.put("z",z);
		return map;
	}

	public void fromHashMap(HashMap<String, Object> map) {
		x = (Double) map.get("x");
		y = (Double) map.get("y");
		z = (Double) map.get("z");
	}

	//Base Functions
	@Override
	public boolean equals(Object object) {
		if(object == null) return false;
		if(!(object instanceof MMWLocation)) return false;
		MMWLocation mmwlocation = (MMWLocation)object;
		return mmwlocation.x == this.x && mmwlocation.y == this.y && mmwlocation.z == this.z;
	}
}

