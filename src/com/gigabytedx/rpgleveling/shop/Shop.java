package com.gigabytedx.rpgleveling.shop;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.item.AddItemToInventory;
import com.gigabytedx.rpgleveling.item.Item;

public class Shop {
	public static void openShop(Main plugin, Player player, String location){
		List<Item> items = plugin.items.getItems();
		player.sendMessage("size of items is: " + items.size());
		Inventory inv = Bukkit.createInventory(player, 27,
				ChatColor.DARK_BLUE + "Shop: " + ChatColor.DARK_BLUE + location);

		for (Item item : items) {
			try {
				if (plugin.playerFoundItemsConfig.getConfigurationSection("players." + player.getUniqueId()).getList("found items").contains(item.getName())
						&& item.getLocationName().toLowerCase().equals(location.toLowerCase())) {
					inv = AddItemToInventory.addItem(inv, item, plugin, true);
				}
			} catch (NullPointerException e) {
				System.out.println("Doesnt have a location name");
			}
		}
		for (Item item : items) {
			try {
				if (!(plugin.playerFoundItemsConfig.getConfigurationSection("players." + player.getUniqueId()).getList("found items").contains(item.getName()))
						&& item.getLocationName().toLowerCase().equals(location.toLowerCase())) {
					inv = AddItemToInventory.addItem(inv, null, plugin, false);
				}
			} catch (NullPointerException e) {
				System.out.println("Doesnt have a location name");
			}
		}
		player.openInventory(inv);
	}
}
