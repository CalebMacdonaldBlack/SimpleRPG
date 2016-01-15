package com.gigabytedx.rpgleveling.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.gigabytedx.rpgleveling.Main;

public class BlockPlace implements Listener {

	Main plugin;

	public BlockPlace(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().isOp() && event.getBlock().getType().equals(Material.BARRIER)) {
			if (plugin.getConfig().getString("world name").equals(event.getBlock().getLocation().getWorld().getName()))
				event.setCancelled(true);
		}
	}
}
