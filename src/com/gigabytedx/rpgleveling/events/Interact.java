package com.gigabytedx.rpgleveling.events;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.item.Item;
import com.gigabytedx.rpgleveling.modifiers.Modifier;
import com.gigabytedx.rpgleveling.shop.Shop;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Interact implements Listener {

	Main plugin;

	public Interact(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {

			Player damager = (Player) event.getDamager();
			try {
				Item itemUsed = Main.itemMap.get(damager.getItemInHand().getItemMeta().getDisplayName());
				for (Modifier buff : itemUsed.getBuffs()) {
					buff.applyBuff(damager, event.getEntity());
				}
				applyDamage((LivingEntity) event.getEntity(), itemUsed.getDamage());
			} catch (NullPointerException e) {
			}

		} else if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player damager = (Player) arrow.getShooter();
				if (damager.getItemInHand().getType().equals(Material.BOW)) {
					try {
						Item itemUsed = Main.itemMap.get(damager.getItemInHand().getItemMeta().getDisplayName());
						for (Modifier buff : itemUsed.getBuffs()) {
							buff.applyBuff(damager, (LivingEntity) event.getEntity());
						}
						if (arrow.isCritical()) {
							applyDamage((LivingEntity) event.getEntity(), Math.floor(itemUsed.getDamage() * 1.5));
							damager.sendMessage(ChatColor.GOLD + "1.5x Damage for critical hit!");
						} else {
							applyDamage((LivingEntity) event.getEntity(), itemUsed.getDamage());
						}
					} catch (NullPointerException e) {
					}
				}
			}
		} else if (event.getDamager() instanceof LivingEntity) {
			Set<ProtectedRegion> protectedRegions = WorldGuardPlugin.inst()
					.getRegionManager(event.getDamager().getLocation().getWorld())
					.getApplicableRegions(event.getDamager().getLocation()).getRegions();
			if (protectedRegions.size() > 0) {
				ProtectedRegion[] prArray = new ProtectedRegion[protectedRegions.size()];
				prArray = protectedRegions.toArray(prArray);
				String name = event.getDamager().getCustomName();
				name = name.substring(name.indexOf(ChatColor.GOLD + ""), name.length());
				name = name.substring(2);

				if (event.getEntity() instanceof Player) {
					applyDamage((Player) event.getEntity(),
							plugin.MobSpawningData
									.getConfigurationSection("Regions." + prArray[0].getId() + ".SpawnableMobs." + name)
									.getInt("Attack"));
				} else {
					((LivingEntity) event.getEntity()).damage(plugin.MobSpawningData
							.getConfigurationSection("Regions." + prArray[0].getId() + ".SpawnableMobs." + name)
							.getInt("Attack"));
				}
			}
			event.setCancelled(true);
		}

	}

	private void applyDamage(LivingEntity entity, double damage) {
		double armorProtectionValue = 0;

		for (ItemStack itemStack : entity.getEquipment().getArmorContents()) {
			try {
				armorProtectionValue += Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getProtection();
			} catch (NullPointerException e) {

			}
		}
		armorProtectionValue = armorProtectionValue * 4;
		double dmgToSubtract = armorProtectionValue / 100 * damage;
		damage -= dmgToSubtract;
		entity.damage(damage);

	}

	@EventHandler
	public void onHoldItemInHand(PlayerItemHeldEvent event) {

		checkArmor(event.getPlayer());
		try {
			Item itemUsed = Main.itemMap
					.get(event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName());
			for (Modifier buff : itemUsed.getBuffs()) {
				if (buff.getTrigger().equals("hold"))
					buff.applyBuff(event.getPlayer(), null);
			}
			for (Modifier buff : itemUsed.getDebuffs()) {
				if (buff.getTrigger().equals("hold"))
					buff.applyBuff(event.getPlayer(), null);
			}
		} catch (NullPointerException e) {
			// event.getPlayer().sendMessage("not using a custom item");
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof Player) {
			Player getPersonClicked = (Player) event.getRightClicked();
			if (plugin.locations.getLocationNames().contains(getPersonClicked.getName().toLowerCase())) {
				Shop.openShop(plugin, event.getPlayer(), getPersonClicked.getName());
			}

		}
	}

	@EventHandler
	public void onInventory(InventoryClickEvent event) {
	}

	@EventHandler
	public void onInventory(InventoryDragEvent event) {
	}

	@EventHandler
	public void onInventory(InventoryCloseEvent event) {
		checkArmor((Player) event.getPlayer());
		
		try {
			for (Modifier modifier : Main.itemMap.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())
					.getBuffs()) {
				modifier.applyBuff((Player) event.getPlayer(), null);
			}
			for (Modifier modifier : Main.itemMap.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())
					.getDebuffs()) {
				modifier.applyBuff((Player) event.getPlayer(), null);
			}
		} catch (NullPointerException e) {

		}
	}

	private void checkArmor(Player player) {
		for (PotionEffect effect : player.getActivePotionEffects()) {
			if (effect.getDuration() > 200000) {
				player.removePotionEffect(effect.getType());
			}
		}
		for (ItemStack itemStack : player.getEquipment().getArmorContents()) {
			try {
				for (Modifier modifier : Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getBuffs()) {
					modifier.applyBuff((Player) player, null);
				}
				for (Modifier modifier : Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getDebuffs()) {
					modifier.applyBuff((Player) player, null);
				}
			} catch (NullPointerException e) {

			}
		}
	}
}
