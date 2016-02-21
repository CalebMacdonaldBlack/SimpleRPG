package com.gigabytedx.rpgleveling;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gigabytedx.rpgleveling.Mobs.GetMobData;
import com.gigabytedx.rpgleveling.cooldowns.PlayerCooldowns;
import com.gigabytedx.rpgleveling.events.BlockPlace;
import com.gigabytedx.rpgleveling.events.EnitityDeath;
import com.gigabytedx.rpgleveling.events.EntitySpawn;
import com.gigabytedx.rpgleveling.events.Interact;
import com.gigabytedx.rpgleveling.events.InventoryInteract;
import com.gigabytedx.rpgleveling.events.Join;
import com.gigabytedx.rpgleveling.events.PlayerRespawn;
import com.gigabytedx.rpgleveling.events.Potions;
import com.gigabytedx.rpgleveling.item.GetItems;
import com.gigabytedx.rpgleveling.item.Item;
import com.gigabytedx.rpgleveling.item.ItemClassValue;
import com.gigabytedx.rpgleveling.locations.GetLocations;
import com.gigabytedx.rpgleveling.locations.Regions;
import com.gigabytedx.rpgleveling.modifiers.GetBuffs;
import com.gigabytedx.rpgleveling.modifiers.Modifier;
import com.gigabytedx.rpgleveling.player.ActiveModifiers;

import commands.GetClass;
import commands.GetXP;
import commands.OpenShop;
import commands.SetWalk;
import commands.ViewItems;

public class Main extends JavaPlugin {
	public GetLocations locations;
	public static GetBuffs buffs;
	public Regions regions = new Regions(this);
	public GetItems items;
	public static Map<String, Item> itemMap = new HashMap<>();
	public static Map<String, Modifier> buffsMap = new HashMap<>();
	public static Map<String, Modifier> debuffsMap = new HashMap<>();
	public static Map<UUID, ActiveModifiers> activeModifiers = new HashMap<>();
	public int loreLength = 6;
	public PlayerCooldowns playerCooldowns = new PlayerCooldowns();
	public ItemClassValue itemClassValue;

	public File MobSpawningDataFile = new File(getDataFolder() + "/Data/MobSpawningData.yml");
	public FileConfiguration MobSpawningData = YamlConfiguration.loadConfiguration(MobSpawningDataFile);

	public File playerFoundItemsFile = new File(getDataFolder() + "/Data/playerDataFile.yml");
	public FileConfiguration playerFoundItemsConfig = YamlConfiguration.loadConfiguration(MobSpawningDataFile);
	
	public File chestRollFile = new File(getDataFolder() + "/Data/chestRollFile.yml");
	public FileConfiguration chestRollConfig = YamlConfiguration.loadConfiguration(chestRollFile);

	public static final int goldBarWorth = 10;
	public static final int goldNuggetWorth = 1;

	private Logger logger;

	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();
		registerCommands();
		registerEvents();
		registerConfig();
		logger.info(pdfFile.getName() + " has been enabled (V." + pdfFile.getVersion() + ")");
		itemClassValue = new ItemClassValue(this);
		loadFiles(MobSpawningDataFile, MobSpawningData);
		loadFiles(playerFoundItemsFile, playerFoundItemsConfig);
		loadFiles(chestRollFile, chestRollConfig);
		new GetMobData(this);

	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		logger = getLogger();

		logger.info(pdfFile.getName() + " has been disabled (V." + pdfFile.getVersion() + ")");
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Interact(this), this);
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new EnitityDeath(this), this);
		pm.registerEvents(new PlayerRespawn(this), this);
		pm.registerEvents(new EntitySpawn(this), this);
		pm.registerEvents(new InventoryInteract(this), this);
		pm.registerEvents(new Potions(this), this);
		pm.registerEvents(new BlockPlace(this), this);
	}

	private void registerCommands() {
		getCommand("viewitems").setExecutor(new ViewItems(this));
		getCommand("getxp").setExecutor(new GetXP(this));
		getCommand("openshop").setExecutor(new OpenShop(this));
		getCommand("setwalk").setExecutor(new SetWalk(this));
		getCommand("getclass").setExecutor(new GetClass(this));
	}

	private void registerConfig() {
		saveDefaultConfig();

		// get skills from config
		locations = new GetLocations(this);
		buffs = new GetBuffs(this);
		items = new GetItems(this);
	}

	public void saveCustomConfig(File file, FileConfiguration fileConfig) {
		try {
			fileConfig.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFiles(File file, FileConfiguration fileConfig) {
		if (file.exists()) {
			try {
				fileConfig.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fileConfig.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void logError(String msg) {
		getLogger().severe(msg);
	}

	public void logDebug(String msg) {
		if (getConfig().getBoolean("enableDebug")) {
			getLogger().info("[Debug] " + msg);
		}
	}
}
