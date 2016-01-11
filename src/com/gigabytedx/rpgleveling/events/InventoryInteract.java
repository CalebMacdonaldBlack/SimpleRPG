package com.gigabytedx.rpgleveling.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.cooldowns.Cooldown;
import com.gigabytedx.rpgleveling.item.PotionItem;

public class InventoryInteract implements Listener {
	Main plugin;

	public InventoryInteract(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {

	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		System.out.println("RUNNING");
		try{
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			if (Main.itemMap
					.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()) instanceof PotionItem) {
				PotionItem item = (PotionItem) Main.itemMap
						.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
					plugin.playerCooldowns.addCooldown(event.getPlayer(), new Cooldown(item.getCooldown(), item,
							event.getPlayer().getInventory().getHeldItemSlot(), event.getPlayer(), plugin));
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}

}
