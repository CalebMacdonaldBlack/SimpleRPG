package com.gigabytedx.rpgleveling.Mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.item.Drop;
import com.gigabytedx.rpgleveling.locations.Region;

public class GetMobData {

	Main plugin;

	public GetMobData(Main plugin) {
		this.plugin = plugin;
		getRegions();
	}

	@SuppressWarnings("unchecked")
	private void getRegions() {
		for (String regionName : plugin.MobSpawningData.getConfigurationSection("Regions").getKeys(false)) {
			List<MobData> regionMobs = new ArrayList<>();
			int maxAllowedToSpawnInRegion = plugin.MobSpawningData
					.getConfigurationSection("Regions." + regionName).getInt("Max");
			for (String mobName : plugin.MobSpawningData
					.getConfigurationSection("Regions." + regionName + ".SpawnableMobs").getKeys(false)) {
				ConfigurationSection mobConf = plugin.MobSpawningData
						.getConfigurationSection("Regions." + regionName + ".SpawnableMobs." + mobName);

				int health = mobConf.getInt("Health");
				boolean naturalAllowed = mobConf.getBoolean("Natural");
				int attack = mobConf.getInt("Attack");
				int level = mobConf.getInt("Level");
				int spawnRate = mobConf.getInt("SpawnRate");
				int xp = mobConf.getInt("Xp");
				String type = mobConf.getString("Type");
				List<String> items = (List<String>) mobConf.getList("Items");
				Set<String> dropNames = plugin.MobSpawningData.getConfigurationSection("Drops").getKeys(false);
				List<Drop> drops = new ArrayList<>();
				for (String dropName : dropNames) {
					drops.add(new Drop(plugin.MobSpawningData.getString("Drops." + dropName + ".Type"),
							plugin.MobSpawningData.getInt("Drops." + dropName + ".Qty"),
							plugin.MobSpawningData.getInt("Drops." + dropName + ".SpawnRate"), dropName,
							plugin.MobSpawningData.getBoolean("Drops." + dropName + ".Custom")));
				}

				regionMobs.add(new MobData(mobName, spawnRate, type, health, attack, level, drops, items, naturalAllowed, xp));
			}
			plugin.regions.addRegion(regionName, new Region(regionName, regionMobs ,plugin, maxAllowedToSpawnInRegion));
		}

	}
}
