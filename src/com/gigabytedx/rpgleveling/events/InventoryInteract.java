package com.gigabytedx.rpgleveling.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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
		System.out.println("SLOT IS THIS: " + event.getRawSlot());
		System.out.println("OTHER SLOT IS THIS: " + event.getSlot());
		System.out.println("TOP SIZE:  " + event.getView().getTopInventory().getSize());
		try {
			if (event.getCursor().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")
					&& event.getRawSlot() < event.getView().getTopInventory().getSize()) {
				event.setCancelled(true);
			}
		} catch (NullPointerException e) {
			System.out.println("NULL MATE");
		}
		try {
			if (event.isRightClick() || event.isShiftClick()) {
				if (event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")) {
					event.setCancelled(true);
				}
			}
		} catch (NullPointerException e) {
			event.setCancelled(true);
		}
		try {
			for (Cooldown coolDown : plugin.playerCooldowns.getCooldownMap().get(event.getWhoClicked())) {
				if ((ChatColor.BLUE + coolDown.getItem().getName())
						.equals(event.getWhoClicked().getItemInHand().getItemMeta().getDisplayName()))
					event.setCancelled(true);
			}
		} catch (NullPointerException e) {

		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		try {
			if (event.getNewItems().get(0).getItemMeta().getDisplayName().contains(ChatColor.BLUE + "")) {
				if (Main.itemMap.get(event.getNewItems().get(0).getItemMeta().getDisplayName()) instanceof PotionItem) {
					PotionItem item = (PotionItem) Main.itemMap
							.get(event.getNewItems().get(0).getItemMeta().getDisplayName());
					if (event.getNewItems().size() != item.getQty())
						event.setCancelled(true);
				}
			}
		} catch (NullPointerException e) {
			event.setCancelled(true);
		}
		try {
			for (Cooldown coolDown : plugin.playerCooldowns.getCooldownMap().get(event.getWhoClicked())) {
				if ((ChatColor.BLUE + coolDown.getItem().getName())
						.equals(event.getNewItems().get(0).getItemMeta().getDisplayName()))
					event.setCancelled(true);
			}
		} catch (NullPointerException e) {

		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		System.out.println("RUNNING");
		try {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				if (Main.itemMap
						.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()) instanceof PotionItem) {
					try {
						for (Cooldown coolDown : plugin.playerCooldowns.getCooldownMap().get(event.getPlayer())) {
							if ((ChatColor.BLUE + coolDown.getItem().getName())
									.equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()))
								return;
						}
					} catch (NullPointerException e) {
						System.out.println("no case");
					}
					PotionItem item = (PotionItem) Main.itemMap
							.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
					plugin.playerCooldowns.addCooldown(event.getPlayer(), new Cooldown(item.getCooldown(), item,
							event.getPlayer().getInventory().getHeldItemSlot(), event.getPlayer(), plugin));
				}
		} catch (NullPointerException e) {
		}
	}

	@EventHandler
	public void dropItemEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

}
