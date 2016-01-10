package com.gigabytedx.rpgleveling.events;

import org.bukkit.event.Listener;

import com.gigabytedx.rpgleveling.Main;

public class PlayerRespawn implements Listener {

	@SuppressWarnings("unused")
	private Main plugin;

	public PlayerRespawn(Main plugin) {
		this.plugin = plugin;
	}

//	@EventHandler
//	public void onRespawn(PlayerRespawnEvent event) {
//		final Player player = event.getPlayer();
//		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//
//			@Override
//			public void run() {
//				GetBuffs.applyUnlockedModifiers(player, plugin);
//
//			}
//		}, 20);
//
//	}
}
