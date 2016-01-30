package com.gigabytedx.rpgleveling.events;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.cooldowns.Cooldown;
import com.gigabytedx.rpgleveling.item.Item;
import com.gigabytedx.rpgleveling.item.PotionItem;
import com.gigabytedx.rpgleveling.modifiers.Modifier;
import com.gigabytedx.rpgleveling.shop.Shop;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Interact implements Listener {

	Main plugin;

	public Interact(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void itemInteract(PlayerInteractEvent event) {
		if (!plugin.getConfig().getString("world name").equals(event.getPlayer().getLocation().getWorld().getName())) {
			return;
		}
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		try{
		if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Material.STAINED_GLASS_PANE)){
			event.setCancelled(true);
			return;
		}
		}catch(NullPointerException e){
			return;
		}
		try {
			Item itemUsed = Main.itemMap.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
			if (plugin.itemClassValue.getBaseClassValues(event.getPlayer()).get(itemUsed.getBaseClass()) < Main.itemMap
					.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).getClassLevelRequirement()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()
						+ ChatColor.DARK_RED
						+ " is not being used effectively because you do not have enough " + Main.itemMap
								.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).getBaseClass()
						+ " items equipped!");
				event.getPlayer().updateInventory();
				return;
			}

		} catch (NullPointerException e) {
		}

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
					}
					if (event.getPlayer().getItemInHand().getAmount() == 1) {
						Item item = Main.itemMap.get(event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
						plugin.playerCooldowns.addCooldown(event.getPlayer(),
								new Cooldown(((PotionItem) item).getCooldown(), item,
										event.getPlayer().getInventory().getHeldItemSlot(), event.getPlayer(), plugin));
					}
				}
		} catch (NullPointerException e) {
		}

	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		event.setDamage(0);
		if (!plugin.getConfig().getString("world name").equals(event.getEntity().getLocation().getWorld().getName()))
			return;
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			try {
				Item itemUsed = Main.itemMap.get(damager.getItemInHand().getItemMeta().getDisplayName());

				if (plugin.itemClassValue.getBaseClassValues(damager).get(itemUsed.getBaseClass()) >= Main.itemMap
						.get(damager.getItemInHand().getItemMeta().getDisplayName()).getClassLevelRequirement()) {

					for (Modifier buff : itemUsed.getBuffs()) {
						buff.applyBuff(damager, event.getEntity());
					}
					applyDamage((LivingEntity) event.getEntity(), itemUsed.getDamage(), damager);
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
							applyDamage((LivingEntity) event.getEntity(), Math.floor(itemUsed.getDamage() * 1.5),
									(Player) arrow.getShooter());
							damager.sendMessage(ChatColor.GOLD + "1.5x Damage for critical hit!");
						} else {
							applyDamage((LivingEntity) event.getEntity(), itemUsed.getDamage(),
									(Player) arrow.getShooter());
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
									.getInt("Attack"),
							null);
				} else {
					((LivingEntity) event.getEntity()).damage(plugin.MobSpawningData
							.getConfigurationSection("Regions." + prArray[0].getId() + ".SpawnableMobs." + name)
							.getInt("Attack"));
				}
			}
		}

	}

	private void applyDamage(LivingEntity entity, double damage, Player player) {
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
		try {
			for (PotionEffect effect : player.getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
					damage = damage + ((1 + effect.getAmplifier()) * 1.3);
				}
			}
		} catch (NullPointerException e) {

		}
		entity.damage(damage);

	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent event) {
//		if (!plugin.getConfig().getString("world name").equals(event.getPlayer().getLocation().getWorld().getName()))
//			return;
//		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
//			return;
//		}
		if (event.getRightClicked() instanceof Villager) {
			Villager getPersonClicked = (Villager) event.getRightClicked();
			if (plugin.locations.getLocationNames().contains(getPersonClicked.getName().toLowerCase())) {
				Shop.openShop(plugin, event.getPlayer(), getPersonClicked.getName());
			}

		}
	}

}
