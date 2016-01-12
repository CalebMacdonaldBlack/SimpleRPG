package com.gigabytedx.rpgleveling.events;

import java.util.Map;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.item.Item;
import com.gigabytedx.rpgleveling.modifiers.Modifier;
import com.gigabytedx.rpgleveling.modifiers.modifier.HealthIncrease;
import com.gigabytedx.rpgleveling.shop.Shop;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Interact implements Listener {

	Main plugin;

	public Interact(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void itemInteract(PlayerInteractEvent event) {
		try {
			Item itemUsed = Main.itemMap.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());

			if (plugin.itemClassValue.getBaseClassValues(event.getPlayer()).get(itemUsed.getBaseClass()) < Main.itemMap
					.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).getClassLevelRequirement()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()
						+ ChatColor.DARK_RED
						+ " is not being used effectively because you do not have enough " + Main.itemMap
								.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).getBaseClass()
						+ " items equipped");
			}

		} catch (NullPointerException e) {

		}

	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			System.out.println("Here");
			Player damager = (Player) event.getDamager();
			try {
				System.out.println("Starting");
				System.out.println(damager.getItemInHand().getItemMeta().getDisplayName());
				Item itemUsed = Main.itemMap.get(damager.getItemInHand().getItemMeta().getDisplayName());
				System.out.println("COMPARE" + itemUsed.getBaseClass());
				System.out.println(plugin.itemClassValue.getBaseClassValues(damager).get(itemUsed.getBaseClass()));
				System.out.println(Main.itemMap.get(damager.getItemInHand().getItemMeta().getDisplayName())
						.getClassLevelRequirement());
				System.out.println(damager.getItemInHand().getItemMeta().getDisplayName());

				if (plugin.itemClassValue.getBaseClassValues(damager).get(itemUsed.getBaseClass()) >= Main.itemMap
						.get(damager.getItemInHand().getItemMeta().getDisplayName()).getClassLevelRequirement()) {
					System.out.println("We have enough");

					for (Modifier buff : itemUsed.getBuffs()) {
						buff.applyBuff(damager, event.getEntity());
					}
					applyDamage((LivingEntity) event.getEntity(), itemUsed.getDamage());
				} else {
					(damager)
							.sendMessage(damager.getItemInHand().getItemMeta().getDisplayName() + ChatColor.DARK_RED
									+ " is not being used effectively because you do not have enough " + Main.itemMap
											.get(damager.getItemInHand().getItemMeta().getDisplayName()).getBaseClass()
									+ " items equipped");
					event.setDamage(1);
					return;
				}
			} catch (NullPointerException e) {
				System.out.println("OI BUDDY NULL ERRE");
				e.printStackTrace();
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
				try {
					if (plugin.itemClassValue.getBaseClassValues((Player) entity)
							.get(Main.itemMap.get(itemStack.getItemMeta().getDisplayName())
									.getBaseClass()) >= Main.itemMap.get(itemStack.getItemMeta().getDisplayName())
											.getClassLevelRequirement()) {
						armorProtectionValue += Main.itemMap.get(itemStack.getItemMeta().getDisplayName())
								.getProtection();
					} else {
						((Player) entity).sendMessage(itemStack.getItemMeta().getDisplayName() + ChatColor.DARK_RED
								+ "Is not being used effectively because you do not have enough "
								+ Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getBaseClass()
								+ " items equipped");
					}
				} catch (ClassCastException e) {
					armorProtectionValue += Main.itemMap.get(itemStack.getItemMeta().getDisplayName()).getProtection();
				}
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
		
		if(event.getNewSlot() > 2 && event.getNewSlot() < 9){
			System.out.println("canlesded mate");
			event.setCancelled(true);
		}

		checkArmor(event.getPlayer());
		for (int itemSlot = 0; itemSlot < 8; itemSlot++) {
			try {
				Item itemUsed = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());
				System.out.println(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());
				
				Map<String, Integer> baseClassValue = plugin.itemClassValue.getBaseClassValues(event.getPlayer());
				System.out.println(baseClassValue.toString());
				String baseClassForItem = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName())
						.getBaseClass();
				int itemBaseClassLevelRequirement = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName())
						.getClassLevelRequirement();
				
				if (baseClassValue.get(baseClassForItem) >= itemBaseClassLevelRequirement) {
					System.out.println("made it this far");
					if (itemUsed != null)
						for (Modifier buff : itemUsed.getBuffs()) {
							System.out.println("APPLYING BUFF: " + buff.getName());
							if (buff.getTrigger().equals("have"))
								buff.applyBuff(event.getPlayer(), null);
							if (buff.getTrigger().equals("hold") && itemSlot == event.getNewSlot()) {
								buff.applyBuff(event.getPlayer(), null);
							}
						}

				} else {
					(event.getPlayer())
							.sendMessage(
									event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName()
											+ ChatColor.DARK_RED
											+ " is not being used effectively because you do not have enough "
											+ Main.itemMap.get(event.getPlayer().getInventory().getItem(itemSlot)
													.getItemMeta().getDisplayName()).getBaseClass()
									+ " items equipped");
					event.getPlayer().setMaxHealth(20);
				}

				if (itemUsed != null) {
					for (Modifier buff : itemUsed.getDebuffs()) {
						if (buff.getTrigger().equals("have"))
							buff.applyBuff(event.getPlayer(), null);
						if (buff.getTrigger().equals("hold") && itemSlot == event.getNewSlot()) {
							buff.applyBuff(event.getPlayer(), null);
						}
					}
				}
			} catch (NullPointerException e) {
			}
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
		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			try {
				Item itemUsed = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());
				if (itemUsed != null)
					for (Modifier modifier : itemUsed.getBuffs()) {
						if (modifier.getTrigger().equals("have"))
							modifier.applyBuff((Player) event.getPlayer(), null);
					}
			} catch (NullPointerException e) {

			}
		}

		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			try {
				Item itemUsed = Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName());
				if (itemUsed != null)
					for (Modifier modifier : itemUsed.getDebuffs()) {
						if (modifier.getTrigger().equals("have"))
							modifier.applyBuff((Player) event.getPlayer(), null);
					}
			} catch (NullPointerException e) {

			}
		}
		boolean hasHealthIncreaseItem = false;
		for (int itemSlot = 0; itemSlot < 9; itemSlot++) {
			try {
				for (Modifier modifier : Main.itemMap
						.get(event.getPlayer().getInventory().getItem(itemSlot).getItemMeta().getDisplayName())
						.getBuffs()) {
					if (modifier instanceof HealthIncrease) {
						hasHealthIncreaseItem = true;
					}
				}
			} catch (NullPointerException e) {

			}
		}
		if (!hasHealthIncreaseItem)
			event.getPlayer().setMaxHealth(20);
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
