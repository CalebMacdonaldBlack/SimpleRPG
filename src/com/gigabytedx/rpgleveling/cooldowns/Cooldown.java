package com.gigabytedx.rpgleveling.cooldowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.item.AddItemToInventory;
import com.gigabytedx.rpgleveling.item.Item;

public class Cooldown {
	int duration;
	Item item;
	int itemSlot;
	Player player;
	Main plugin;
	Cooldown cooldown;
	int schedularId;

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
		schedularId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			int count = duration;

			@Override
			public void run() {
				count--;

				ItemStack is = new ItemStack(Material.BARRIER);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(ChatColor.GOLD + "Cooldown: " + ChatColor.DARK_RED + count + ChatColor.BLUE + " - "
						+ item.getName());
				is.setItemMeta(im);
				player.getInventory().setItem(itemSlot, is);

				if (count == 0) {
					finish();
					Bukkit.getScheduler().cancelTask(schedularId);
				}
			}

			private void finish() {
				player.getInventory().setItem(itemSlot, AddItemToInventory.getItemStack(item, plugin));

				plugin.playerCooldowns.getCooldownMap().get(player).remove(cooldown);
			}
		}, 0, 20);
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
