package com.gigabytedx.rpgleveling.item;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gigabytedx.rpgleveling.Main;

public class ItemClassValue {
	Main plugin;

	public ItemClassValue(Main plugin) {
		this.plugin = plugin;
	}

	public Map<String, Integer> getBaseClassValues(Player player) {
		Map<String, Integer> classValues = new HashMap<>();
		String className;

		for (String skillName : plugin.getConfig().getConfigurationSection("skills").getKeys(false)) {
			classValues.put(skillName, 0);
		}

		for (ItemStack is : player.getInventory().getArmorContents()) {
			try {
				className = Main.itemMap.get(is.getItemMeta().getDisplayName()).getBaseClass();
				classValues.put(className, classValues.get(className) + 1);
			} catch (NullPointerException e) {

			}
		}
		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			try {
				className = Main.itemMap.get(player.getInventory().getItem(itemSlot).getItemMeta().getDisplayName())
						.getBaseClass();
				classValues.put(className, classValues.get(className) + 1);
			} catch (NullPointerException e) {

			}
		}

		return classValues;

	}
}
