package com.gigabytedx.rpgleveling.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.item.AddItemToInventory;
import com.gigabytedx.rpgleveling.item.Item;
import com.gigabytedx.rpgleveling.item.PotionItem;

public class Cooldown{
	int duration;
	Item item;
	int itemSlot;
	Player player;
	Main plugin;
	Cooldown cooldown;

	public Cooldown(int i, Item item, int itemSlot, Player player, Main plugin) {
		this.duration = i;
		this.item = item;
		this.itemSlot = itemSlot;
		this.player = player;
		this.plugin = plugin;
		cooldown = this;
		
		runCooldown();
	}

	private void runCooldown() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				if (item instanceof PotionItem)
					player.getInventory().setItem(itemSlot, ((PotionItem) item).getPotion());
				else {
					player.getInventory().setItem(itemSlot, AddItemToInventory.getItemStack(item, plugin));
				}
				plugin.playerCooldowns.getCooldownMap().get(player).remove(cooldown);
			}
		}, duration * 20);
	}

	public int getDuration() {
		return duration;
	}

	public Item getItem() {
		return item;
	}

	public int getItemSlot() {
		return itemSlot;
	}

	public Player getPlayer() {
		return player;
	}

}
