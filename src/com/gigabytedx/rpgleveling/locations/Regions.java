package com.gigabytedx.rpgleveling.locations;

import java.util.HashMap;
import java.util.Map;

import com.gigabytedx.rpgleveling.Main;

public class Regions {
	Map<String, Region> regions = new HashMap<>();
	Main plugin;

	public Regions(Main plugin) {
		this.plugin = plugin;
	}

	public Map<String, Region> getRegions() {
		return regions;
	}

	public void addRegion(String regionName, Region region) {
		regions.put(regionName.toLowerCase(), region);
	}

	public Region getRegion(String regionName) {
		return regions.get(regionName);
	}
	
	public boolean hasRegion(String regionName) {
		//System.out.println("RGIONS VALUES: " + regions.keySet().toString());
		return regions.containsKey(regionName);
	}

}
