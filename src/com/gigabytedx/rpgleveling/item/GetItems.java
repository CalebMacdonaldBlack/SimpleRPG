package com.gigabytedx.rpgleveling.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.modifiers.Modifier;

public class GetItems {
	private List<Item> items;
	Main main;

	public GetItems(Main main) {
		this.main = main;
		items = new ArrayList<>();
		getItemsFromConfig(main);
	}

	@SuppressWarnings("unchecked")
	private void getItemsFromConfig(Main main) {
		// this gets all the Item configuration section key names and adds them
		// to a list
		Set<String> itemConfigSectionNames = main.getConfig().getConfigurationSection("Items").getKeys(false);

		// iterate through skill names
		for (String itemName : itemConfigSectionNames) {
			ConfigurationSection itemConfSection = main.getConfig().getConfigurationSection("Items")
					.getConfigurationSection(itemName);
			String lore = itemConfSection.getString("Lore");
			Material type = Material.getMaterial(itemConfSection.getString("Type"));
			double cost = itemConfSection.getDouble("Cost");
			boolean enchanted = itemConfSection.getBoolean("Enchanted");
			String location = itemConfSection.getString("Location");
			List<String> buffNames = (List<String>) itemConfSection.getList("Buffs");
			List<Modifier> buffs = new ArrayList<>();
			List<String> debuffNames = (List<String>) itemConfSection.getList("Debuffs");
			List<Modifier> debuffs = new ArrayList<>();
			int damage = itemConfSection.getInt("Damage");
			int protection = itemConfSection.getInt("Protection");
			try {

				for (String buffName : buffNames) {
					try {

						buffs.add(Main.buffsMap.get(buffName));
					} catch (NullPointerException e) {
						// e.printStackTrace();
					}
				}
			} catch (NullPointerException e) {
				System.out.println(itemName + " does not have any buffs");
			}
			try {
				for (String debuffName : debuffNames) {
					try {
						debuffs.add(Main.debuffsMap.get(debuffName));
					} catch (NullPointerException e) {
						// e.printStackTrace();
					}
				}
			} catch (NullPointerException e) {
				System.out.println(itemName + " does not have any buffs");
			}
			// add new skill to list
			items.add(new Item(itemName, lore, cost, type, enchanted, buffs, debuffs, location, damage, protection));
			Main.itemMap.put(ChatColor.BLUE + itemName,
					new Item(itemName, lore, cost, type, enchanted, buffs, debuffs, location, damage, protection));
		}

	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
