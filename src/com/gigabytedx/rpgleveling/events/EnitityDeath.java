package com.gigabytedx.rpgleveling.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.gigabytedx.rpgleveling.Main;
import com.gigabytedx.rpgleveling.Mobs.MobData;
import com.gigabytedx.rpgleveling.item.AddItemToInventory;
import com.gigabytedx.rpgleveling.item.Drop;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class EnitityDeath implements Listener {

	private Main plugin;
	// I dont know why but killing a mob with enchanted item runs death event
	// twice
	private Entity lastDead;

	public EnitityDeath(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		try {
			if (event.getEntity().getKiller() instanceof Player && !(lastDead.equals(event.getEntity()))) {
				lastDead = event.getEntity();
				setDrops(event);
			}
		} catch (NullPointerException e) {
			lastDead = event.getEntity();
			setDrops(event);
		}
		event.getDrops().clear();
	}

	private void setDrops(EntityDeathEvent event) {
		try {
			Set<ProtectedRegion> protectedRegions = WorldGuardPlugin.inst()
					.getRegionManager(event.getEntity().getLocation().getWorld())
					.getApplicableRegions(event.getEntity().getLocation()).getRegions();

			String mobName = event.getEntity().getCustomName();
			mobName = mobName.substring(mobName.indexOf(ChatColor.GOLD + ""), mobName.length());
			mobName = mobName.substring(2);
			try {
				if (plugin.regions.hasRegion(((ProtectedRegion) protectedRegions.toArray()[0]).getId())) {
					List<Drop> itemRandomPool = new ArrayList<>();
					MobData mob = plugin.regions.getRegion(((ProtectedRegion) protectedRegions.toArray()[0]).getId())
							.getMobData(mobName);
					for (Drop drop : mob.getDrops()) {
						for (int x = 0; x < drop.getSpawnRate(); x++) {
							itemRandomPool.add(drop);
						}
					}

					Random random = new Random();
					int randomIndex = random.nextInt(itemRandomPool.size());
					Drop drop = itemRandomPool.get(randomIndex);
					if (drop.isCustom()) {
						AddItemToInventory.addItem(event.getEntity().getKiller().getInventory(),
								Main.itemMap.get(ChatColor.BLUE + drop.getName()), plugin, true);
					} else {
						event.getEntity().getKiller().getInventory()
								.addItem(new ItemStack(Material.valueOf(drop.getType()), drop.getQty()));
					}
					Player damager = (Player) event.getEntity().getKiller();
					damager.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, mob.getXp()));
				}
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		} catch (NullPointerException e) {

		}
	}
}
