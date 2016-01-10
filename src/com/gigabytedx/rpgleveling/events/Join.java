package com.gigabytedx.rpgleveling.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gigabytedx.rpgleveling.Main;

public class Join implements Listener {

	private Main plugin;

	public Join(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();

		// initialize player with 0 xp if they aren't already saved in the xp
		// file
		try{
			plugin.playerFoundItemsConfig.getConfigurationSection("players")
					.createSection(player.getUniqueId().toString());
			plugin.playerFoundItemsConfig.getConfigurationSection("players." + player.getUniqueId().toString())
					.createSection("found items");

			plugin.saveCustomConfig(plugin.playerFoundItemsFile, plugin.playerFoundItemsConfig);
		}catch(NullPointerException e){
			plugin.playerFoundItemsConfig.createSection("players." + player.getUniqueId() + ".found items");
			plugin.saveCustomConfig(plugin.playerFoundItemsFile, plugin.playerFoundItemsConfig);
		}
	}
}
