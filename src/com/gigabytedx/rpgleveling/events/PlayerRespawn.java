package com.gigabytedx.rpgleveling.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gigabytedx.rpgleveling.Main;

public class PlayerRespawn implements Listener {

	private Main plugin;

	public PlayerRespawn(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = (Player) event.getPlayer();

		// initialize player with 0 xp if they aren't already saved in the xp
		// file
		try {
			plugin.playerFoundItemsConfig.getConfigurationSection("players")
					.createSection(player.getUniqueId().toString());
			plugin.playerFoundItemsConfig.getConfigurationSection("players." + player.getUniqueId().toString())
					.createSection("found items");

			plugin.saveCustomConfig(plugin.playerFoundItemsFile, plugin.playerFoundItemsConfig);
		} catch (NullPointerException e) {
			plugin.playerFoundItemsConfig.createSection("players." + player.getUniqueId() + ".found items");
			plugin.saveCustomConfig(plugin.playerFoundItemsFile, plugin.playerFoundItemsConfig);
		}

		ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Locked ability slot");
		itemStack.setItemMeta(meta);
		for (int itemSlot = 3; itemSlot < 9; itemSlot++) {
			event.getPlayer().getInventory().setItem(itemSlot, itemStack);
		}
	}
}
