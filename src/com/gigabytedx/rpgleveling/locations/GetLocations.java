package com.gigabytedx.rpgleveling.locations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gigabytedx.rpgleveling.Main;

public class GetLocations {
	Main main;
	List<String> locationNames = new ArrayList<>();

	public GetLocations(Main main) {
		this.main = main;
		getLocationsFromConfig(main);
	}

	private void getLocationsFromConfig(Main main) {
		// this gets all the skill configuration section key names and adds them
		// to a list
		Set<String> locationConfigSectionNames = main.getConfig().getConfigurationSection("Locations").getKeys(false);
		for(String locationName: locationConfigSectionNames){
			locationNames.add(locationName.toLowerCase());
		}
		System.out.println("LOCATIONS  " + locationNames.toString());
	}

	public List<String> getLocationNames() {
		return locationNames;
	}
	
}